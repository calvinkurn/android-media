package com.tokopedia.tkpd.flashsale.presentation.manageproductlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.campaign.base.BaseCampaignManageProductListFragment
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.components.adapter.LoadingDelegateAdapter
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.entity.LoadingItem
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsDialogProductSubmissionSseBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionSseResult
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionSseResult.Status.COMPLETE
import com.tokopedia.tkpd.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.ChooseProductActivity
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.detail.CampaignDetailActivity
import com.tokopedia.tkpd.flashsale.presentation.detail.bottomsheet.CampaignDetailBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import com.tokopedia.tkpd.flashsale.presentation.dialogconfirmation.ConfirmationDialog
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant.ManageProductNonVariantActivity
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.ManageProductVariantActivity
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.delegate.FlashSaleManageProductListItemDelegate
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.delegate.FlashSaleManageProductListItemGlobalErrorDelegate
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.delegate.FlashSaleManageProductListItemShimmeringDelegate
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListGlobalErrorItem
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListItem
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListShimmeringItem
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.viewholder.FlashSaleManageProductListGlobalErrorViewHolder
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.viewholder.FlashSaleManageProductListItemViewHolder
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel.FlashSaleManageProductListUiEffect
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel.FlashSaleManageProductListUiEvent
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleRequestCodeConstant.REQUEST_CODE_MANAGE_PRODUCT_NON_VARIANT
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleRequestCodeConstant.REQUEST_CODE_MANAGE_PRODUCT_VARIANT
import com.tokopedia.tkpd.flashsale.util.tracker.FlashSaleManageProductListPageTracker
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ANNOUNCEMENT
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FlashSaleManageProductListFragment :
    BaseCampaignManageProductListFragment<CompositeAdapter>(),
    FlashSaleManageProductListItemViewHolder.Listener,
    HasPaginatedList by HasPaginatedListImpl(),
    FlashSaleManageProductListGlobalErrorViewHolder.Listener {

    companion object {

        @JvmStatic
        fun newInstance(
            reservationId: String,
            campaignId: String,
            tabName: String
        ): FlashSaleManageProductListFragment {
            val fragment = FlashSaleManageProductListFragment()
            val bundle = Bundle()
            bundle.putString(BundleConstant.BUNDLE_KEY_RESERVATION_ID, reservationId)
            bundle.putString(BundleConstant.BUNDLE_FLASH_SALE_ID, campaignId)
            bundle.putString(BundleConstant.BUNDLE_KEY_TAB_NAME, tabName)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val reservationId by lazy {
        arguments?.getString(BundleConstant.BUNDLE_KEY_RESERVATION_ID).orEmpty()
    }

    private val campaignId by lazy {
        arguments?.getString(BundleConstant.BUNDLE_FLASH_SALE_ID).orEmpty()
    }

    private val tabName by lazy {
        arguments?.getString(BundleConstant.BUNDLE_KEY_TAB_NAME).orEmpty()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var tracker: FlashSaleManageProductListPageTracker
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(FlashSaleManageProductListListViewModel::class.java) }
    private val coachMark by lazy {
        context?.let {
            CoachMark2(it)
        }
    }
    private var currentOffset: Int = 0
    private var loaderDialogProductSubmitSse: LoaderDialog? = null
    private var dialogProductSubmissionSseBinding: StfsDialogProductSubmissionSseBinding? = null
    private val flashSaleAdapter by lazy {
        CompositeAdapter.Builder()
            .add(FlashSaleManageProductListItemDelegate(this))
            .add(FlashSaleManageProductListItemShimmeringDelegate())
            .add(FlashSaleManageProductListItemGlobalErrorDelegate(this))
            .add(LoadingDelegateAdapter())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        init()
        configHeaderUnify()
        configRecyclerView()
        getCampaignDetailBottomSheetData()
        getReservedProductList()
        observeUiState()
        observeUiEffect()
    }

    private fun init() {
        context?.let {
            loaderDialogProductSubmitSse = LoaderDialog(it).apply {
                dialogProductSubmissionSseBinding =
                    StfsDialogProductSubmissionSseBinding.inflate(LayoutInflater.from(it))
                customView = dialogProductSubmissionSseBinding?.root
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getFlashSaleSubmissionProgress(campaignId)
    }

    private fun getFlashSaleSubmissionProgress(campaignId: String) {
        viewModel.getFlashSaleSubmissionProgress(campaignId.toLongOrZero())
    }

    private fun configRecyclerView() {
        val pagingConfig = HasPaginatedList.Config(
            pageSize = 10,
            onLoadNextPage = {
                flashSaleAdapter.addItem(LoadingItem)
            }, onLoadNextPageFinished = {
                flashSaleAdapter.removeItem(LoadingItem)
            })
        rvProductList?.apply {
            attachOnScrollListener({
                coachMark?.dismissCoachMark()
            }, {
                coachMark?.dismissCoachMark()
            })
            attachPaging(this, pagingConfig) { _, offset ->
                currentOffset = offset
                loadNextReservedProduct()
            }
        }
    }

    private fun loadNextReservedProduct() {
        viewModel.processEvent(
            FlashSaleManageProductListUiEvent.LoadNextReservedProduct(
                reservationId,
                currentOffset
            )
        )
    }

    private fun configTicker(totalProduct: Int) {
        ticker?.shouldShowWithAction(!totalProduct.isZero()) {
            ticker?.apply {
                tickerType = TYPE_ANNOUNCEMENT
                tickerShape = Ticker.SHAPE_LOOSE
                setHtmlDescription(
                    String.format(getString(R.string.stfs_manage_product_list_ticker_manage_product))
                )
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        redirectToManageProductPage()
                    }

                    override fun onDismiss() {
                    }

                })
            }
        }
    }

    private fun redirectToManageProductPage() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
    }

    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiEffect.collect {
                when (it) {
                    is FlashSaleManageProductListUiEffect.AddIconCampaignDetailBottomSheet -> {
                        addInformationIcon(it.campaignDetailBottomSheetData)
                    }
                    is FlashSaleManageProductListUiEffect.ShowCoachMarkOnFirstProductItem -> {
                        configCoachMarkForFirstProductItem()
                    }
                    is FlashSaleManageProductListUiEffect.ShowToasterSuccessDelete -> {
                        showToasterSuccess(
                            getString(R.string.stfs_manage_product_list_success_delete_product_message),
                            getString(R.string.stfs_manage_product_list_success_delete_product_cta)
                        )
                    }
                    is FlashSaleManageProductListUiEffect.ShowToasterErrorDelete -> {
                        showToasterError(
                            ErrorHandler.getErrorMessage(context, it.throwable),
                            ctaText = getString(R.string.stfs_manage_product_list_success_delete_product_cta)
                        )
                    }
                    is FlashSaleManageProductListUiEffect.CloseManageProductListPage -> {
                        redirectToChooseProductPage()
                    }
                    is FlashSaleManageProductListUiEffect.ShowErrorGetReservedProductList -> {
                        showErrorGetReservedProductList(it.throwable)
                    }
                    is FlashSaleManageProductListUiEffect.ShowSubmitButton -> {
                        showButtonSubmit()
                    }
                    is FlashSaleManageProductListUiEffect.ConfigSubmitButton -> {
                        configSubmitButton(it.isEnableSubmitButton)
                    }
                    is FlashSaleManageProductListUiEffect.OnProductSubmitted -> {
                        onProductSubmitted(it.result)
                    }
                    is FlashSaleManageProductListUiEffect.ShowErrorLoadNextReservedProductList -> {
                        onErrorLoadNextReservedProductList(it.throwable)
                    }
                    is FlashSaleManageProductListUiEffect.ShowErrorSubmitDiscountedProduct -> {
                        onErrorSubmitDiscountedProduct(it.throwable)
                    }
                    is FlashSaleManageProductListUiEffect.ClearProductList -> {
                        clearProductList()
                    }
                    is FlashSaleManageProductListUiEffect.OnProductSseSubmission -> {
                        showDialogProductSseSubmission(it.flashSaleProductSubmissionSseResult)
                    }
                    is FlashSaleManageProductListUiEffect.OnSuccessAcknowledgeProductSubmissionSse -> {
                        redirectToCampaignDetailPage(it.totalSubmittedProduct.toLong())
                    }
                    is FlashSaleManageProductListUiEffect.OnSseOpen -> {
                        listenToExistingSse()
                    }
                }
            }
        }
    }

    private fun listenToExistingSse() {
        viewModel.listenToExistingSse(campaignId)
    }

    private fun showDialogProductSseSubmission(
        flashSaleProductSubmissionSseResult: FlashSaleProductSubmissionSseResult
    ) {
        val currentProcessedProduct = flashSaleProductSubmissionSseResult.countProcessedProduct
        val totalProduct = flashSaleProductSubmissionSseResult.countAllProduct
        updateProductSubmissionProgressDialog(currentProcessedProduct, totalProduct)
        showProductSubmissionSseProgressDialog()
        when (flashSaleProductSubmissionSseResult.status) {
            COMPLETE -> {
                acknowledgeProductSubmissionSse(
                    flashSaleProductSubmissionSseResult.campaignId,
                    flashSaleProductSubmissionSseResult.countProcessedProduct
                )
            }
            else -> {}
        }
    }

    private fun showProductSubmissionSseProgressDialog() {
        val isDialogShown = loaderDialogProductSubmitSse?.dialog?.isShowing
        if (isDialogShown == false) {
            loaderDialogProductSubmitSse?.dialog?.setCancelable(false)
            loaderDialogProductSubmitSse?.show()
        }
    }

    private fun updateProductSubmissionProgressDialog(
        currentProcessedProduct: Int,
        totalProduct: Int
    ) {
        dialogProductSubmissionSseBinding?.typographyDescription?.text = String.format(
            dialogProductSubmissionSseBinding?.typographyDescription?.text.toString(),
            currentProcessedProduct,
            totalProduct
        )
        loaderDialogProductSubmitSse?.setLoadingText("")
    }

    private fun acknowledgeProductSubmissionSse(campaignId: String, totalSubmittedProduct: Int) {
        viewModel.acknowledgeProductSubmissionSse(campaignId, totalSubmittedProduct)
    }

    private fun clearProductList() {
        flashSaleAdapter.removeItem(flashSaleAdapter.getItems())
    }

    private fun onErrorSubmitDiscountedProduct(throwable: Throwable) {
        buttonSubmit?.isLoading = false
        showToasterError(
            ErrorHandler.getErrorMessage(context, throwable),
            ctaText = getString(R.string.stfs_manage_product_list_success_delete_product_cta)
        )
    }

    private fun onErrorLoadNextReservedProductList(throwable: Throwable) {
        flashSaleAdapter.removeItem(LoadingItem)
        showToasterError(
            ErrorHandler.getErrorMessage(context, throwable),
            ctaText = getString(R.string.stfs_manage_product_list_retry_cta),
            toasterLength = Toaster.LENGTH_INDEFINITE,
            onClickCta = {
                flashSaleAdapter.addItem(LoadingItem)
                loadNextReservedProduct()
            }
        )
    }

    private fun redirectToChooseProductPage() {
        ChooseProductActivity.start(context, campaignId.toLongOrZero(), tabName)
        finishPage()
    }

    private fun onProductSubmitted(productSubmissionResult: ProductSubmissionResult) {
        buttonSubmit?.isLoading = false
        if (productSubmissionResult.isSuccess) {
            redirectToCampaignDetailPage(productSubmissionResult.totalSubmittedProduct)
        } else {
            showToasterError(
                productSubmissionResult.errorMessage,
                ctaText = getString(R.string.stfs_manage_product_list_success_delete_product_cta)
            )
        }
    }

    private fun redirectToCampaignDetailPage(totalSubmittedProduct: Long) {
        context?.let {
            CampaignDetailActivity.start(
                it,
                campaignId.toLongOrZero(),
                totalSubmittedProduct
            )
            finishPage()
        }
    }

    private fun configSubmitButton(isEnableSubmitButton: Boolean) {
        buttonSubmit?.isEnabled = isEnableSubmitButton
    }

    override fun showButtonSubmit() {
        super.showButtonSubmit()
        buttonSubmit?.apply {
            text = getString(R.string.stfs_manage_product_list_submit_button_text)
            isEnabled = false
        }
    }

    private fun showErrorGetReservedProductList(throwable: Throwable) {
        flashSaleAdapter.apply {
            removeItem(getItems())
            addItem(FlashSaleManageProductListGlobalErrorItem(throwable))
        }
    }

    private fun finishPage() {
        activity?.finish()
    }

    private fun showToasterSuccess(toasterMessage: String, ctaText: String = "") {
        showToaster(
            toasterMessage,
            Toaster.TYPE_NORMAL,
            ctaText = ctaText
        )
    }

    private fun showToasterError(
        toasterMessage: String,
        toasterLength: Int = Toaster.LENGTH_LONG,
        ctaText: String = "",
        onClickCta: View.OnClickListener = View.OnClickListener {}
    ) {
        showToaster(
            toasterMessage,
            Toaster.TYPE_ERROR,
            toasterLength,
            ctaText,
            onClickCta
        )
    }

    private fun showToaster(
        toasterMessage: String,
        toasterType: Int,
        toasterLength: Int = Toaster.LENGTH_LONG,
        ctaText: String = "",
        onClickCta: View.OnClickListener = View.OnClickListener {}
    ) {
        view?.let {
            Toaster.build(
                it,
                toasterMessage,
                toasterLength,
                toasterType,
                ctaText,
                onClickCta
            ).show()
        }
    }

    private fun configCoachMarkForFirstProductItem() {
        rvProductList?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                rvProductList?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                showCoachMarkOnFirstProductItem()
                setSharedPrefCoachMarkAlreadyShown()
            }
        })
    }

    private fun setSharedPrefCoachMarkAlreadyShown() {
        viewModel.setSharedPrefCoachMarkAlreadyShown()
    }

    private fun showCoachMarkOnFirstProductItem() {
        getFirstProductItemButtonView()?.let {
            val coachMarkItem = ArrayList<CoachMark2Item>()
            coachMarkItem.add(
                CoachMark2Item(
                    it,
                    getString(R.string.stfs_manage_product_list_coach_mark_title),
                    getString(R.string.stfs_manage_product_list_coach_mark_description)
                )
            )
            coachMark?.showCoachMark(coachMarkItem)
        }
    }

    private fun getFirstProductItemButtonView(): View? {
        return adapter?.getItems()?.indexOfFirst {
            it is FlashSaleManageProductListItem
        }?.let { position ->
            (rvProductList?.findViewHolderForAdapterPosition(position) as FlashSaleManageProductListItemViewHolder).buttonManage
        }
    }

    private fun showTotalProduct(totalProduct: Int) {
        textTotalProduct?.shouldShowWithAction(!totalProduct.isZero()) {
            textTotalProduct?.text =
                getString(R.string.stfs_manage_product_list_total_product, totalProduct.toString())
        }
    }

    private fun configHeaderUnify() {
        headerUnify?.apply {
            title = getString(R.string.stfs_manage_product_list_title)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                hideGlobalErrorState()
                setLoadingShimmeringData(it.isLoading)
                if (!it.isLoading) {
                    configTicker(it.totalProduct)
                    showTotalProduct(it.totalProduct)
                    updateDelegateData(it.listDelegateItem)
                    checkShouldEnableButtonSubmit()
                }
                notifyLoadResult(
                    flashSaleAdapter.getItems()
                        .filterIsInstance<FlashSaleManageProductListItem>().size != it.totalProduct
                )
            }
        }
    }

    private fun hideGlobalErrorState() {
        val globalErrorItem = flashSaleAdapter.getItems()
            .filterIsInstance<FlashSaleManageProductListGlobalErrorItem>()
        flashSaleAdapter.removeItem(globalErrorItem)
    }

    private fun checkShouldEnableButtonSubmit() {
        viewModel.processEvent(FlashSaleManageProductListUiEvent.CheckShouldEnableButtonSubmit)
    }

    private fun addInformationIcon(campaignDetailBottomSheetModel: CampaignDetailBottomSheetModel?) {
        campaignDetailBottomSheetModel?.let { model ->
            headerUnify?.apply {
                val infoIcon = IconUnify(activity ?: return, IconUnify.INFORMATION)
                addCustomRightContent(infoIcon)
                infoIcon.setOnClickListener {
                    showBottomSheetCampaignDetail(model)
                }
            }
        }
    }

    private fun showBottomSheetCampaignDetail(model: CampaignDetailBottomSheetModel) {
        val activity = activity ?: return
        CampaignDetailBottomSheet.newInstance(model).show(activity.supportFragmentManager, "")
    }

    private fun setLoadingShimmeringData(isLoadingProductList: Boolean) {
        flashSaleAdapter.apply {
            if (isLoadingProductList)
                addItem(FlashSaleManageProductListShimmeringItem)
            else
                removeItem(FlashSaleManageProductListShimmeringItem)
        }
    }

    private fun updateDelegateData(listDelegateItem: List<DelegateAdapterItem>) {
        flashSaleAdapter.submit(listDelegateItem)
    }

    private fun getCampaignDetailBottomSheetData() {
        viewModel.processEvent(
            FlashSaleManageProductListUiEvent.GetCampaignDetailBottomSheet(campaignId)
        )
    }

    private fun getReservedProductList() {
        viewModel.processEvent(
            FlashSaleManageProductListUiEvent.GetReservedProductList(
                reservationId,
                Int.ZERO
            )
        )
    }

    override fun onBackArrowClicked() {
        showClickBackDialog()
    }

    override fun onFragmentBackPressed(): Boolean {
        showClickBackDialog()
        return true
    }

    private fun showClickBackDialog() {
        context?.let {
            ConfirmationDialog(it).apply {
                setOnPositiveConfirmed {
                    redirectToChooseProductPage()
                }
                show(
                    getString(R.string.stfs_manage_product_list_click_back_dialog_title),
                    getString(R.string.stfs_manage_product_list_click_back_dialog_description),
                    getString(R.string.stfs_manage_product_list_click_back_dialog_ok_cta)
                )
            }
        }
    }

    override fun createAdapterInstance(): CompositeAdapter {
        return flashSaleAdapter
    }

    override fun onSubmitButtonClicked() {
        sendClickApplyProductDiscountTracker()
        submitDiscountedProduct()
    }

    private fun sendClickApplyProductDiscountTracker() {
        tracker.sendClickApplyManageDiscountEvent(campaignId)
    }

    private fun submitDiscountedProduct() {
        buttonSubmit?.isLoading = true
        viewModel.processEvent(
            FlashSaleManageProductListUiEvent.SubmitDiscountedProduct(
                reservationId,
                campaignId
            )
        )
    }

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onManageProductButtonClicked(productData: ReservedProduct.Product) {
        sendClickManageProductDiscountTracker(productData)
        redirectToManageProductDetailPage(productData)
    }

    private fun sendClickManageProductDiscountTracker(productData: ReservedProduct.Product) {
        tracker.sendClickManageProductDiscountEvent(
            campaignId,
            productData.productId.toString()
        )
    }

    private fun redirectToManageProductDetailPage(productData: ReservedProduct.Product) {
        if (productData.isParentProduct) {
            redirectToManageProductVariantPage(productData)
        } else {
            redirectToManageProductNonVariantPage(productData, campaignId)
        }
    }

    private fun redirectToManageProductVariantPage(productData: ReservedProduct.Product) {
        context?.let {
            ManageProductVariantActivity.createIntent(it, productData, campaignId).apply {
                startActivityForResult(
                    this,
                    REQUEST_CODE_MANAGE_PRODUCT_VARIANT
                )
            }
        }
    }

    private fun redirectToManageProductNonVariantPage(
        productData: ReservedProduct.Product,
        campaignId: String
    ) {
        context?.let {
            ManageProductNonVariantActivity.createIntent(it, productData, campaignId.toLongOrZero())
                .apply {
                    startActivityForResult(
                        this,
                        REQUEST_CODE_MANAGE_PRODUCT_NON_VARIANT
                    )
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_MANAGE_PRODUCT_NON_VARIANT, REQUEST_CODE_MANAGE_PRODUCT_VARIANT -> {
                handleUpdatedProductResult(resultCode, data)
            }
            else -> {}
        }
    }

    private fun handleUpdatedProductResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.extras?.getParcelable<ReservedProduct.Product>(
                BUNDLE_KEY_PRODUCT
            )?.let {
                updateProductData(it)
            }
        }
    }

    private fun updateProductData(productData: ReservedProduct.Product) {
        viewModel.processEvent(FlashSaleManageProductListUiEvent.UpdateProductData(productData))
    }

    override fun onDeleteProductButtonClicked(productData: ReservedProduct.Product) {
        showDeleteProductConfirmationDialog(productData)
    }

    private fun showDeleteProductConfirmationDialog(productData: ReservedProduct.Product) {
        context?.let {
            ConfirmationDialog(it).apply {
                setOnPositiveConfirmed {
                    deleteProductFromReservation(productData)
                }
                show(
                    getString(R.string.stfs_manage_product_list_delete_product_dialog_title),
                    getString(R.string.stfs_manage_product_list_delete_product_dialog_description),
                    getString(R.string.stfs_manage_product_list_delete_product_dialog_ok_cta)
                )
            }
        }
    }

    private fun deleteProductFromReservation(productData: ReservedProduct.Product) {
        viewModel.processEvent(
            FlashSaleManageProductListUiEvent.DeleteProductFromReserved(
                productData,
                reservationId,
                campaignId
            )
        )
    }

    override fun onGlobalErrorActionClickRetry() {
        getReservedProductList()
    }

}
