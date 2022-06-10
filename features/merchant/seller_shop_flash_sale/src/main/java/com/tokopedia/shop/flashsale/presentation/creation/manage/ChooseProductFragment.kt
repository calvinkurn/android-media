package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentChooseProductBinding
import com.tokopedia.shop.flashsale.common.constant.ChooseProductConstant.PRODUCT_LIST_SIZE
import com.tokopedia.shop.flashsale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.presentation.creation.manage.adapter.ReserveProductAdapter
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.ChooseProductViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ChooseProductFragment : BaseSimpleListFragment<ReserveProductAdapter, ReserveProductModel>() {

    @Inject
    lateinit var viewModel: ChooseProductViewModel

    private var binding by autoClearedNullable<SsfsFragmentChooseProductBinding>()

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

        viewModel.reserveProductList.observe(viewLifecycleOwner) {
            renderList(it, hasNextPage = it.size == getPerPage())
        }

        viewModel.errors.observe(viewLifecycleOwner) {
            showErrorToaster(it)
        }
    }

    private fun showErrorToaster(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        Toaster.build(view = view ?: return, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
            .show()
    }

    override fun createAdapter() = ReserveProductAdapter()

    override fun getRecyclerView(view: View) = binding?.rvProducts

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? = null

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
    }

    override fun onHideLoading() {
    }

    override fun onDataEmpty() {
    }

    override fun onGetListError(message: String) {
    }

    var guidelineBegin = 58
    override fun onScrolled(xScrollAmount: Int, yScrollAmount: Int) {
        guidelineBegin -= yScrollAmount
        if (guidelineBegin < 0) guidelineBegin = 0
        if (guidelineBegin > 58) guidelineBegin = 58
        binding?.guideline3?.setGuidelineBegin(guidelineBegin)
        binding?.guideline4?.setGuidelineEnd(guidelineBegin)
    }
}