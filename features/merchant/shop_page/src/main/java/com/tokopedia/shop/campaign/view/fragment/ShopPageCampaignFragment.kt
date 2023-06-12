package com.tokopedia.shop.campaign.view.fragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.extension.stepScrollToPositionWithDelay
import com.tokopedia.play.widget.ui.model.ext.hasSuccessfulTranscodedChannel
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopCampaignTabTracker
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_MULTIPLE_BUNDLING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_SINGLE_BUNDLING
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.util.mapper.ShopPageCampaignMapper
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapter
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplaySliderBannerHighlightViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignProductBundleParentWidgetViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderItemViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderMoreItemViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.WidgetConfigListener
import com.tokopedia.shop.campaign.view.bottomsheet.ExclusiveLaunchVoucherListBottomSheet
import com.tokopedia.shop.campaign.view.bottomsheet.VoucherDetailBottomSheet
import com.tokopedia.shop.campaign.view.listener.ShopCampaignCarouselProductListener
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.campaign.view.viewmodel.ShopCampaignViewModel
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.extension.showToaster
import com.tokopedia.shop.common.extension.showToasterError
import com.tokopedia.shop.common.util.ShopPageExceptionHandler
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.databinding.FragmentShopPageCampaignBinding
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.CheckCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.NotifyMeAction
import com.tokopedia.shop.home.view.model.ShopHomeProductBundleListUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopPageLayoutUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.pageheader.presentation.fragment.InterfaceShopPageHeader
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragment
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

class ShopPageCampaignFragment :
    ShopPageHomeFragment(),
    WidgetConfigListener,
    ShopCampaignProductBundleParentWidgetViewHolder.Listener,
    ShopHomeListener,
    ShopCampaignInterface,
    ShopCampaignDisplaySliderBannerHighlightViewHolder.Listener,
    ShopCampaignCarouselProductListener,
    ShopCampaignVoucherSliderItemViewHolder.Listener,
    ShopCampaignVoucherSliderMoreItemViewHolder.Listener {

    companion object {
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val LOAD_WIDGET_ITEM_PER_PAGE = 3
        private const val LIST_WIDGET_LAYOUT_START_INDEX = 0
        private const val HOME_TAB_APP_LINK_LAST_SEGMENT = "home"

        fun createInstance(shopId: String): ShopPageCampaignFragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            return ShopPageCampaignFragment().apply {
                arguments = bundle
            }
        }
    }

    private val viewBindingCampaignTab: FragmentShopPageCampaignBinding? by viewBinding()
    private var campaignTabBackgroundColor: String = ""
    private var campaignTabBackgroundPatternImage: String = ""
    private var recyclerViewTopPadding = 0
    private var isClickToScrollToTop = false
    private var latestCompletelyVisibleItemIndex = -1
    private var textColor: String = ""
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var viewModelCampaign: ShopCampaignViewModel? = null

    private val shopCampaignTabAdapter: ShopCampaignTabAdapter
        get() = adapter as ShopCampaignTabAdapter
    private val shopCampaignTabAdapterTypeFactory by lazy {
        ShopCampaignTabAdapterTypeFactory(
            shopHomeDisplayWidgetListener = this,
            shopCampaignDisplayBannerTimerWidgetListener = this,
            shopCampaignCarouselProductListener = this,
            playWidgetCoordinator = playWidgetCoordinator,
            shopPlayWidgetListener = this,
            multipleProductBundleListener = this,
            singleProductBundleListener = this,
            bundlingParentListener = this,
            shopCampaignInterface = this,
            sliderBannerHighlightListener = this,
            shopCampaignVoucherSliderItemListener = this,
            shopCampaignVoucherSliderMoreItemListener = this
        )
    }

    private var globalErrorShopPage: GlobalError? = null

    @Inject
    lateinit var shopCampaignTabTracker: ShopCampaignTabTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelCampaign =
            ViewModelProviders.of(this, viewModelFactory).get(ShopCampaignViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_page_campaign, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVoucherSliderWidgetData()
        observeRedeemResult()
        observeCampaignWidgetListVisitable()
        observeLatestShopCampaignWidgetLayoutData()
    }

    private fun observeLatestShopCampaignWidgetLayoutData() {
        viewModelCampaign?.latestShopCampaignWidgetLayoutData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    getRecyclerView(view)?.visible()
                    setShopLayoutData(it.data)
                }
                is Fail -> {
                    onErrorGetLatestShopCampaignWidgetLayoutData(it.throwable)
                }
            }
        }
    }

    private fun onErrorGetLatestShopCampaignWidgetLayoutData(throwable: Throwable) {
        globalErrorShopPage?.visible()
        if (throwable is MessageErrorException) {
            globalErrorShopPage?.setType(GlobalError.SERVER_ERROR)
        } else {
            globalErrorShopPage?.setType(GlobalError.NO_CONNECTION)
        }
        globalErrorShopPage?.errorSecondaryAction?.show()
        globalErrorShopPage?.setOnClickListener {
            globalErrorShopPage?.errorSecondaryAction?.hide()
            getLatestShopCampaignWidgetLayoutData()
        }
        getRecyclerView(view)?.hide()
    }

    private fun observeCampaignWidgetListVisitable() {
        viewModelCampaign?.campaignWidgetListVisitable?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    shopCampaignTabAdapter.submitList(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun observeRedeemResult() {
        viewModelCampaign?.redeemResult?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    handleRedeemVoucherSuccess(it.data)
                }

                is Fail -> {
                    showToasterError(view ?: return@observe, it.throwable)
                }
            }
        }
    }

    private fun observeVoucherSliderWidgetData() {
        viewModelCampaign?.voucherSliderWidgetData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    viewModelCampaign?.updateVoucherSliderWidgetData(
                        shopCampaignTabAdapter.getNewVisitableItems(),
                        it.data
                    )
                }
                else -> {
                    shopCampaignTabAdapter.getVoucherSliderUiModel()?.copy(
                        isError = true
                    )?.let { voucherSliderUiModel ->
                        viewModelCampaign?.updateVoucherSliderWidgetData(
                            shopCampaignTabAdapter.getNewVisitableItems(),
                            voucherSliderUiModel
                        )
                    }
                }
            }
        }
    }

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
        checkShopLayout()
    }

    override fun checkShopLayout() {
        shopPageHomeLayoutUiModel.let {
            if (it != null) {
                setShopLayoutData(it)
                setWidgetLayoutPlaceholder()
            } else {
                getLatestShopCampaignWidgetLayoutData()
            }
        }
    }

    override fun setWidgetLayoutPlaceholder() {
        val shopHomeWidgetContentData =
            ShopPageCampaignMapper.mapShopCampaignWidgetLayoutToListShopCampaignWidget(
                listWidgetLayout,
                shopId
            )
        shopCampaignTabAdapter.setCampaignLayoutData(shopHomeWidgetContentData)
    }

    override fun setShopLayoutData(dataWidgetLayoutUiModel: ShopPageLayoutUiModel) {
        listWidgetLayout = dataWidgetLayoutUiModel.listWidgetLayout.toMutableList()
        val shopCampaignWidgetContentData =
            ShopPageCampaignMapper.mapShopCampaignWidgetLayoutToListShopCampaignWidget(
                dataWidgetLayoutUiModel.listWidgetLayout,
                shopId
            )
        shopCampaignTabAdapter.setCampaignLayoutData(shopCampaignWidgetContentData)
    }

    override fun initView() {
        globalErrorShopPage = viewBindingCampaignTab?.globalError
    }

    override fun observeShopHomeWidgetContentData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelCampaign?.shopCampaignWidgetContentData?.collect {
                when (it) {
                    is Success -> {
                        onSuccessGetShopHomeWidgetContentData(it.data)
                        renderCampaignTabBackgroundColor()
                    }

                    is Fail -> {
                        val throwable = it.throwable
                        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                        if (!ShopUtil.isExceptionIgnored(throwable)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this@ShopPageCampaignFragment::observeShopHomeWidgetContentData.name,
                                liveDataName = ShopHomeViewModel::shopHomeWidgetContentData.name,
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = errorMessage,
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = ShopPageLoggerConstant.Tag.SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                            )
                        }
                        showErrorToast(errorMessage)
                    }
                }
            }
            viewModelCampaign?.shopCampaignHomeWidgetContentDataError?.collect {
                shopCampaignTabAdapter.removeShopHomeWidget(it)
            }
        }
    }

    override fun observePlayWidget() {
        viewModel?.playWidgetObservable?.observe(viewLifecycleOwner) { carouselPlayWidgetUiModel ->
            shopPlayWidgetAnalytic.widgetId = carouselPlayWidgetUiModel?.widgetId.orEmpty()
            shopCampaignTabAdapter.updatePlayWidget(carouselPlayWidgetUiModel?.playWidgetState)

            val widget = carouselPlayWidgetUiModel?.playWidgetState

            if (widget?.model?.hasSuccessfulTranscodedChannel == true) showWidgetTranscodeSuccessToaster()

            val parent = parentFragment
            if (parent is InterfaceShopPageHeader) {
                val recyclerView = getRecyclerView(view)

                if (parent.isNewlyBroadcastSaved() == true) {
                    parent.clearIsNewlyBroadcastSaved()
                    recyclerView?.addOneTimeGlobalLayoutListener {
                        viewScope.launch {
                            parent.collapseAppBar()
                            val widgetPosition =
                                shopCampaignTabAdapter.list.orEmpty()
                                    .indexOfFirst { it is CarouselPlayWidgetUiModel }
                            val finalPosition = min(
                                ShopUtil.getActualPositionFromIndex(widgetPosition),
                                shopCampaignTabAdapter?.itemCount.orZero()
                            )
                            recyclerView.stepScrollToPositionWithDelay(
                                finalPosition,
                                PLAY_WIDGET_NEWLY_BROADCAST_SCROLL_DELAY
                            )
                        }
                    }
                }
            }

            carouselPlayWidgetUiModel?.actionEvent?.getContentIfNotHandled()?.let {
                when (it) {
                    is CarouselPlayWidgetUiModel.Action.Delete -> showWidgetDeletedToaster()
                    is CarouselPlayWidgetUiModel.Action.DeleteFailed -> showWidgetDeleteFailedToaster(
                        it.channelId,
                        it.reason
                    )

                    else -> {
                    }
                }
            }
        }
    }

    override fun onSuccessGetShopHomeWidgetContentData(mapWidgetContentData: Map<Pair<String, String>, Visitable<*>?>) {
        shopCampaignTabAdapter.updateShopCampaignWidgetContentData(mapWidgetContentData)
    }

    // region mvc widget listener
    override fun onVoucherImpression(model: ShopHomeVoucherUiModel, position: Int) {
        shopCampaignTabTracker.impressionShopBannerWidget(
            shopId,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            userId
        )
    }

    override fun onVoucherTokoMemberInformationImpression(
        model: ShopHomeVoucherUiModel,
        position: Int
    ) {
        shopCampaignTabTracker.impressionShopBannerWidget(
            shopId,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            userId
        )
        shopCampaignTabTracker.impressionSeeEntryPointMerchantVoucherCouponTokoMemberInformation(
            shopId
        )
    }

    override fun onVoucherReloaded() {
        getMvcWidgetData()
    }
    // endregion

    //region Bundling Widget
    override fun onImpressionBundlingWidget(
        model: ShopHomeProductBundleListUiModel,
        position: Int
    ) {
        shopCampaignTabTracker.impressionShopBannerWidget(
            shopId,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            userId
        )
    }

    override fun onMultipleBundleProductClicked(
        shopId: String,
        warehouseId: String,
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundleType: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int
    ) {
        shopCampaignTabTracker.clickCampaignTabProduct(
            selectedProduct.productId,
            selectedProduct.productName,
            selectedMultipleBundle.displayPriceRaw,
            widgetName,
            shopId,
            userId,
            widgetTitle,
            ShopUtil.getActualPositionFromIndex(productItemPosition),
            VALUE_MULTIPLE_BUNDLING,
            selectedMultipleBundle.bundleId
        )
        goToPDP(selectedProduct.productAppLink)
    }

    override fun onSingleBundleProductClicked(
        shopId: String,
        warehouseId: String,
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int,
        bundleType: String
    ) {
        shopCampaignTabTracker.clickCampaignTabProduct(
            selectedProduct.productId,
            selectedProduct.productName,
            selectedSingleBundle.displayPriceRaw,
            widgetName,
            shopId,
            userId,
            widgetTitle,
            ShopUtil.getActualPositionFromIndex(productItemPosition),
            VALUE_SINGLE_BUNDLING,
            selectedSingleBundle.bundleId
        )
        goToPDP(selectedProduct.productAppLink)
    }

    override fun impressionProductItemBundleMultiple(
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int
    ) {
        shopCampaignTabTracker.impressionCampaignTabProduct(
            selectedProduct.productId,
            selectedProduct.productName,
            selectedMultipleBundle.displayPriceRaw,
            widgetName,
            shopId,
            userId,
            widgetTitle,
            ShopUtil.getActualPositionFromIndex(productItemPosition),
            VALUE_MULTIPLE_BUNDLING,
            selectedMultipleBundle.bundleId
        )
    }

    override fun impressionProductBundleSingle(
        shopId: String,
        warehouseId: String,
        selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
        selectedProduct: ShopHomeBundleProductUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        bundleType: String
    ) {
        shopCampaignTabTracker.impressionCampaignTabProduct(
            selectedProduct.productId,
            selectedProduct.productName,
            selectedSingleBundle.displayPriceRaw,
            widgetName,
            shopId,
            userId,
            widgetTitle,
            ShopUtil.getActualPositionFromIndex(bundlePosition),
            VALUE_SINGLE_BUNDLING,
            selectedSingleBundle.bundleId
        )
    }
    //endregion

    override fun getWidgetTextColor(): Int {
        return parseColor(textColor)
    }

    private fun renderCampaignTabBackgroundColor() {
//        if (listBackgroundColor.isNotEmpty()) {
//            topView?.show()
//            centerView?.show()
//            bottomView?.show()
//            val colors = IntArray(listBackgroundColor.size)
//            for (i in listBackgroundColor.indices) {
//                colors[i] = parseColor(listBackgroundColor.getOrNull(i).orEmpty())
//            }
//            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
//            gradient.cornerRadius = 0f
//            topView?.setBackgroundColor(parseColor(listBackgroundColor.firstOrNull().orEmpty()))
//            centerView?.background = gradient
//            bottomView?.setBackgroundColor(parseColor(listBackgroundColor.lastOrNull().orEmpty()))
//        } else {
//            topView?.hide()
//            centerView?.hide()
//            bottomView?.hide()
//        }
    }

    private fun parseColor(colorHex: String): Int {
        return try {
            Color.parseColor(colorHex)
        } catch (e: Throwable) {
            0
        }
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
        return object :
            DataEndlessScrollListener(staggeredGridLayoutManager, shopCampaignTabAdapter) {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                val layoutManager =
                    (getRecyclerView(view)?.layoutManager as? StaggeredGridLayoutManager)
                val firstCompletelyVisibleItemPosition =
                    layoutManager?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0)
                        .orZero()
                if (firstCompletelyVisibleItemPosition == 0 && isClickToScrollToTop) {
                    isClickToScrollToTop = false
                    (parentFragment as? ShopPageHeaderFragment)?.expandHeader()
                }
                if (firstCompletelyVisibleItemPosition != latestCompletelyVisibleItemIndex) {
                    hideScrollToTopButton()
                }
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
                    checkIsShouldShowPerformanceDashboardCoachMark()
                }
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int) {}
        }
    }

    override fun checkIsShouldShowPerformanceDashboardCoachMark() {
        val isShownAlready = coachMarkSharedPref.hasBeenShown(ContentCoachMarkSharedPref.Key.PerformanceDashboardEntryPointShopPage, viewModel?.userSessionShopId.orEmpty())
        if (isShownAlready) return
        val recyclerView = getRecyclerView(view)
        recyclerView?.addOneTimeGlobalLayoutListener {
            val widgetPosition = shopCampaignTabAdapter.list.orEmpty().indexOfFirst { it is CarouselPlayWidgetUiModel }
            val widgetViewHolder = recyclerView.findViewHolderForAdapterPosition(widgetPosition)
            val ivAction = widgetViewHolder?.itemView?.findViewById<IconUnify>(com.tokopedia.play.widget.R.id.play_widget_iv_action)
            if (ivAction?.isVisible == true) {
                val coachMarkItems = mutableListOf<CoachMark2Item>()
                val coachMark = CoachMark2(requireContext())
                coachMarkItems.add(
                    CoachMark2Item(
                        anchorView = ivAction,
                        title = getString(com.tokopedia.content.common.R.string.performance_dashboard_coachmark_title),
                        description = getString(com.tokopedia.content.common.R.string.performance_dashboard_coachmark_subtitle),
                        position = CoachMark2.POSITION_BOTTOM,
                    )
                )
                coachMark.isOutsideTouchable = true
                coachMark.showCoachMark(ArrayList(coachMarkItems))
                coachMarkSharedPref.setHasBeenShown(
                    ContentCoachMarkSharedPref.Key.PerformanceDashboardEntryPointShopPage,
                    viewModel?.userSessionShopId.orEmpty()
                )
            }
        }
    }

    private fun checkLoadNextShopCampaignWidgetContentData(
        lastVisibleItemPosition: Int,
        firstVisibleItemPosition: Int
    ) {
        val shouldLoadLastVisibleItem =
            shopCampaignTabAdapter.isLoadNextCampaignWidgetData(lastVisibleItemPosition)
        val shouldLoadFirstVisibleItem =
            shopCampaignTabAdapter.isLoadNextCampaignWidgetData(firstVisibleItemPosition)
        if (shouldLoadLastVisibleItem || shouldLoadFirstVisibleItem) {
            val position = if (shouldLoadLastVisibleItem) {
                lastVisibleItemPosition
            } else {
                firstVisibleItemPosition
            }
            val listWidgetLayoutToLoad = getListWidgetLayoutToLoad(position)
            shopCampaignTabAdapter.updateShopCampaignWidgetStateToLoading(listWidgetLayoutToLoad)
            checkLoadVoucherSliderData(listWidgetLayoutToLoad)
            checkLoadPlayWidgetData(listWidgetLayoutToLoad)
            getWidgetContentData(listWidgetLayoutToLoad)

            listWidgetLayoutToLoad.clear()
        }
    }

    private fun checkLoadPlayWidgetData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>) {
        listWidgetLayoutToLoad.firstOrNull {
            isWidgetPlay(it)
        }?.let {
            listWidgetLayoutToLoad.remove(it)
            getPlayWidgetData()
        }
    }

    override fun getPlayWidgetData() {
        shopCampaignTabAdapter.getPlayWidgetUiModel()?.let {
            viewModel?.getPlayWidget(shopId, it)
        }
    }

    private fun checkLoadVoucherSliderData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>) {
        listWidgetLayoutToLoad.firstOrNull {
            isWidgetVoucherSlider(it)
        }?.let {
            listWidgetLayoutToLoad.remove(it)
            getVoucherSliderData()
        }
    }

    private fun getVoucherSliderData() {
        shopCampaignTabAdapter.getVoucherSliderUiModel()?.let {
            viewModelCampaign?.getVoucherSliderData(it)
        }
    }

    private fun isWidgetVoucherSlider(uiModel: ShopPageWidgetUiModel): Boolean {
        return uiModel.widgetType == WidgetType.VOUCHER_SLIDER && uiModel.widgetName == WidgetName.VOUCHER
    }

    private fun getWidgetContentData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>) {
        if (listWidgetLayoutToLoad.isNotEmpty()) {
            val widgetUserAddressLocalData = ShopUtil.getShopPageWidgetUserAddressLocalData(context)
                ?: LocalCacheModel()
            viewModelCampaign?.getWidgetContentData(
                listWidgetLayoutToLoad.toList(),
                shopId,
                widgetUserAddressLocalData,
            )
        }
    }

    private fun getListWidgetLayoutToLoad(lastCompletelyVisibleItemPosition: Int): MutableList<ShopPageWidgetUiModel> {
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

    override fun scrollToTop() {
        isClickToScrollToTop = true
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun isShowScrollToTopButton(): Boolean {
        return latestCompletelyVisibleItemIndex > 0
    }

    private fun hideScrollToTopButton() {
        (parentFragment as? ShopPageHeaderFragment)?.hideScrollToTopButton()
    }

    private fun showScrollToTopButton() {
        (parentFragment as? ShopPageHeaderFragment)?.showScrollToTopButton()
    }

    fun setCampaignTabBackgroundColor(backgroundColor: String) {
        this.campaignTabBackgroundColor = backgroundColor
    }

    fun setCampaignTabBackgroundPatternImage(backgroundPatternImage: String) {
        this.campaignTabBackgroundPatternImage = backgroundPatternImage
    }

    override fun isCampaignTabDarkMode(): Boolean {
        return true
    }

    override fun resumeSliderBannerAutoScroll() {
        shopCampaignTabAdapter.resumeSliderBannerAutoScroll()
    }

    override fun pauseSliderBannerAutoScroll() {
        shopCampaignTabAdapter.pauseSliderBannerAutoScroll()
    }

    override fun onProductImageClicked(productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData) {
        RouteManager.route(context, productData.appLink)
    }

    override fun onProductImageImpression(productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData) {
    }

    override fun onWidgetSliderBannerHighlightImpression(
        uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
        bindingAdapterPosition: Int
    ) {
    }

    override fun onWidgetSliderBannerHighlightCtaClicked(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        checkShouldSelectHomeTab(uiModel.header.ctaLink)
    }

    private fun checkShouldSelectHomeTab(appLink: String) {
        if (Uri.parse(appLink).lastPathSegment.equals(HOME_TAB_APP_LINK_LAST_SEGMENT, true)) {
            (parentFragment as? ShopPageHeaderFragment)?.selectShopTab(ShopPageHeaderTabName.HOME)
        }
    }

    override fun onCampaignCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    ) {
        goToPDP(productUiModel?.productUrl.orEmpty())
    }

    override fun onCampaignCarouselProductItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    ) {
    }

    override fun onCtaClicked(carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?) {
        context?.let {
            RouteManager.route(it, carouselProductWidgetUiModel?.header?.ctaLink)
        }
    }

    override fun onCampaignCarouselProductWidgetImpression(
        position: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel
    ) {
    }

    override fun onCampaignVoucherSliderItemImpression(
        model: ExclusiveLaunchVoucher,
        position: Int
    ) {
    }

    override fun onCampaignVoucherSliderItemClick(model: ExclusiveLaunchVoucher, position: Int) {
        showVoucherDetailBottomSheet(model)
    }

    override fun onCampaignVoucherSliderItemCtaClaimClick(
        model: ExclusiveLaunchVoucher,
        position: Int
    ) {
        if (isLogin) {
            redeemCampaignVoucherSlider(model)
        } else {
            redirectToLoginPage()
        }
    }

    private fun redeemCampaignVoucherSlider(model: ExclusiveLaunchVoucher) {
        viewModelCampaign?.redeemCampaignVoucherSlider(model)
    }

    override fun onCampaignVoucherSliderMoreItemClick(listCategorySlug: List<String>) {
        showVoucherListBottomSheet(listCategorySlug)
    }

    private fun showVoucherListBottomSheet(listCategorySlug: List<String>) {
        val bottomSheet = ExclusiveLaunchVoucherListBottomSheet.newInstance(
            shopId = shopId,
            useDarkBackground = isCampaignTabDarkMode(),
            slugs = listCategorySlug
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showVoucherDetailBottomSheet(selectedVoucher: ExclusiveLaunchVoucher) {
        if (!isAdded) return

        val bottomSheet = VoucherDetailBottomSheet.newInstance(
            shopId = shopId,
            slug = selectedVoucher.slug,
            promoVoucherCode = selectedVoucher.couponCode
        ).apply {
            setOnVoucherRedeemSuccess { redeemResult ->
                handleRedeemVoucherSuccess(redeemResult)
            }
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun handleRedeemVoucherSuccess(redeemResult: RedeemPromoVoucherResult) {
        if (redeemResult.redeemMessage.isNotEmpty()) {
            showToaster(view ?: return, redeemResult.redeemMessage)
            getVoucherSliderData()
        }
    }

    override fun onSuccessGetYouTubeData(widgetId: String, data: YoutubeVideoDetailModel) {
        shopCampaignTabAdapter.setHomeYouTubeData(widgetId, data)
    }

    override fun onFailedGetYouTubeData(widgetId: String, throwable: Throwable) {
        ShopPageExceptionHandler.logExceptionToCrashlytics(
            ShopPageExceptionHandler.ERROR_WHEN_GET_YOUTUBE_DATA,
            throwable
        )
        shopCampaignTabAdapter.setHomeYouTubeData(widgetId, YoutubeVideoDetailModel())
    }

    override fun onDisplayBannerTimerClicked(
        position: Int,
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {}

    override fun onClickCtaDisplayBannerTimerWidget(uiModel: ShopWidgetDisplayBannerTimerUiModel) {}

    override fun onTimerFinished(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        shopCampaignTabAdapter.removeWidget(uiModel)
        endlessRecyclerViewScrollListener.resetState()
        getLatestShopCampaignWidgetLayoutData()
    }

    private fun getLatestShopCampaignWidgetLayoutData() {
        globalErrorShopPage?.hide()
        shopCampaignTabAdapter.showLoading()
        scrollToTop()
        viewModelCampaign?.getLatestShopCampaignWidgetLayoutData(
            shopId,
            extParam,
            ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
            "CampaignTab"
        )
    }

    override fun onClickRemindMe(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        viewModel?.let {
            if (it.isLogin) {
                viewModelCampaign?.showBannerTimerRemindMeLoading(
                    shopCampaignTabAdapter.getNewVisitableItems()
                )
                handleBannerTimerClickRemindMe(uiModel)
            } else {
                redirectToLoginPage()
            }
        }
    }

    override fun onSuccessCheckBannerTimerNotifyMe(data: CheckCampaignNotifyMeUiModel) {
        val isRegisterCampaign = data.action.equals(
            NotifyMeAction.REGISTER.action,
            ignoreCase = true
        )
        viewModelCampaign?.updateBannerTimerWidgetData(
            shopCampaignTabAdapter.getNewVisitableItems(),
            isRegisterCampaign,
            true
        )
        view?.let {
            Toaster.build(
                it,
                data.message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.shop_string_ok)
            ).show()
        }
    }

    override fun onFailCheckBannerTimerNotifyMe(errorMessage: String) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_string_ok)
            ).show()
        }
        viewModelCampaign?.updateBannerTimerWidgetData(
            shopCampaignTabAdapter.getNewVisitableItems(),
            isRemindMe = false,
            isClickRemindMe = false
        )
    }

}
