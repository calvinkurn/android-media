package com.tokopedia.shop.campaign.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapter
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.common.constant.ShopCommonExtraConstant
import com.tokopedia.shop.common.data.model.ShopPageWidgetLayoutUiModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.bottomsheet.ShopHomeFlashSaleTncBottomSheet
import com.tokopedia.shop.home.view.bottomsheet.ShopHomeNplCampaignTncBottomSheet
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.home.view.model.BannerType
import com.tokopedia.shop.home.view.model.CheckCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.GetCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.NotifyMeAction
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopPageHomeWidgetLayoutUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.shop.pageheader.presentation.fragment.InterfaceShopPageHeader
import com.tokopedia.shop.pageheader.presentation.fragment.NewShopPageFragment
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.shop_widget.thematicwidget.viewholder.ThematicWidgetViewHolder
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession

class ShopPageCampaignFragment : ShopPageHomeFragment() {

    companion object {
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val KEY_IS_OFFICIAL_STORE = "IS_OFFICIAL_STORE"
        private const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        private const val KEY_SHOP_NAME = "SHOP_NAME"
        private const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        private const val KEY_SHOP_REF = "SHOP_REF"
        private const val REQUEST_CODE_USER_LOGIN = 101
        private const val REGISTER_VALUE = "REGISTER"
        private const val UNREGISTER_VALUE = "UNREGISTER"
        private const val NPL_REMIND_ME_CAMPAIGN_ID = "NPL_REMIND_ME_CAMPAIGN_ID"
        private const val FLASH_SALE_REMIND_ME_CAMPAIGN_ID = "FLASH_SALE_REMIND_ME_CAMPAIGN_ID"
        private const val LOAD_WIDGET_ITEM_PER_PAGE = 3
        private const val LIST_WIDGET_LAYOUT_START_INDEX = 0
        private const val MIN_BUNDLE_SIZE = 1

        fun createInstance(
            shopId: String,
            isOfficialStore: Boolean,
            isGoldMerchant: Boolean,
            shopName: String,
            shopAttribution: String,
            shopRef: String
        ): ShopPageCampaignFragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            bundle.putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
            bundle.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            bundle.putString(KEY_SHOP_NAME, shopName)
            bundle.putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
            bundle.putString(KEY_SHOP_REF, shopRef)

            return ShopPageCampaignFragment().apply {
                arguments = bundle
            }
        }
    }

    private var isThematicWidgetShown: Boolean = false
    private var recyclerViewTopPadding = 0
    private var isClickToScrollToTop = false
    private var latestCompletelyVisibleItemIndex = -1
    private var listBackgroundColor: List<String> = listOf()
    private var textColor: String = ""
    private var topView: View? = null
    private var centerView: View? = null
    private var bottomView: View? = null
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private val shopCampaignTabAdapter: ShopCampaignTabAdapter
        get() = adapter as ShopCampaignTabAdapter

    private val shopCampaignTabAdapterTypeFactory by lazy {
        val userSession = UserSession(context)
        val _shopId = arguments?.getString(KEY_SHOP_ID, "") ?: ""
        val _isMyShop = ShopUtil.isMyShop(shopId = _shopId, userSessionShopId = userSession.shopId.orEmpty())
        ShopCampaignTabAdapterTypeFactory(
            listener = this,
            onMerchantVoucherListWidgetListener = this,
            shopHomeEndlessProductListener = this,
            shopHomeCarouselProductListener = this,
            shopProductEtalaseListViewHolderListener = this,
            shopHomeCampaignNplWidgetListener = this,
            shopHomeFlashSaleWidgetListener = this,
            shopProductChangeGridSectionListener = this,
            playWidgetCoordinator = playWidgetCoordinator,
            isShowTripleDot = !_isMyShop,
            shopHomeShowcaseListWidgetListener = this,
            shopHomePlayWidgetListener = this,
            shopHomeCardDonationListener = this,
            multipleProductBundleListener = this,
            singleProductBundleListener = this,
            thematicWidgetListener = thematicWidgetProductClickListenerImpl(),
            shopHomeProductListSellerEmptyListener = this
        )
    }

    private var globalErrorShopPage: GlobalError? = null

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun loadInitialData() {
        shopCampaignTabAdapter.showLoading()
        getRecyclerView(view)?.visible()
        recyclerViewTopPadding = getRecyclerView(view)?.paddingTop ?: 0
        globalErrorShopPage?.hide()
        shopCampaignTabAdapter.isOwner = isOwner
        shopPageHomeLayoutUiModel?.let {
            setShopHomeWidgetLayoutData(it)
        }
        setWidgetLayoutPlaceholder()
    }

    private fun setWidgetLayoutPlaceholder() {
        if (listWidgetLayout.isNotEmpty()) {
            shopCampaignTabAdapter.hideLoading()
            val shopHomeWidgetContentData =
                ShopPageHomeMapper.mapShopHomeWidgetLayoutToListShopHomeWidget(
                    listWidgetLayout,
                    isOwner,
                    isLogin,
                    isThematicWidgetShown
                )
            shopCampaignTabAdapter.setCampaignLayoutData(shopHomeWidgetContentData)
        }
    }

    override fun initView() {
        super.initView()
        topView = viewBinding?.topView
        centerView = viewBinding?.centerView
        bottomView = viewBinding?.bottomView

    }

    override fun onSuccessGetShopHomeWidgetContentData(mapWidgetContentData: Map<Pair<String, String>, Visitable<*>?>) {
        super.onSuccessGetShopHomeWidgetContentData(mapWidgetContentData)
        setCampaignTabBackgroundGradient()
    }

    override fun observeShopProductFilterParameterSharedViewModel() {}

    override fun observeShopChangeProductGridSharedViewModel() {}

    // mvc widget listener region
    override fun onVoucherImpression(model: ShopHomeVoucherUiModel, position: Int) {
        // TODO: 12/07/22 Implement mvc voucher tracker
    }

    override fun onVoucherReloaded() {
        getMvcWidgetData()
    }

    override fun onVoucherTokoMemberInformationImpression(model: ShopHomeVoucherUiModel, position: Int) {
        // TODO: 12/07/22 Implement mvc voucher tracker
    }
    // mvc widget listener end region

    // region Product bundle multiple widget
    override fun addMultipleBundleToCart(
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleListSize: Int,
        productDetails: List<ShopHomeBundleProductUiModel>,
        bundleName: String,
        widgetLayout: ShopHomeWidgetLayout
    ) {
        if (isOwner) {
            // disable owner add their own bundle to cart
            showErrorToast(getString(R.string.shop_page_product_bundle_failed_atc_text_for_shop_owner))
        } else {
            if (selectedMultipleBundle.isProductsHaveVariant) {
                // go to bundling selection page
                goToBundlingSelectionPage(selectedMultipleBundle.bundleId)
            } else {
                // atc bundle directly from shop page home
                val widgetLayoutParams = ShopPageWidgetLayoutUiModel(
                    widgetId = widgetLayout.widgetId,
                    widgetMasterId = widgetLayout.widgetMasterId,
                    widgetType = widgetLayout.widgetType,
                    widgetName = widgetLayout.widgetName
                )
                viewModel?.addBundleToCart(
                    shopId = shopId,
                    userId = userId,
                    bundleId = selectedMultipleBundle.bundleId,
                    productDetails = productDetails,
                    onFinishAddToCart = { handleOnFinishAtcBundle(it, bundleListSize, widgetLayoutParams) },
                    onErrorAddBundleToCart = { handleOnErrorAtcBundle(it) },
                    productQuantity = selectedMultipleBundle.minOrder
                )
            }
        }
        // TODO: 12/07/22 Implement atc tracker bundle multiple

    }

    override fun impressionProductBundleMultiple(
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        // TODO: 12/07/22 Implement multiple bundle impression tracker
    }

    override fun onMultipleBundleProductClicked(
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        // TODO: 12/07/22 Implement multiple bundle tracker

        goToPDP(selectedProduct.productId)
    }
    // endregion


    // region Product bundle single widget
    override fun onSingleBundleProductClicked(
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        // TODO: 12/07/22 Implement single bundle product clicked tracker

        goToPDP(selectedProduct.productId)
    }

    override fun addSingleBundleToCart(
        selectedBundle: ShopHomeProductBundleDetailUiModel,
        bundleListSize: Int,
        bundleProducts: ShopHomeBundleProductUiModel,
        bundleName: String,
        widgetLayout: ShopHomeWidgetLayout
    ) {
        if (isOwner) {
            // disable owner add their own bundle to cart
            showErrorToast(getString(R.string.shop_page_product_bundle_failed_atc_text_for_shop_owner))
        } else {
            if (selectedBundle.isProductsHaveVariant) {
                // go to bundling selection page
                goToBundlingSelectionPage(selectedBundle.bundleId)
            } else {
                // atc bundle directly from shop page home
                val widgetLayoutParams = ShopPageWidgetLayoutUiModel(
                    widgetId = widgetLayout.widgetId,
                    widgetMasterId = widgetLayout.widgetMasterId,
                    widgetType = widgetLayout.widgetType,
                    widgetName = widgetLayout.widgetName
                )
                viewModel?.addBundleToCart(
                    shopId = shopId,
                    userId = userId,
                    bundleId = selectedBundle.bundleId,
                    productDetails = listOf(bundleProducts),
                    onFinishAddToCart = { handleOnFinishAtcBundle(it, bundleListSize, widgetLayoutParams) },
                    onErrorAddBundleToCart = { handleOnErrorAtcBundle(it) },
                    productQuantity = selectedBundle.minOrder
                )
            }
        }

        // TODO: 12/07/22 Implement atc bundle single tracker
    }

    override fun onTrackSingleVariantChange(selectedProduct: ShopHomeBundleProductUiModel, selectedSingleBundle: ShopHomeProductBundleDetailUiModel, bundleName: String) {
        // TODO: 12/07/22 Implement variant change single bundle tracker
    }

    override fun impressionProductBundleSingle(
        selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
        selectedProduct: ShopHomeBundleProductUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        // TODO: 12/07/22 Implement impression product bundle single
    }
    // endregion

    private fun setCampaignTabBackgroundGradient() {
        if (listBackgroundColor.isNotEmpty()) {
            topView?.show()
            centerView?.show()
            bottomView?.show()
            val colors = IntArray(listBackgroundColor.size)
            for (i in listBackgroundColor.indices) {
                colors[i] = parseColor(listBackgroundColor.getOrNull(i).orEmpty())
            }
            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            gradient.cornerRadius = 0f
            topView?.setBackgroundColor(Color.parseColor(listBackgroundColor.firstOrNull()))
            centerView?.background = gradient
            bottomView?.setBackgroundColor(Color.parseColor(listBackgroundColor.lastOrNull()))
        } else {
            topView?.hide()
            centerView?.hide()
            bottomView?.hide()
        }
    }

    private fun parseColor(colorHex: String): Int {
        return try {
            Color.parseColor(colorHex)
        } catch (e: Throwable) {
            0
        }
    }

    private fun onFailCheckCampaignNplNotifyMe(campaignId: String, errorMessage: String) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_string_ok)
            ).show()
        }
        shopCampaignTabAdapter.updateRemindMeStatusCampaignNplWidgetData(campaignId)
    }

    private fun onSuccessCheckCampaignNplNotifyMe(data: CheckCampaignNotifyMeUiModel) {
        val isRegisterCampaign =
            data.action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()
        shopCampaignTabAdapter.updateRemindMeStatusCampaignNplWidgetData(
            data.campaignId,
            isRegisterCampaign,
            true
        )
        if (shopCampaignTabAdapter.isCampaignFollower(data.campaignId)) {
            shopPageHomeTracking.clickNotifyMeNplFollowerButton(
                isOwner,
                data.action,
                viewModel?.userId.orEmpty(),
                customDimensionShopPage
            )
        } else {
            shopPageHomeTracking.clickNotifyMeButton(
                isOwner,
                data.action,
                customDimensionShopPage
            )
        }
        view?.let {
            Toaster.build(it,
                data.message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.shop_string_ok)
            ) {
                shopPageHomeTracking.toasterActivationClickOk(isOwner, customDimensionShopPage)
            }.show()
            shopPageHomeTracking.impressionToasterActivation(isOwner, customDimensionShopPage)
        }
    }

    private fun onSuccessGetCampaignNplRemindMeStatusData(data: GetCampaignNotifyMeUiModel) {
        shopCampaignTabAdapter.updateRemindMeStatusCampaignNplWidgetData(data.campaignId, data.isAvailable)
        if (getNplRemindMeClickedCampaignId() == data.campaignId && !data.isAvailable) {
            val nplCampaignModel = shopCampaignTabAdapter.getNplCampaignUiModel(data.campaignId)
            nplCampaignModel?.let {
                shopCampaignTabAdapter.showNplRemindMeLoading(data.campaignId)
                handleClickRemindMe(it)
                setNplRemindMeClickedCampaignId("")
            }
        }
    }

    private fun onFailCheckCampaignFlashSaleNotifyMe(campaignId: String, errorMessage: String) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_page_label_oke)
            ).show()
        }
        shopCampaignTabAdapter.updateRemindMeStatusCampaignFlashSaleWidgetData(campaignId)
    }

    private fun onSuccessCheckCampaignFlashSaleNotifyMe(data: CheckCampaignNotifyMeUiModel) {
        val isRegisterCampaign =
            data.action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()
        shopCampaignTabAdapter.updateRemindMeStatusCampaignFlashSaleWidgetData(
            data.campaignId,
            isRegisterCampaign,
            true
        )
        view?.let {
            Toaster.build(
                it,
                data.message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.shop_page_label_oke)
            ).show()
        }
    }

    private fun onSuccessGetCampaignFlashSaleRemindMeStatusData(data: GetCampaignNotifyMeUiModel) {
        shopCampaignTabAdapter.updateRemindMeStatusCampaignFlashSaleWidgetData(
            data.campaignId,
            data.isAvailable
        )
        if (getFlashSaleRemindMeClickedCampaignId() == data.campaignId && !data.isAvailable) {
            val flashSaleCampaignModel =
                shopCampaignTabAdapter.getFlashSaleCampaignUiModel(data.campaignId)
            flashSaleCampaignModel?.let {
                handleFlashSaleClickReminder(it)
                setFlashSaleRemindMeClickedCampaignId("")
            }
        }
    }

    private fun onErrorGetShopHomeLayoutData(throwable: Throwable) {
        if (throwable is MessageErrorException) {
            globalErrorShopPage?.setType(GlobalError.SERVER_ERROR)
        } else {
            globalErrorShopPage?.setType(GlobalError.NO_CONNECTION)
        }
        globalErrorShopPage?.visible()
        getRecyclerView(view)?.hide()

        globalErrorShopPage?.setOnClickListener {
            loadInitialData()
        }
    }

    override fun setShopHomeWidgetLayoutData(data: ShopPageHomeWidgetLayoutUiModel) {
        listWidgetLayout = data.listWidgetLayout.toMutableList()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AdapterTypeFactory> {
        return ShopCampaignTabAdapter(shopCampaignTabAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): AdapterTypeFactory {
        return shopCampaignTabAdapterTypeFactory
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopPageCampaignFragment)
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(staggeredGridLayoutManager, shopCampaignTabAdapter) {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                val layoutManager =
                    (getRecyclerView(view)?.layoutManager as? StaggeredGridLayoutManager)
                val firstCompletelyVisibleItemPosition =
                    layoutManager?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0)
                        .orZero()
                if (firstCompletelyVisibleItemPosition == 0 && isClickToScrollToTop) {
                    isClickToScrollToTop = false
                    (parentFragment as? NewShopPageFragment)?.expandHeader()
                }
                if (firstCompletelyVisibleItemPosition != latestCompletelyVisibleItemIndex)
                    hideScrollToTopButton()
                latestCompletelyVisibleItemIndex = firstCompletelyVisibleItemPosition
                val lastCompletelyVisibleItemPosition =
                    layoutManager?.findLastCompletelyVisibleItemPositions(
                        null
                    )?.getOrNull(0).orZero()
                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPositions(
                    null
                )?.getOrNull(0).orZero()
                checkLoadNextShopCampaignWidgetContentData(
                    lastCompletelyVisibleItemPosition,
                    firstVisibleItemPosition
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
                super.onScrollStateChanged(recyclerView, state)
                if (state == SCROLL_STATE_IDLE) {
                    val firstCompletelyVisibleItemPosition =
                        (layoutManager as? StaggeredGridLayoutManager)?.findFirstCompletelyVisibleItemPositions(
                            null
                        )?.getOrNull(0).orZero()
                    if (firstCompletelyVisibleItemPosition > 0) {
                        showScrollToTopButton()
                    }
                }
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int) {}
        }
    }

    private fun checkLoadNextShopCampaignWidgetContentData(
        lastVisibleItemPosition: Int,
        firstVisibleItemPosition: Int
    ) {
        val shouldLoadLastVisibleItem =
            shopCampaignTabAdapter.isLoadNextHomeWidgetData(lastVisibleItemPosition)
        val shouldLoadFirstVisibleItem =
            shopCampaignTabAdapter.isLoadNextHomeWidgetData(firstVisibleItemPosition)
        if (shouldLoadLastVisibleItem || shouldLoadFirstVisibleItem) {
            val position = if (shouldLoadLastVisibleItem)
                lastVisibleItemPosition
            else
                firstVisibleItemPosition
            val listWidgetLayoutToLoad = getListWidgetLayoutToLoad(position)
            shopCampaignTabAdapter.updateShopHomeWidgetStateToLoading(listWidgetLayoutToLoad)

            val widgetMvcLayout = listWidgetLayoutToLoad.firstOrNull { isWidgetMvc(it) }?.apply {
                listWidgetLayoutToLoad.remove(this)
            }
            getWidgetContentData(listWidgetLayoutToLoad)

            // get mvc widget content data
            widgetMvcLayout?.let {
                getMvcWidgetData()
            }
            listWidgetLayoutToLoad.clear()
        }
    }

    private fun getWidgetContentData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetLayoutUiModel>) {
        if (listWidgetLayoutToLoad.isNotEmpty()) {
            val widgetUserAddressLocalData = ShopUtil.getShopPageWidgetUserAddressLocalData(context)
                ?: LocalCacheModel()
            viewModel?.getWidgetContentData(
                listWidgetLayoutToLoad.toList(),
                shopId,
                widgetUserAddressLocalData,
                isThematicWidgetShown
            )
        }
    }

    private fun getListWidgetLayoutToLoad(lastCompletelyVisibleItemPosition: Int): MutableList<ShopPageWidgetLayoutUiModel> {
        return if (listWidgetLayout.isNotEmpty()) {
            if (shopCampaignTabAdapter.isLoadFirstWidgetContentData()) {
                listWidgetLayout.subList(
                    LIST_WIDGET_LAYOUT_START_INDEX,
                    ShopUtil.getActualPositionFromIndex(lastCompletelyVisibleItemPosition)
                )
            } else {
                val toIndex = LOAD_WIDGET_ITEM_PER_PAGE.takeIf { it <= listWidgetLayout.size }
                    ?: listWidgetLayout.size
                listWidgetLayout.subList(LIST_WIDGET_LAYOUT_START_INDEX, toIndex)
            }
        } else {
            mutableListOf()
        }
    }

    override fun loadData(page: Int) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_USER_LOGIN -> {
                if (resultCode == Activity.RESULT_OK)
                    (parentFragment as? InterfaceShopPageHeader)?.refreshData()
            }
            else -> {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleOnFinishAtcBundle(
            atcBundleModel: AddToCartBundleModel,
            bundleListSize: Int,
            widgetLayout: ShopPageWidgetLayoutUiModel
    ) {
        atcBundleModel.validateResponse(
                onSuccess = {
                    showToastSuccess(
                            getString(R.string.shop_page_product_bundle_success_atc_text),
                            getString(R.string.see_label)
                    ) {
                        goToCart()
                    }
                },
                onFailedWithMessages = {
                    val errorDialogCtaText: String
                    val errorMessageDescription = if (bundleListSize > MIN_BUNDLE_SIZE) {
                        errorDialogCtaText = getString(R.string.shop_page_product_bundle_failed_oos_cta_text_with_alt)
                        getString(R.string.shop_page_product_bundle_failed_oos_dialog_desc_with_alt)
                    } else {
                        errorDialogCtaText = getString(R.string.shop_page_product_bundle_failed_oos_cta_text)
                        getString(R.string.shop_page_product_bundle_failed_oos_dialog_desc_no_alt)
                    }
                    showErrorDialogAtcBundle(
                            errorTitle = getString(R.string.shop_page_product_bundle_failed_oos_dialog_title),
                            errorDescription = errorMessageDescription,
                            ctaText = errorDialogCtaText,
                            widgetLayoutParams = widgetLayout
                    )
                },
                onFailedWithException = {
                    showErrorToast(it.message.orEmpty())
                }
        )
    }

    private fun handleOnErrorAtcBundle(throwable: Throwable) {
        showErrorToast(throwable.message.orEmpty())
    }

    private fun goToBundlingSelectionPage(bundleId: String) {
        val bundlingSelectionPageAppLink = UriUtil.buildUri(
                ApplinkConstInternalMechant.MERCHANT_PRODUCT_BUNDLE,
                ShopHomeProductBundleItemUiModel.DEFAULT_BUNDLE_PRODUCT_PARENT_ID
        )
        val bundleAppLinkWithParams = Uri.parse(bundlingSelectionPageAppLink).buildUpon()
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_BUNDLE_ID, bundleId)
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_PAGE_SOURCE, ApplinkConstInternalMechant.SOURCE_SHOP_PAGE)
                .build()
                .toString()
        context?.let {
            val bspIntent = RouteManager.getIntent(it, bundleAppLinkWithParams)
            startActivity(bspIntent)
        }
    }

    private fun showErrorDialogAtcBundle(
            errorTitle: String,
            errorDescription: String,
            ctaText: String,
            widgetLayoutParams: ShopPageWidgetLayoutUiModel
    ) {
        context?.let {
            DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(errorTitle)
                setDescription(errorDescription)
                setPrimaryCTAText(ctaText)
                setPrimaryCTAClickListener {
                    dismiss()
                    getWidgetContentData(mutableListOf(widgetLayoutParams))
                }
            }.show()
        }
    }

    private fun sendShopHomeWidgetImpressionTracker(
        segmentName: String,
        widgetName: String,
        widgetId: String,
        position: Int
    ) {
        if (!isOwner) {
            shopPageHomeTracking.onImpressionShopHomeWidget(
                segmentName,
                widgetName,
                widgetId,
                position,
                shopId,
                userId
            )
        }
    }

    private fun sendShopHomeWidgetClickedTracker(
        segmentName: String,
        widgetName: String,
        widgetId: String,
        position: Int
    ) {
        if (!isOwner) {
            shopPageHomeTracking.onClickedShopHomeWidget(
                segmentName,
                widgetName,
                widgetId,
                position,
                shopId,
                userId
            )
        }
    }

    override fun onCtaClicked(shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel?) {
        context?.let {
            RouteManager.route(it, shopHomeCarouselProductUiModel?.header?.ctaLink)
        }
    }

    private fun goToCart() {
        RouteManager.route(context, ApplinkConst.CART)
    }

    private fun goToPDP(productId: String) {
        context?.let {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            startActivity(intent)
        }
    }

    private fun redirectToLoginPage(requestCode: Int = REQUEST_CODE_USER_LOGIN) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, requestCode)
        }
    }

    private fun showToastSuccess(
        message: String,
        ctaText: String = "",
        ctaAction: View.OnClickListener? = null
    ) {
        activity?.run {
            view?.let {
                ctaAction?.let { ctaClickListener ->
                    Toaster.build(
                        it,
                        message,
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        ctaText,
                        ctaClickListener
                    ).show()
                } ?: Toaster.build(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    ctaText
                ).show()
            }
        }
    }

    private fun showErrorToast(message: String) {
        activity?.run {
            view?.let { Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show() }
        }
    }

    override fun onCampaignCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        sendShopHomeWidgetClickedTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_CAMPAIGN,
            shopHomeNewProductLaunchCampaignUiModel.name,
            shopHomeNewProductLaunchCampaignUiModel.widgetId,
            ShopUtil.getActualPositionFromIndex(parentPosition)
        )
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let {
            shopPageHomeTracking.clickCampaignNplProduct(
                isOwner,
                it.statusCampaign,
                shopHomeProductViewModel?.name ?: "",
                shopHomeProductViewModel?.id ?: "",
                shopHomeProductViewModel?.displayedPrice ?: "",
                shopName,
                ShopUtil.getActualPositionFromIndex(parentPosition),
                itemPosition,
                isLogin,
                customDimensionShopPage
            )
        }
        shopHomeProductViewModel?.let {
            goToPDP(it.id ?: "")
        }
    }

    override fun onCampaignCarouselProductItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let {
            shopPageHomeTracking.impressionCampaignNplProduct(
                isOwner,
                it.statusCampaign,
                shopHomeProductViewModel?.name ?: "",
                shopHomeProductViewModel?.id ?: "",
                shopHomeProductViewModel?.displayedPrice ?: "",
                shopName,
                ShopUtil.getActualPositionFromIndex(parentPosition),
                itemPosition,
                isLogin,
                customDimensionShopPage
            )
        }
    }

    override fun onClickTncCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            shopPageHomeTracking.clickTncButton(isOwner, it.statusCampaign, customDimensionShopPage)
            showNplCampaignTncBottomSheet(
                it.campaignId,
                it.statusCampaign,
                it.dynamicRule.dynamicRoleData.ruleID
            )
        }
    }

    override fun onClickTncFlashSaleWidget(model: ShopHomeFlashSaleUiModel) {
        model.data?.firstOrNull()?.let {
            shopPageHomeTracking.onClickTnCButtonFlashSaleWidget(
                campaignId = it.campaignId,
                shopId = shopId,
                userId = userId,
                isOwner = isOwner
            )
            showFlashTncSaleBottomSheet(it.campaignId)
        }
    }

    override fun onClickSeeAllFlashSaleWidget(model: ShopHomeFlashSaleUiModel) {
        context?.run {
            if (shopId.isNotBlank() && model.header.ctaLink.isNotBlank()) {
                model.data?.firstOrNull()?.let { flashSaleItem ->
                    shopPageHomeTracking.onClickSeeAllButtonFlashSaleWidget(
                        statusCampaign = flashSaleItem.statusCampaign,
                        shopId = shopId,
                        userId = userId,
                        isOwner = isOwner
                    )
                }
                RouteManager.route(this, model.header.ctaLink)
            }
        }
    }

    private fun showNplCampaignTncBottomSheet(
        campaignId: String,
        statusCampaign: String,
        ruleID: String
    ) {
        val bottomSheet = ShopHomeNplCampaignTncBottomSheet.createInstance(
            campaignId,
            statusCampaign,
            shopId,
            isOfficialStore,
            isGoldMerchant,
            ruleID
        )
        bottomSheet.show(childFragmentManager, "")
    }

    private fun showFlashTncSaleBottomSheet(campaignId: String) {
        val bottomSheet = ShopHomeFlashSaleTncBottomSheet.createInstance(campaignId)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onClickRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        viewModel?.let {
            val campaignId = model.data?.firstOrNull()?.campaignId.orEmpty()
            if (it.isLogin) {
                shopCampaignTabAdapter.showNplRemindMeLoading(campaignId)
                handleClickRemindMe(model)
            } else {
                setNplRemindMeClickedCampaignId(campaignId)
                redirectToLoginPage()
            }
        }
    }

    override fun onClickFlashSaleReminder(model: ShopHomeFlashSaleUiModel) {
        viewModel?.let {
            val campaignId = model.data?.firstOrNull()?.campaignId.orEmpty()
            if (it.isLogin) {
                handleFlashSaleClickReminder(model)
            } else {
                setFlashSaleRemindMeClickedCampaignId(campaignId)
                redirectToLoginPage()
            }
            shopPageHomeTracking.onClickReminderButtonFlashSaleWidget(
                campaignId = campaignId,
                shopId = shopId,
                userId = userId,
                isOwner = isOwner
            )
        }
    }

    override fun onFlashSaleProductClicked(model: ShopHomeProductUiModel) {
        goToPDP(model.id ?: "")
    }

    override fun onPlaceHolderClickSeeAll(model: ShopHomeFlashSaleUiModel) {
        context?.run {
            if (shopId.isNotBlank() && model.header.ctaLink.isNotBlank()) {
                RouteManager.route(this, model.header.ctaLink)
            }
        }
    }

    private fun handleClickRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val isRemindMe = model.data?.firstOrNull()?.isRemindMe ?: false
        val action = if (isRemindMe) {
            UNREGISTER_VALUE
        } else {
            REGISTER_VALUE
        }
        val campaignId = model.data?.firstOrNull()?.campaignId ?: ""
        viewModel?.clickRemindMe(campaignId, action)
    }

    private fun handleFlashSaleClickReminder(model: ShopHomeFlashSaleUiModel) {
        val isRemindMe = model.data?.firstOrNull()?.isRemindMe ?: false
        val action = if (isRemindMe) {
            UNREGISTER_VALUE
        } else {
            REGISTER_VALUE
        }
        val campaignId = model.data?.firstOrNull()?.campaignId ?: ""
        viewModel?.clickFlashSaleReminder(campaignId, action)
    }

    override fun onClickCtaCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            shopPageHomeTracking.clickCtaCampaignNplWidget(
                isOwner,
                it.statusCampaign,
                customDimensionShopPage
            )
            context?.let { context ->
                // expected ctaLink produce ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST
                val showcaseIntent = RouteManager.getIntent(context, model.header.ctaLink).apply {
                    // set isNeedToReload data to true for sync shop info data in product result fragment
                    putExtra(ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, true)
                }
                startActivity(showcaseIntent)
            }
        }
    }

    override fun onClickCampaignBannerAreaNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            context?.let { context ->
                val appLink = model.header.ctaLink
                if (appLink.isNotEmpty()) {
                    RouteManager.route(context, appLink)
                }
            }
        }
    }

    override fun onImpressionCampaignNplWidget(
        position: Int,
        shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel
    ) {
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let {
            val statusCampaign = it.statusCampaign
            val selectedBannerType = when (statusCampaign.toLowerCase()) {
                StatusCampaign.UPCOMING.statusCampaign.toLowerCase() -> BannerType.UPCOMING.bannerType
                StatusCampaign.ONGOING.statusCampaign.toLowerCase() -> BannerType.LIVE.bannerType
                StatusCampaign.FINISHED.statusCampaign.toLowerCase() -> BannerType.FINISHED.bannerType
                else -> ""
            }
            val selectedBanner = it.bannerList.firstOrNull {
                it.bannerType.toLowerCase() == selectedBannerType.toLowerCase()
            }
            val isSeeCampaign =
                if (statusCampaign.toLowerCase() == StatusCampaign.UPCOMING.statusCampaign.toLowerCase()) {
                    it.totalNotifyWording.isNotEmpty()
                } else {
                    null
                }
            sendShopHomeWidgetImpressionTracker(
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_CAMPAIGN,
                shopHomeNewProductLaunchCampaignUiModel.name,
                shopHomeNewProductLaunchCampaignUiModel.widgetId,
                ShopUtil.getActualPositionFromIndex(position)
            )
            shopPageHomeTracking.impressionCampaignNplWidget(
                    it.statusCampaign,
                    shopId,
                    ShopUtil.getActualPositionFromIndex(position),
                    isSeeCampaign,
                    selectedBanner?.imageId.orEmpty(),
                    selectedBanner?.imageUrl ?: "",
                    customDimensionShopPage,
                    isOwner
            )
        }
    }

    override fun onFlashSaleWidgetImpressed(model: ShopHomeFlashSaleUiModel, position: Int) {
        model.data?.firstOrNull()?.let { itemFlashSale ->
            val campaignId = itemFlashSale.campaignId
            val statusCampaign = itemFlashSale.statusCampaign
            shopPageHomeTracking.impressionCampaignFlashSaleWidget(
                campaignId = campaignId,
                statusCampaign = statusCampaign,
                shopId = shopId,
                userId = userId,
                position = position,
                isOwner = isOwner
            )
        }
    }

    // npl widget
    override fun onTimerFinished(model: ShopHomeNewProductLaunchCampaignUiModel) {
        shopCampaignTabAdapter.removeWidget(model)
        endlessRecyclerViewScrollListener.resetState()
        shopCampaignTabAdapter.removeProductList()
        shopCampaignTabAdapter.showLoading()
        viewModel?.getShopPageHomeWidgetLayoutData(shopId, extParam)
        scrollToTop()
    }

    // flash sale widget
    override fun onTimerFinished(model: ShopHomeFlashSaleUiModel) {
        shopCampaignTabAdapter.removeWidget(model)
        endlessRecyclerViewScrollListener.resetState()
        shopCampaignTabAdapter.removeProductList()
        shopCampaignTabAdapter.showLoading()
        viewModel?.getShopPageHomeWidgetLayoutData(shopId, extParam)
        scrollToTop()
    }

    private fun setNplRemindMeClickedCampaignId(campaignId: String) {
        PersistentCacheManager.instance.put(NPL_REMIND_ME_CAMPAIGN_ID, campaignId)
    }

    private fun getNplRemindMeClickedCampaignId(): String {
        return PersistentCacheManager.instance.get(
            NPL_REMIND_ME_CAMPAIGN_ID,
            String::class.java,
            ""
        ).orEmpty()
    }

    private fun setFlashSaleRemindMeClickedCampaignId(campaignId: String) {
        PersistentCacheManager.instance.put(FLASH_SALE_REMIND_ME_CAMPAIGN_ID, campaignId)
    }

    private fun getFlashSaleRemindMeClickedCampaignId(): String {
        return PersistentCacheManager.instance.get(
            FLASH_SALE_REMIND_ME_CAMPAIGN_ID,
            String::class.java,
            ""
        ).orEmpty()
    }

    override fun scrollToTop() {
        isClickToScrollToTop = true
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun isShowScrollToTopButton(): Boolean {
        return latestCompletelyVisibleItemIndex > 0
    }

    private fun hideScrollToTopButton() {
        (parentFragment as? NewShopPageFragment)?.hideScrollToTopButton()
    }

    private fun showScrollToTopButton() {
        (parentFragment as? NewShopPageFragment)?.showScrollToTopButton()
    }

    private fun thematicWidgetProductClickListenerImpl(): ThematicWidgetViewHolder.ThematicWidgetListener = object : ThematicWidgetViewHolder.ThematicWidgetListener {

        override fun onThematicWidgetImpressListener(model: ThematicWidgetUiModel, position: Int) {
            shopPageHomeTracking.impressionThematicWidgetCampaign(
                campaignName = model.name,
                campaignId = model.campaignId,
                shopId = shopId,
                userId = userId,
                position = position
            )
        }

        override fun onProductCardThematicWidgetImpressListener(
            products: List<ProductCardUiModel>,
            position: Int,
            campaignId: String,
            campaignName: String
        ) {
            shopPageHomeTracking.impressionProductCardThematicWidgetCampaign(
                campaignName = campaignName,
                campaignId =campaignId,
                shopId = shopId,
                userId = userId,
                products = products,
            )
        }

        override fun onProductCardThematicWidgetClickListener(
            product: ProductCardUiModel,
            campaignId: String,
            campaignName: String,
            position: Int
        ) {
            shopPageHomeTracking.clickProductCardThematicWidgetCampaign(
                campaignName = campaignName,
                campaignId = campaignId,
                shopId = shopId,
                userId = userId,
                product = product,
                position = position
            )
            RouteManager.route(context, product.productUrl)
        }

        override fun onProductCardSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String) {
            shopPageHomeTracking.clickProductCardSeeAllThematicWidgetCampaign(
                campaignId = campaignId,
                campaignName = campaignName,
                shopId = shopId,
                userId = userId,
            )
            RouteManager.route(context, appLink)
        }

        override fun onSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String) {
            shopPageHomeTracking.clickSeeAllThematicWidgetCampaign(
                campaignId = campaignId,
                campaignName = campaignName,
                shopId = shopId,
                userId = userId,
            )
            RouteManager.route(context, appLink)
        }

        override fun onThematicWidgetTimerFinishListener(model: ThematicWidgetUiModel?) {
            model?.apply {
                shopCampaignTabAdapter.removeWidget(this)
            }
        }
    }

    fun setPageBackgroundColor(listBackgroundColor: List<String>) {
        this.listBackgroundColor = listBackgroundColor
    }

    fun setPageTextColor(textColor: String) {
        this.textColor = textColor
    }
}
