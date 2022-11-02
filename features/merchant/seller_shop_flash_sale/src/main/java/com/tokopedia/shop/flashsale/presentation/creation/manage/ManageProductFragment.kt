package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentManageProductBinding
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.extension.*
import com.tokopedia.shop.flashsale.common.preference.SharedPreferenceDataStore
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType.*
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.presentation.creation.highlight.ManageHighlightedProductActivity
import com.tokopedia.shop.flashsale.presentation.creation.manage.adapter.ManageProductListAdapter
import com.tokopedia.shop.flashsale.presentation.creation.manage.bottomsheet.EditProductInfoBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.manage.dialog.ProductDeleteDialog
import com.tokopedia.shop.flashsale.presentation.creation.manage.dialog.ShopClosedDialog
import com.tokopedia.shop.flashsale.presentation.creation.manage.dialog.showSuccessSaveCampaignDraft
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ManageProductFragment : BaseDaggerFragment() {

    companion object {
        private const val PAGE_SIZE = 50
        private const val LIST_TYPE = 0
        private const val RECYCLERVIEW_ITEM_FIRST_INDEX = 0
        private const val DELAY = 3000L
        private const val REQUEST_CODE = 123
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_no_active_campaign.png"
        private const val INCOMPLETE_PRODUCT_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/product_incomplete.png"
        private const val SCROLL_ANIMATION_DELAY = 500L

        private const val GUIDELINE_MARGIN_FOOTER_HALF_WAY = 95
        private const val GUIDELINE_MARGIN_FOOTER_MIN = 0
        private const val GUIDELINE_MARGIN_HEADER_MIN = 0

        @JvmStatic
        fun newInstance(campaignId: Long, pageMode: PageMode): ManageProductFragment {
            return ManageProductFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var sharedPreference: SharedPreferenceDataStore

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageProductViewModel::class.java) }
    private val campaignId by lazy {
        arguments?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID).orZero()
    }
    private val pageMode by lazy {
        arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
    }

    private val manageProductListAdapter by lazy {
        ManageProductListAdapter(
            onEditClicked = ::editProduct,
            onDeleteClicked = ::deleteProduct
        )
    }
    private var binding by autoClearedNullable<SsfsFragmentManageProductBinding>()

    private var guidelineMarginFooter = GUIDELINE_MARGIN_FOOTER_MIN
    private var guidelineMarginFooterMax = GUIDELINE_MARGIN_FOOTER_MIN
    private var guidelineMarginHeader = GUIDELINE_MARGIN_HEADER_MIN
    private var guidelineMarginHeaderMax = GUIDELINE_MARGIN_HEADER_MIN

    private val animateScrollDebounce: (Int) -> Unit by lazy {
        debounce(SCROLL_ANIMATION_DELAY, lifecycleScope) {
            view?.post { animateScroll(it) }
        }
    }

    override fun getScreenName(): String = ManageProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
        sharedPreference.isCampaignInfoCoachMarkDismissed()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentManageProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        setupView()
        handlePageMode()
        observeShopStatus()
        observeProductList()
        observeIncompleteProducts()
        observeRemoveProductsStatus()
        observeBannerType()
        viewModel.getShopStatus()
    }

    private fun setupView() {
        binding?.apply {
            tpgAddProduct.setOnClickListener {
                if (manageProductListAdapter.itemCount < PAGE_SIZE) {
                    showChooseProductPage()
                } else {
                    binding?.cardBottomButtonGroup.showError(
                        getString(R.string.manage_product_maximum_product_error)
                    )
                }
            }
            imageIncompleteProductInfo.setImageUrl(INCOMPLETE_PRODUCT_IMAGE_URL)
            btnSaveDraft.setOnClickListener {
                context?.let { ctx ->
                    showSuccessSaveCampaignDraft(
                        ctx,
                        viewModel.campaignName,
                        action = { routeToCampaignListPage() })
                }
            }
            btnContinue.setOnClickListener {
                viewModel.onButtonProceedTapped()
                context?.let { context ->
                    ManageHighlightedProductActivity.start(context, campaignId, pageMode)
                }
            }
        }
        setupScrollListener()
    }

    private fun handlePageMode() {
        if (pageMode == PageMode.UPDATE || pageMode == PageMode.DRAFT) {
            binding?.btnSaveDraft?.gone()
        }
    }

    private fun handleCoachMark() {
        val shouldShowCoachMark = !sharedPreference.isManageProductCoachMarkDismissed()
        val hasProduct = manageProductListAdapter.itemCount.isMoreThanZero()
        if (shouldShowCoachMark && !viewModel.getIsCoachMarkShown() && hasProduct) {
            showCoachMark()
        }
    }

    private fun observeProductList() {
        viewModel.products.observe(viewLifecycleOwner) { result ->
            showLoader()
            when (result) {
                is Success -> {
                    hideLoader()
                    displayProducts(result.data)
                    if (result.data.productList.size.isMoreThanZero()) {
                        hideEmptyState()
                    } else {
                        showEmptyState()
                        if (viewModel.autoNavigateToChooseProduct()) {
                            showChooseProductPage()
                        }
                    }
                }
                is Fail -> {
                    hideLoader()
                    showEmptyState()
                    result.throwable.localizedMessage?.let { view.showError(it) }
                }
            }
        }
    }

    private fun observeIncompleteProducts() {
        viewModel.incompleteProducts.observe(viewLifecycleOwner) {
            if (viewModel.autoShowEditProduct) {
                showEditProductBottomSheet(it)
                viewModel.autoShowEditProduct = false
            }
        }
    }

    private fun observeShopStatus() {
        viewModel.shopStatus.observeOnce(viewLifecycleOwner) {
            if (it is Success) {
                if (it.data == ShopStatusDef.CLOSED) {
                    showShopClosedDialog()
                } else {
                    loadProductsData()
                }
            } else if (it is Fail) {
                view?.showError(it.throwable)
            }
        }
    }

    private fun observeRemoveProductsStatus() {
        viewModel.removeProductsStatus.observe(viewLifecycleOwner) {
            doOnDelayFinished(DELAY) {
                if (it is Success) {
                    loadProductsData()
                    showSuccessDeleteProductToaster()
                } else if (it is Fail) {
                    binding?.cardBottomButtonGroup?.showError(it.throwable)
                }
            }
        }
    }

    private fun observeBannerType() {
        viewModel.bannerType.observe(viewLifecycleOwner) { type ->
            when (type) {
                EMPTY_BANNER -> {
                    showEmptyProductBanner()
                }
                ERROR_BANNER -> {
                    showErrorProductBanner()
                }
                HIDE_BANNER -> {
                    hideBanner()
                }
                else -> {
                    hideBanner()
                }
            }
        }
    }

    private fun loadProductsData() {
        viewModel.getProducts(campaignId, LIST_TYPE)
    }

    private fun showSuccessEditProductToaster() {
        binding?.cardBottomButtonGroup.showToaster(
            getString(R.string.manage_product_success_edit_toaster_message)
        )
    }

    private fun showSuccessDeleteProductToaster() {
        binding?.cardBottomButtonGroup.showToaster(
            getString(R.string.manage_product_success_delete_toaster_message)
        )
    }

    private fun displayProducts(productList: SellerCampaignProductList) {
        viewModel.getBannerType(productList)
        viewModel.getCampaignDetail(campaignId)
        handleScrollUp()
        binding?.apply {
            recyclerViewProduct.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            manageProductListAdapter.clearAll()
            manageProductListAdapter.submit(productList.productList)
            recyclerViewProduct.adapter = manageProductListAdapter
            tpgProductCount.text = String.format(
                getString(R.string.manage_product_placeholder_product_count),
                productList.totalProduct
            )
        }
        handleCoachMark()
    }

    private fun setupScrollListener() {
        binding?.apply {
            setInitialMaxGuideline()
            recyclerViewProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    guidelineMarginHeader -= dy
                    guidelineMarginFooter -= dy
                    setGuidelineMinAndMax()
                    setGuidelineHeader()
                    setGuidelineFooter()
                    if (guidelineMarginFooter <= GUIDELINE_MARGIN_FOOTER_HALF_WAY) {
                        animateScrollDebounce.invoke(dy)
                    } else {
                        animateScrollDebounce.invoke(Constant.ZERO)
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(Constant.ONE)
                        && newState == RecyclerView.SCROLL_STATE_DRAGGING
                    ) {
                        animateScrollDebounce.invoke(Constant.ZERO)
                    }
                }
            })
        }
    }

    private fun setInitialMaxGuideline() {
        binding?.apply {
            guidelineMarginHeaderMax = guidelineHeader.getGuidelineBegin().orZero()
            guidelineMarginFooterMax = guidelineFooter.getGuidelineEnd().orZero()
            guidelineMarginHeader = guidelineMarginHeaderMax
            guidelineMarginFooter = guidelineMarginFooterMax
        }
    }

    private fun setGuidelineMinAndMax() {
        if (guidelineMarginHeader < GUIDELINE_MARGIN_HEADER_MIN)
            guidelineMarginHeader = GUIDELINE_MARGIN_HEADER_MIN
        if (guidelineMarginHeader > guidelineMarginHeaderMax)
            guidelineMarginHeader = guidelineMarginHeaderMax
        if (guidelineMarginFooter < GUIDELINE_MARGIN_FOOTER_MIN)
            guidelineMarginFooter = GUIDELINE_MARGIN_FOOTER_MIN
        if (guidelineMarginFooter > guidelineMarginFooterMax)
            guidelineMarginFooter = guidelineMarginFooterMax
    }

    private fun setGuidelineHeader() {
        if (viewModel.bannerType.value == HIDE_BANNER) {
            binding?.guidelineHeader?.setGuidelineBegin(GUIDELINE_MARGIN_HEADER_MIN)
        } else {
            binding?.guidelineHeader?.setGuidelineBegin(guidelineMarginHeader)
        }
    }

    private fun setGuidelineFooter() {
        binding?.guidelineFooter?.setGuidelineEnd(guidelineMarginFooter)
    }

    private fun showEmptyState() {
        binding?.apply {
            emptyState.visible()
            cardIncompleteProductInfo.gone()
            tpgProductCount.gone()
            tpgAddProduct.gone()
            recyclerViewProduct.gone()
            cardBottomButtonGroup.gone()
            showHeaderPadding(false)

            emptyState.setImageUrl(EMPTY_STATE_IMAGE_URL)
            emptyState.setPrimaryCTAClickListener {
                if (manageProductListAdapter.itemCount < PAGE_SIZE) {
                    showChooseProductPage()
                } else {
                    binding?.cardBottomButtonGroup.showError(
                        getString(R.string.manage_product_maximum_product_error)
                    )
                }
            }
        }
    }

    private fun hideEmptyState() {
        binding?.apply {
            emptyState.gone()
            tpgProductCount.visible()
            tpgAddProduct.visible()
            recyclerViewProduct.visible()
            cardBottomButtonGroup.visible()
        }
    }

    private fun showEmptyProductBanner() {
        binding?.apply {
            tickerErrorProductInfo.gone()
            cardIncompleteProductInfo.visible()
            showHeaderPadding(true)
            btnContinue.disable()
        }
    }

    private fun showErrorProductBanner() {
        binding?.apply {
            tickerErrorProductInfo.visible()
            cardIncompleteProductInfo.gone()
            showHeaderPadding(true)
            btnContinue.disable()
        }
    }

    private fun hideBanner() {
        binding?.apply {
            tickerErrorProductInfo.gone()
            cardIncompleteProductInfo.gone()
            showHeaderPadding(false)
            btnContinue.enable()
        }
    }

    private fun showHeaderPadding(isVisible: Boolean) {
        binding?.apply {
            val specSize = View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
            headerBackground.measure(specSize, specSize)
            val headerHeight = if (isVisible) headerBackground.measuredHeight else Int.ZERO
            recyclerViewProduct.setPadding(Int.ZERO, headerHeight, Int.ZERO, Int.ZERO)
        }
    }

    private fun showCoachMark() {
        binding?.recyclerViewProduct?.post {
            val btnAddProductAnchorView = binding?.tpgAddProduct
            val coachMarkItems = arrayListOf<CoachMark2Item>()
            try {
                val view = binding?.recyclerViewProduct?.getChildAt(RECYCLERVIEW_ITEM_FIRST_INDEX)
                val holder =
                    view?.let { binding?.recyclerViewProduct?.findContainingViewHolder(it) }
                holder?.let {
                    coachMarkItems.add(
                        CoachMark2Item(
                            holder.itemView.findViewById(R.id.btn_update_product),
                            "",
                            getString(R.string.manage_product_first_list_coachmark),
                            CoachMark2.POSITION_BOTTOM
                        )
                    )
                    btnAddProductAnchorView?.let {
                        coachMarkItems.add(
                            CoachMark2Item(
                                it,
                                "",
                                getString(R.string.manage_product_second_list_coachmark),
                                CoachMark2.POSITION_BOTTOM
                            )
                        )
                    }

                    val coachMark = activity?.let { act -> CoachMark2(act) }
                    coachMark?.showCoachMark(coachMarkItems)
                    viewModel.setIsCoachMarkShown(true)
                    coachMark?.onFinishListener = {
                        sharedPreference.markManageProductCoachMarkComplete()
                    }
                    coachMark?.onDismissListener = {
                        sharedPreference.markManageProductCoachMarkComplete()
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun showLoader() {
        binding?.apply {
            loader.visible()
            cardIncompleteProductInfo.gone()
            tpgProductCount.gone()
            tpgAddProduct.gone()
            emptyState.gone()
            recyclerViewProduct.gone()
            cardBottomButtonGroup.gone()
            showHeaderPadding(false)
        }
    }

    private fun hideLoader() {
        binding?.apply {
            loader.gone()
        }
    }

    private fun showEditProductBottomSheet(productList: List<SellerCampaignProductList.Product>) {
        if (productList.isEmpty()) return
        val bottomSheet = EditProductInfoBottomSheet.newInstance(campaignId, productList)
        bottomSheet.setOnEditProductSuccessListener {
            doOnDelayFinished(DELAY) {
                loadProductsData()
                showSuccessEditProductToaster()
            }
        }
        bottomSheet.setOnDeleteProductSuccessListener {
            doOnDelayFinished(DELAY) {
                loadProductsData()
                showSuccessDeleteProductToaster()
            }
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun showChooseProductPage() {
        val context = context ?: return
        val intent = ChooseProductActivity.createIntent(
            context,
            campaignId.toString(),
            manageProductListAdapter.itemCount
        )
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun editProduct(product: SellerCampaignProductList.Product) {
        showEditProductBottomSheet(listOf(product))
    }

    private fun deleteProduct(product: SellerCampaignProductList.Product) {
        ProductDeleteDialog().apply {
            setOnPrimaryActionClick {
                showLoader()
                viewModel.removeProducts(campaignId, listOf(product))
            }
            show(context ?: return)
        }
    }

    private fun routeToCampaignListPage() {
        val context = context ?: return
        CampaignListActivity.start(context, isSaveDraft = true, previousPageMode = pageMode)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                viewModel.autoShowEditProduct = true
                viewModel.setAutoNavigateToChooseProduct(false)
                showLoader()
                doOnDelayFinished(DELAY) {
                    viewModel.getProducts(campaignId, LIST_TYPE)
                }
            }
        }
    }

    private fun animateScroll(scrollingAmount: Int) {
        if (scrollingAmount.isMoreThanZero()) {
            handleScrollDown()
        } else {
            handleScrollUp()
        }
    }

    private fun handleScrollDown() {
        binding?.apply {
            guidelineHeader.animateSlide(
                guidelineMarginHeader,
                GUIDELINE_MARGIN_HEADER_MIN,
                true
            )
            guidelineFooter.animateSlide(
                guidelineMarginFooter,
                GUIDELINE_MARGIN_FOOTER_MIN,
                false
            )
            guidelineMarginFooter = GUIDELINE_MARGIN_FOOTER_MIN
            guidelineMarginHeader = GUIDELINE_MARGIN_HEADER_MIN
        }
    }

    private fun handleScrollUp() {
        binding?.apply {
            if (viewModel.bannerType.value == HIDE_BANNER) {
                guidelineHeader.setGuidelineBegin(GUIDELINE_MARGIN_HEADER_MIN)
                guidelineMarginHeader = GUIDELINE_MARGIN_HEADER_MIN

                guidelineFooter.animateSlide(
                    guidelineMarginFooter,
                    guidelineMarginFooterMax,
                    false
                )
                guidelineMarginFooter = guidelineMarginFooterMax
            } else {
                guidelineHeader.animateSlide(
                    guidelineMarginHeader,
                    guidelineMarginHeaderMax,
                    true
                )
                guidelineFooter.animateSlide(
                    guidelineMarginFooter,
                    guidelineMarginFooterMax,
                    false
                )
                guidelineMarginFooter = guidelineMarginFooterMax
                guidelineMarginHeader = guidelineMarginHeaderMax
            }
        }
    }
}
