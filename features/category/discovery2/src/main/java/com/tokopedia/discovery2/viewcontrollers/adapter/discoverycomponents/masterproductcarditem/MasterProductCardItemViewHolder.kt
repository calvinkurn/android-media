package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.content.res.Resources
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.ClickAreaType
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.pdp.AtcBuyType
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Constant.ProductTemplate.LIST
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils.Companion.isOldProductCardType
import com.tokopedia.discovery2.analytics.TrackDiscoveryRecommendationMapper.asProductTrackModel
import com.tokopedia.discovery2.analytics.TrackDiscoveryRecommendationMapper.asTrackConfirmCart
import com.tokopedia.discovery2.analytics.TrackDiscoveryRecommendationMapper.isEligibleToTrack
import com.tokopedia.discovery2.analytics.TrackDiscoveryRecommendationMapper.isEligibleToTrackRecTrigger
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.productcarditem.DiscoATCRequestParams
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.notifications.settings.NotificationGeneralPromptLifecycleCallbacks
import com.tokopedia.notifications.settings.NotificationReminderPrompt
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import timber.log.Timber
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class MasterProductCardItemViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner),
    ATCNonVariantListener,
    AppLogRecTriggerInterface
{

    private var masterProductCardItemViewModel: MasterProductCardItemViewModel? = null
    private var masterProductCardGridView: ProductCardGridView? = null
    private var masterProductCardListView: ProductCardListView? = null
    private var productCardView: CardUnify2? =
        itemView.findViewById(productcardR.id.cardViewProductCard)
    private var productCardName = ""
    private var dataItem: DataItem? = null
    private var componentPosition: Int? = null
    private var buttonNotify: UnifyButton? = null
    private var lastClickTime = 0L
    private var interval: Int = 500
    private var isFullFilment: Boolean = false
    private var isRecommendation = false

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        isRecommendation = masterProductCardItemViewModel?.components?.recomQueryProdId != null
        masterProductCardItemViewModel = discoveryBaseViewModel as MasterProductCardItemViewModel
        masterProductCardItemViewModel?.let {
            getSubComponent().inject(it)
        }
        lastClickTime = 0L
        initView()
    }

    private fun initView() {
        if (masterProductCardItemViewModel?.getTemplateType() == LIST) {
            masterProductCardListView = itemView.findViewById(R.id.master_product_card_list)
            buttonNotify = masterProductCardListView?.getNotifyMeButton()
            masterProductCardListView?.setNotifyMeOnClickListener {
                if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
                    return@setNotifyMeOnClickListener
                }
                lastClickTime = SystemClock.elapsedRealtime()
                sentNotifyButtonEvent()
                masterProductCardItemViewModel?.subscribeUser()
            }

            masterProductCardListView?.setAddVariantClickListener {
                openVariantSheet()
            }
            masterProductCardListView?.setAddToCartNonVariantClickListener(this)
            masterProductCardListView?.setAddToCartWishlistOnClickListener {
                handleATC(
                    masterProductCardItemViewModel?.getProductDataItem()?.minQuantity ?: 1,
                    true
                )
            }
            masterProductCardListView?.setAddToCartOnClickListener {
                handleATC(
                    masterProductCardItemViewModel?.getProductDataItem()?.minQuantity ?: 1,
                    masterProductCardItemViewModel?.getProductDataItem()?.atcButtonCTA == Constant.ATCButtonCTATypes.GENERAL_CART
                )
            }
            masterProductCardListView?.setOnClickListener(object: ProductCardClickListener {
                override fun onClick(v: View) {
                    handleUIClick(v)
                }

                override fun onSellerInfoClicked(v: View) {
                    sendAdsRealtimeClickByteIo(AdsLogConst.Refer.SELLER_NAME)
                }

                override fun onAreaClicked(v: View) {
                    sendAdsRealtimeClickByteIo(AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    sendAdsRealtimeClickByteIo(AdsLogConst.Refer.COVER)
                }
            })
        } else {
            masterProductCardGridView = itemView.findViewById(R.id.master_product_card_grid)
            buttonNotify = masterProductCardGridView?.getNotifyMeButton()
            masterProductCardGridView?.setNotifyMeOnClickListener {
                if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
                    return@setNotifyMeOnClickListener
                }
                lastClickTime = SystemClock.elapsedRealtime()
                sentNotifyButtonEvent()
                masterProductCardItemViewModel?.subscribeUser()
                showNotificationReminderPrompt()
            }

            masterProductCardGridView?.setAddVariantClickListener {
                openVariantSheet()
            }
            masterProductCardGridView?.setAddToCartNonVariantClickListener(this)
            masterProductCardGridView?.setAddToCartOnClickListener {
                if (checkForVariantProductCard(masterProductCardItemViewModel?.getProductDataItem()?.parentProductId)) {
                    openVariantSheet()
                } else {
                    handleATC(
                        masterProductCardItemViewModel?.getProductDataItem()?.minQuantity ?: 1,
                        masterProductCardItemViewModel?.getProductDataItem()?.atcButtonCTA == Constant.ATCButtonCTATypes.GENERAL_CART
                    )
                }
            }
            masterProductCardGridView?.setOnClickListener(object: ProductCardClickListener {
                override fun onClick(v: View) {
                    handleUIClick(v)
                }

                override fun onSellerInfoClicked(v: View) {
                    sendAdsRealtimeClickByteIo(AdsLogConst.Refer.SELLER_NAME)
                }

                override fun onAreaClicked(v: View) {
                    sendAdsRealtimeClickByteIo(AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    sendAdsRealtimeClickByteIo(AdsLogConst.Refer.COVER)
                }
            })
        }
    }

    private fun sendAdsRealtimeClickByteIo(refer: String) {
        dataItem?.let {
            if (it.isTopads == true) {
                AppLogTopAds.sendEventRealtimeClick(itemView.context, it.asAdsLogRealtimeClickModel(refer))
            }
        }
    }

    private fun setCtaClickListener() {
        masterProductCardListView?.setGenericCtaButtonOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
                return@setGenericCtaButtonOnClickListener
            }
            lastClickTime = SystemClock.elapsedRealtime()
            sentNotifyButtonEvent()
            masterProductCardItemViewModel?.subscribeUser()
            showNotificationReminderPrompt()
        }
        masterProductCardGridView?.setGenericCtaButtonOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
                return@setGenericCtaButtonOnClickListener
            }
            lastClickTime = SystemClock.elapsedRealtime()
            sentNotifyButtonEvent()
            masterProductCardItemViewModel?.subscribeUser()
            showNotificationReminderPrompt()
        }
    }

    private fun checkForVariantProductCard(parentProductId: String?): Boolean {
        return parentProductId != null && parentProductId.toLongOrZero() > 0
    }

    private fun showNotificationReminderPrompt() {
        val pageName = "kejarDiskon"
        masterProductCardItemViewModel?.getProductDataItem()?.notifyMe?.let { notifyMeStatus ->
            if (!notifyMeStatus) {
                (fragment as DiscoveryFragment).activity?.let {
                    val view = NotificationGeneralPromptLifecycleCallbacks()
                        .notificationGeneralPromptView(it, pageName)
                    NotificationReminderPrompt(view).showReminderPrompt(it, pageName)
                }
            }
        }
    }

    private fun openVariantSheet() {
        masterProductCardItemViewModel?.getProductDataItem()?.let { dataItem ->
            (fragment as DiscoveryFragment).openVariantBottomSheet(
                productId = dataItem.productId.orEmpty(),
                parentPosition = masterProductCardItemViewModel?.getParentPositionForCarousel()
                    ?: RecyclerView.NO_POSITION,
                requestingComponent = masterProductCardItemViewModel?.components
            )
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        masterProductCardItemViewModel?.let { masterProductCardItemViewModel ->
            productCardName = masterProductCardItemViewModel.getComponentName()
        }
        lifecycleOwner?.let { lifecycle ->
            masterProductCardItemViewModel?.getDataItemValue()?.observe(lifecycle) { data ->
                if (masterProductCardListView !=  null) {
                    masterProductCardListView?.setVisibilityPercentListener(
                        isTopAds = data.isTopads.orFalse(),
                        eventListener = createVisibilityPercentCallback(data)
                    )
                } else {
                    masterProductCardGridView?.setVisibilityPercentListener(
                        isTopAds = data.isTopads.orFalse(),
                        eventListener = createVisibilityPercentCallback(data)
                    )
                }

                dataItem = data
            }
            masterProductCardItemViewModel?.getProductModelValue()?.observe(lifecycle) { data ->
                populateData(data)
            }
            masterProductCardItemViewModel?.getComponentPosition()?.observe(lifecycle) { position ->
                componentPosition = position
            }
            masterProductCardItemViewModel?.getShowLoginData()?.observe(lifecycle) {
                if (it == true) {
                    componentPosition?.let { position ->
                        (fragment as DiscoveryFragment).openLoginScreen(
                            position
                        )
                    }
                }
            }
            masterProductCardItemViewModel?.notifyMeCurrentStatus()?.observe(lifecycle) {
                updateNotifyMeButton(it)
                updateNotifyMeState(it)
            }
            masterProductCardItemViewModel?.showNotifyToastMessage()?.observe(lifecycle) {
                showNotifyResultToast(it)
            }
            masterProductCardItemViewModel?.getComponentPosition()?.observe(lifecycle) {
                componentPosition = it
            }
            masterProductCardItemViewModel?.getSyncPageLiveData()?.observe(lifecycle) {
                if (it) (fragment as DiscoveryFragment).reSync()
            }
            masterProductCardItemViewModel?.getScrollSimilarProductComponentID()
                ?.observe(lifecycle) {
                    (fragment as DiscoveryFragment).scrollToComponentWithID(it)
                }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            masterProductCardItemViewModel?.getDataItemValue()?.removeObservers(it)
            masterProductCardItemViewModel?.getProductModelValue()?.removeObservers(it)
            masterProductCardItemViewModel?.getComponentPosition()?.removeObservers(it)
            masterProductCardItemViewModel?.getShowLoginData()?.removeObservers(it)
            masterProductCardItemViewModel?.notifyMeCurrentStatus()?.removeObservers(it)
            masterProductCardItemViewModel?.showNotifyToastMessage()?.removeObservers(it)
            masterProductCardItemViewModel?.getComponentPosition()?.removeObservers(it)
            masterProductCardItemViewModel?.getSyncPageLiveData()?.removeObservers(it)
            masterProductCardItemViewModel?.getScrollSimilarProductComponentID()
                ?.removeObservers(it)
            Timber.d("Is notifyMeCurrentStatus removed -> ${masterProductCardItemViewModel?.getProductDataItem()?.name}")
        }
    }

    private fun populateData(productCardModel: ProductCardModel) {
        adjustLayoutDimens()
        masterProductCardGridView?.setProductModel(productCardModel)
        masterProductCardListView?.setProductModel(productCardModel)
        updateNotifyMeState(dataItem?.notifyMe)

        setWishlist()
        setCtaClickListener()
        set3DotsWishlistWithAtc(dataItem)
        setSimilarProductWishlist(dataItem)
        checkProductIsFulfillment(productCardModel)
        setButtonATCOnClickListener()
    }

    private fun adjustLayoutDimens() {
        val oldVersionCard = masterProductCardItemViewModel?.components?.properties.isOldProductCardType()
        if (!oldVersionCard) return

        if (productCardName == ComponentNames.ProductCardCarouselItem.componentName ||
                productCardName == ComponentNames.ProductCardSprintSaleCarouselItem.componentName ||
                productCardName == ComponentNames.ProductCardCarouselItemList.componentName ||
                productCardName == ComponentNames.ShopOfferHeroBrandProductItem.componentName
        ) {
            masterProductCardGridView?.let {
                it.applyCarousel()
                productCardView?.layoutParams?.width =
                        itemView.context.resources.getDimensionPixelSize(R.dimen.disco_product_card_width)
                it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            masterProductCardListView?.let {
                it.applyCarousel()
                productCardView?.layoutParams?.width =
                        Resources.getSystem().displayMetrics.widthPixels - itemView.context.resources.getDimensionPixelSize(
                            R.dimen.dp_70
                        )
                it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        } else {
            setProductViewDimens()
        }
    }

    private fun setButtonATCOnClickListener() {
        masterProductCardListView?.setAddToCartOnClickListener {
            handleATC(
                masterProductCardItemViewModel?.getProductDataItem()?.minQuantity ?: 1,
                masterProductCardItemViewModel?.getProductDataItem()?.atcButtonCTA == Constant.ATCButtonCTATypes.GENERAL_CART
            )
        }
        masterProductCardGridView?.setAddToCartOnClickListener {
            if (checkForVariantProductCard(masterProductCardItemViewModel?.getProductDataItem()?.parentProductId)) {
                openVariantSheet()
            } else {
                handleATC(
                    masterProductCardItemViewModel?.getProductDataItem()?.minQuantity ?: 1,
                    masterProductCardItemViewModel?.getProductDataItem()?.atcButtonCTA == Constant.ATCButtonCTATypes.GENERAL_CART
                )
            }
        }
    }

    private fun checkProductIsFulfillment(productCardModel: ProductCardModel) {
        productCardModel.labelGroupList.forEach {
            if (it.position == IS_FULFILLMENT) {
                isFullFilment = true
            }
        }
    }

    private fun setSimilarProductWishlist(dataItem: DataItem?) {
        if (dataItem?.hasSimilarProductWishlist == true) {
            masterProductCardListView?.setSeeSimilarProductWishlistOnClickListener {
                masterProductCardItemViewModel?.scrollToTargetSimilarProducts()
            }
        }
    }

    private fun set3DotsWishlistWithAtc(dataItem: DataItem?) {
        if (dataItem?.hasThreeDotsWishlist == true) {
            masterProductCardListView?.setThreeDotsWishlistOnClickListener {
                masterProductCardItemViewModel?.saveProductCardComponent()
                masterProductCardItemViewModel?.getThreeDotsWishlistOptionsModel()?.let { it1 ->
                    showProductCardOptions(
                        fragment,
                        it1
                    )
                }
            }
        }
    }

    private fun setWishlist() {
        masterProductCardGridView?.setThreeDotsOnClickListener {
            masterProductCardItemViewModel?.saveProductCardComponent()
            masterProductCardItemViewModel?.getProductCardOptionsModel()?.let { it1 ->
                showProductCardOptions(
                    fragment,
                    it1
                )
            }
        }
    }

    private fun updateNotifyMeButton(notifyMeStatus: Boolean?) {
        if (dataItem?.hasNotifyMe == true) {
            val notifyText = masterProductCardItemViewModel?.getNotifyText(notifyMeStatus)
            masterProductCardListView?.reRenderGenericCtaButton(
                ProductCardModel(
                    productCardGenericCta = ProductCardModel.ProductCardGenericCta(
                        copyWriting = notifyText,
                        mainButtonVariant = UnifyButton.Variant.GHOST,
                        mainButtonType = if (notifyMeStatus == true) {
                            UnifyButton.Type.ALTERNATE
                        } else {
                            UnifyButton.Type.MAIN
                        }
                    )
                )
            )
            masterProductCardGridView?.reRenderGenericCtaButton(
                ProductCardModel(
                    productCardGenericCta = ProductCardModel.ProductCardGenericCta(
                        copyWriting = notifyText,
                        mainButtonVariant = UnifyButton.Variant.GHOST,
                        mainButtonType = if (notifyMeStatus == true) {
                            UnifyButton.Type.ALTERNATE
                        } else {
                            UnifyButton.Type.MAIN
                        }
                    )
                )
            )
        }
    }

    private fun updateNotifyMeState(notifyMeStatus: Boolean?) {
        val notifyText = masterProductCardItemViewModel?.getNotifyText(notifyMeStatus)
        buttonNotify?.let {
            if (dataItem?.hasNotifyMe == true) {
                it.text = notifyText
                if (notifyMeStatus == true) {
                    it.apply {
                        setTextColor(context.resources.getColor(unifyprinciplesR.color.Unify_NN950_68))
                        buttonVariant = UnifyButton.Variant.GHOST
                        buttonType = UnifyButton.Type.ALTERNATE
                    }
                } else {
                    it.apply {
                        setTextColor(context.resources.getColor(unifyprinciplesR.color.Unify_GN500))
                        buttonVariant = UnifyButton.Variant.GHOST
                        buttonType = UnifyButton.Type.MAIN
                    }
                }
            }
        }
    }

    private fun setProductViewDimens() {
        masterProductCardGridView?.let {
            it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        masterProductCardListView?.let {
            it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun handleUIClick(view: View) {
        dataItem?.let {
            if (it.isEligibleToTrack()) {
                AppLogRecommendation.sendProductClickAppLog(
                    it.asProductTrackModel(productCardName),
                    ClickAreaType.PRODUCT
                )
            }
        }

        masterProductCardItemViewModel?.sendTopAdsClick()
        var applink = dataItem?.applinks ?: ""
        if ((fragment as DiscoveryFragment).isAffiliateInitialized) {
            applink =
                fragment.createAffiliateLink(applink)
        }
        masterProductCardItemViewModel?.navigate(fragment.context, applink)
        sendClickEvent()
    }

    private fun sendClickEvent() {
        masterProductCardItemViewModel?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackProductCardClick(
                    it.components,
                    it.isUserLoggedIn(),
                    isFullFilment,
                    dataItem?.warehouseId ?: 0
                )
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        masterProductCardItemViewModel?.trackShowProductCard()
        masterProductCardItemViewModel?.sendTopAdsView()
        masterProductCardItemViewModel?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .viewProductsList(
                    it.components,
                    it.isUserLoggedIn(),
                    isFullFilment,
                    dataItem?.warehouseId ?: 0
                )
        }
    }

    private fun showNotifyResultToast(toastData: Pair<Boolean, String?>) {
        try {
            if (!toastData.first && !toastData.second.isNullOrEmpty()) {
                Toaster.make(
                    itemView.rootView,
                    toastData.second!!,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL
                )
            } else if (toastData.first && !toastData.second.isNullOrEmpty()) {
                Toaster.make(
                    itemView.rootView,
                    toastData.second!!,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR
                )
            } else {
                Toaster.make(
                    itemView.rootView,
                    itemView.context.getString(R.string.product_card_error_msg),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sentNotifyButtonEvent() {
        masterProductCardItemViewModel?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackNotifyClick(it.components, it.isUserLoggedIn(), it.getUserID())
        }
    }

    override fun onQuantityChanged(quantity: Int) {
        handleATC(quantity, false)
    }

    private fun handleATC(quantity: Int, isGeneralCartATC: Boolean) {
        masterProductCardItemViewModel?.let { masterProductCardItemViewModel ->
            masterProductCardItemViewModel.updateProductQuantity(quantity)
            if (masterProductCardItemViewModel.isUserLoggedIn()) {
                masterProductCardItemViewModel.getProductDataItem()?.let { productItem ->
                    productItem.productId?.let { productId ->
                        if (productId.isNotEmpty()) {
                            (fragment as DiscoveryFragment).addOrUpdateItemCart(
                                DiscoATCRequestParams(
                                    parentPosition = masterProductCardItemViewModel.getParentPositionForCarousel(),
                                    position = masterProductCardItemViewModel.position,
                                    productId = productId,
                                    quantity = quantity,
                                    shopId = if (isGeneralCartATC) productItem.shopId else null,
                                    isGeneralCartATC = isGeneralCartATC,
                                    requestingComponent = masterProductCardItemViewModel.components,
                                    appLogParam = AppLogAnalytics.getEntranceInfo(AtcBuyType.ATC)
                                )
                            )

                            if (productItem.isEligibleToTrack()) {
                                productItem.trackConfirmCartAppLog()
                            }
                        }
                    }
                }
            } else {
                masterProductCardItemViewModel.handleATCFailed()
                (fragment as DiscoveryFragment).openLoginScreen()
            }
        }
    }

    private fun DataItem.trackConfirmCartAppLog() {
        val productTrackModel = asProductTrackModel(productCardName)
        AppLogRecommendation.sendConfirmCartAppLog(productTrackModel, asTrackConfirmCart())
    }

    companion object {
        const val IS_FULFILLMENT = "fulfillment"
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return RecommendationTriggerObject(
            sessionId = dataItem?.getAppLog()?.sessionId.orEmpty(),
            requestId = dataItem?.getAppLog()?.requestId.orEmpty(),
            moduleName = dataItem?.getAppLog()?.pageName.orEmpty(),
            listName = dataItem?.topLevelTab?.name.orEmpty(),
            listNum = dataItem?.topLevelTab?.index ?: -1
        )
    }

    override fun isEligibleToTrack(): Boolean {
        return dataItem?.isEligibleToTrackRecTrigger(masterProductCardItemViewModel?.getComponentName().orEmpty()).orFalse()
    }

    private fun createVisibilityPercentCallback(data: DataItem) = object : ProductConstraintLayout.OnVisibilityPercentChanged {
        override fun onShow() {
            if (data.isTopads.orFalse()) {
                AppLogTopAds.sendEventShow(itemView.context, data.asAdsLogShowModel())
            }
        }

        override fun onShowOver(maxPercentage: Int) {
            if (data.isTopads.orFalse()) {
                AppLogTopAds.sendEventShowOver(itemView.context, data.asAdsLogShowOverModel(maxPercentage))
            }
        }
    }
}
