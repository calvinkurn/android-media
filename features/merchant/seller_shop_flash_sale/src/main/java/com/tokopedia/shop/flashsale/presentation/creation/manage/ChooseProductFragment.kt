package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentChooseProductBinding
import com.tokopedia.shop.flashsale.common.constant.ChooseProductConstant.PRODUCT_LIST_SIZE
import com.tokopedia.shop.flashsale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flashsale.common.extension.*
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.presentation.creation.manage.adapter.ReserveProductAdapter
import com.tokopedia.shop.flashsale.presentation.creation.manage.dialog.ShopClosedDialog
import com.tokopedia.shop.flashsale.presentation.creation.manage.enums.ShopStatus
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.SelectedProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.ChooseProductViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
class ChooseProductFragment : BaseSimpleListFragment<ReserveProductAdapter, ReserveProductModel>() {

    companion object {
        const val SWIPEPROGRESS_MARGIN = 120
        const val GUIDELINE_MARGIN_MIN = 0
        const val GUIDELINE_ANIMATION_DELAY = 500L

        @JvmStatic
        fun newInstance(campaignId: String): ChooseProductFragment {
            val fragment = ChooseProductFragment()
            val bundle = Bundle()
            bundle.putString(ChooseProductActivity.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: ChooseProductViewModel
    private var binding by autoClearedNullable<SsfsFragmentChooseProductBinding>()
    private var guidelineMargin = GUIDELINE_MARGIN_MIN
    private var guidelineMarginMax = GUIDELINE_MARGIN_MIN
    private val campaignId by lazy {
        arguments?.getString(ChooseProductActivity.BUNDLE_KEY_CAMPAIGN_ID).orEmpty()
    }
    private val animateScrollDebounce: (Int) -> Unit by lazy {
        debounce(GUIDELINE_ANIMATION_DELAY, GlobalScope) {
            view?.post { animateScroll(it) }
        }
    }

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
        guidelineMarginMax = binding?.guidelineFooter?.setGuidelineEnd().orZero()
        guidelineMargin = guidelineMarginMax
        viewModel.getShopInfo()
    }

    override fun createAdapter() = ReserveProductAdapter(::onSelectedItemChanges)

    override fun getRecyclerView(view: View) = binding?.rvProducts

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        binding?.swipeRefreshProducts?.setProgressViewOffset(false, Int.ZERO, SWIPEPROGRESS_MARGIN)
        return binding?.swipeRefreshProducts
    }

    override fun getPerPage() = PRODUCT_LIST_SIZE

    override fun addElementToAdapter(list: List<ReserveProductModel>) {
        adapter?.addItems(list)
    }

    override fun loadData(page: Int) {
        viewModel.getReserveProductList(campaignId, page)
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
        if (adapter?.itemCount.orZero() < getPerPage()) return
        guidelineMargin -= yScrollAmount
        if (guidelineMargin < GUIDELINE_MARGIN_MIN) guidelineMargin = GUIDELINE_MARGIN_MIN
        if (guidelineMargin > guidelineMarginMax) guidelineMargin = guidelineMarginMax
        binding?.guidelineFooter?.setGuidelineEnd(guidelineMargin)
        binding?.guidelineHeader?.setGuidelineBegin(guidelineMargin)
        animateScrollDebounce.invoke(yScrollAmount)
    }

    private fun onSelectedItemChanges(selectedItem: MutableList<SelectedProductModel>) {
        viewModel.setSelectedItems(selectedItem)
    }

    private fun setupObservers() {
        setupReserveProductListObserver()
        setupErrorsObserver()
        setupSelectionItemsObserver()
        setupIsSelectionValidObserver()
        setupIsSelectionHasVariantObserver()
        setupIsAddProductSuccessObserver()
        setupShopInfoObserver()
    }

    private fun setupIsAddProductSuccessObserver() {
        viewModel.isAddProductSuccess.observe(viewLifecycleOwner) {
            binding?.btnSave?.isLoading = false
            if (it) {
                activity?.setResult(Activity.RESULT_OK, Intent())
                activity?.finish()
            }
        }
    }

    private fun setupErrorsObserver() {
        viewModel.errors.observe(viewLifecycleOwner) {
            showGetListError(it)
            binding?.btnSave?.isLoading = false
        }
    }

    private fun setupReserveProductListObserver() {
        viewModel.reserveProductList.observe(viewLifecycleOwner) {
            renderList(it, hasNextPage = it.size == getPerPage())
        }
    }

    private fun setupSelectionItemsObserver() {
        viewModel.selectedItems.observe(viewLifecycleOwner) { selectedItems ->
            val selectedCount = selectedItems.filter { !it.isProductPreviouslySubmitted }.size
            binding?.tvSelectedProduct?.text =
                getString(R.string.chooseproduct_selected_product_suffix, selectedCount)
            setupButtonSave(selectedItems)
        }
    }

    private fun setupIsSelectionValidObserver() {
        viewModel.isSelectionValid.observe(viewLifecycleOwner) {
            binding?.btnSave?.isEnabled = it
            adapter?.run {
                if (getSelectedProduct().size.isMoreThanZero()) {
                    setInputEnabled(it)
                } else {
                    // always enabled when there is no selected products
                    setInputEnabled(true)
                }
            }
        }
    }

    private fun setupIsSelectionHasVariantObserver() {
        viewModel.isSelectionHasVariant.observe(viewLifecycleOwner) {
            binding?.tvSelectedProductVariant?.isVisible = it
        }
    }

    private fun setupShopInfoObserver() {
        viewModel.shopStatus.observe(viewLifecycleOwner) {
            if (it == ShopStatus.CLOSED) showShopClosedDialog()
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

    private fun setupButtonSave(selectedProduct: List<SelectedProductModel>) {
        if (selectedProduct.isEmpty()) {
            binding?.btnSave?.text = getString(R.string.chooseproduct_save_button_text_empty)
        } else {
            binding?.btnSave?.text = getString(R.string.chooseproduct_save_button_text, selectedProduct.size)
        }

        binding?.btnSave?.setOnClickListener {
            viewModel.addProduct(campaignId)
            binding?.btnSave?.isLoading = true
        }
    }

    private fun showShopClosedDialog() {
        val dialog = ShopClosedDialog(primaryCTAAction = ::goToShopSettings)
        dialog.setOnDismissListener {
            activity?.finish()
        }
        dialog.show(childFragmentManager)
    }

    private fun goToShopSettings() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS)
    }

    private fun animateScroll(scrollingAmount: Int) {
        if (scrollingAmount.isMoreThanZero()) {
            binding?.guidelineHeader?.animateSlide(guidelineMargin, GUIDELINE_MARGIN_MIN, true)
            binding?.guidelineFooter?.animateSlide(guidelineMargin, GUIDELINE_MARGIN_MIN, false)
            guidelineMargin = GUIDELINE_MARGIN_MIN
        } else if (scrollingAmount.isLessThanZero()){
            binding?.guidelineHeader?.animateSlide(guidelineMargin, guidelineMarginMax, true)
            binding?.guidelineFooter?.animateSlide(guidelineMargin, guidelineMarginMax, false)
            guidelineMargin = guidelineMarginMax
        }
    }
}