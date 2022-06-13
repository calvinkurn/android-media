package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentChooseProductBinding
import com.tokopedia.shop.flashsale.common.constant.ChooseProductConstant.PRODUCT_LIST_SIZE
import com.tokopedia.shop.flashsale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flashsale.common.extension.setFragmentToUnifyBgColor
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.presentation.creation.manage.adapter.ReserveProductAdapter
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.ChooseProductViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ChooseProductFragment : BaseSimpleListFragment<ReserveProductAdapter, ReserveProductModel>() {

    @Inject
    lateinit var viewModel: ChooseProductViewModel

    private var binding by autoClearedNullable<SsfsFragmentChooseProductBinding>()
    private var guidelineBegin = 58

    override fun getScreenName() = ChooseProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentChooseProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()

        setupObservers()
        setupSearchBar()
    }

    override fun createAdapter() = ReserveProductAdapter(::onSelectedItemChanges)

    override fun getRecyclerView(view: View) = binding?.rvProducts

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        binding?.swipeRefreshProducts?.setProgressViewOffset(false, 0, 120)
        return binding?.swipeRefreshProducts
    }

    override fun getPerPage() = PRODUCT_LIST_SIZE

    override fun addElementToAdapter(list: List<ReserveProductModel>) {
        adapter?.addItems(list)
    }

    override fun loadData(page: Int) {
        viewModel.getReserveProductList(page)
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {
        binding?.emptyStateSearch?.hide()
        binding?.emptyStateProduct?.hide()
        binding?.loaderProducts?.show()
    }

    override fun onHideLoading() {
        binding?.loaderProducts?.hide()
    }

    override fun onDataEmpty() {
        if (viewModel.isSearching()) {
            binding?.emptyStateSearch?.show()
        } else {
            binding?.emptyStateProduct?.show()
        }
    }

    override fun onGetListError(message: String) {
        view?.showError(message)
    }

    override fun onScrolled(xScrollAmount: Int, yScrollAmount: Int) {
        guidelineBegin -= yScrollAmount
        if (guidelineBegin < 0) guidelineBegin = 0
        if (guidelineBegin > 64) guidelineBegin = 64
        binding?.guideline3?.setGuidelineBegin(guidelineBegin)
        binding?.guideline4?.setGuidelineEnd(guidelineBegin)
    }

    private fun onSelectedItemChanges(selectedItem: MutableList<String>) {
        binding?.tvSelectedProduct?.text =
            getString(R.string.chooseproduct_selected_product_suffix, selectedItem.size)
    }

    private fun setupObservers() {
        setupReserveProductListObserver()
        setupErrorsObserver()
    }

    private fun setupErrorsObserver() {
        viewModel.errors.observe(viewLifecycleOwner) {
            showGetListError(it)
        }
    }

    private fun setupReserveProductListObserver() {
        viewModel.reserveProductList.observe(viewLifecycleOwner) {
            renderList(it, hasNextPage = it.size == getPerPage())
        }
    }

    private fun setupSearchBar() {
        binding?.searchBarProduct?.searchBarTextField?.setOnEditorActionListener {
                textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setSearchKeyword(textView.text.toString())
                loadInitialData()
            }
            return@setOnEditorActionListener false
        }
    }
}