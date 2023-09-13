package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.content.res.Resources
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Constant.ProductTemplate.LIST
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.productcarditem.DiscoATCRequestParams
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.notifications.settings.NotificationGeneralPromptLifecycleCallbacks
import com.tokopedia.notifications.settings.NotificationReminderPrompt
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton

class MasterProductCardItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner), ATCNonVariantListener {

    private var masterProductCardItemViewModel: MasterProductCardItemViewModel? = null
    private var masterProductCardGridView: ProductCardGridView? = null
    private var masterProductCardListView: ProductCardListView? = null
    private var productCardView: CardView = itemView.findViewById(com.tokopedia.productcard.R.id.cardViewProductCard)
    private var productCardName = ""
    private var dataItem: DataItem? = null
    private var componentPosition: Int? = null
    private var buttonNotify: UnifyButton? = null
    private var lastClickTime = 0L
    private var interval: Int = 500
    private var isFulFillment: Boolean = false

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
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
                handleATC(
                    masterProductCardItemViewModel?.getProductDataItem()?.minQuantity ?: 1,
                    masterProductCardItemViewModel?.getProductDataItem()?.atcButtonCTA == Constant.ATCButtonCTATypes.GENERAL_CART
                )
            }
        }
        productCardView.setOnClickListener {
            handleUIClick(it)
        }
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
        masterProductCardItemViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventProductATCTokonow(
                it,
                ""
            )
        }
        masterProductCardItemViewModel?.getProductDataItem()?.let { dataItem ->
            (fragment as DiscoveryFragment).openVariantBottomSheet(dataItem.productId ?: "")
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        masterProductCardItemViewModel?.let { masterProductCardItemViewModel ->
            productCardName = masterProductCardItemViewModel.getComponentName()
        }
        lifecycleOwner?.let { lifecycle ->
            masterProductCardItemViewModel?.getDataItemValue()?.observe(
                lifecycle,
                Observer { data ->
                    dataItem = data
                }
            )
            masterProductCardItemViewModel?.getProductModelValue()?.observe(
                lifecycle,
                Observer { data ->
                    populateData(data)
                }
            )
            masterProductCardItemViewModel?.getComponentPosition()?.observe(
                lifecycle,
                Observer { position ->
                    componentPosition = position
                }
            )
            masterProductCardItemViewModel?.getShowLoginData()?.observe(
                lifecycle,
                Observer {
                    if (it == true) {
                        componentPosition?.let { position -> (fragment as DiscoveryFragment).openLoginScreen(position) }
                    }
                }
            )
            masterProductCardItemViewModel?.notifyMeCurrentStatus()?.observe(
                lifecycle,
                Observer {
                    updateNotifyMeState(it)
                }
            )
            masterProductCardItemViewModel?.showNotifyToastMessage()?.observe(
                lifecycle,
                Observer {
                    showNotifyResultToast(it)
                }
            )
            masterProductCardItemViewModel?.getComponentPosition()?.observe(
                lifecycle,
                Observer {
                    componentPosition = it
                }
            )
            masterProductCardItemViewModel?.getSyncPageLiveData()?.observe(
                lifecycle,
                Observer {
                    if (it) (fragment as DiscoveryFragment).reSync()
                }
            )
            masterProductCardItemViewModel?.getScrollSimilarProductComponentID()?.observe(lifecycle) {
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
            masterProductCardItemViewModel?.getScrollSimilarProductComponentID()?.removeObservers(it)
        }
    }

    private fun populateData(productCardModel: ProductCardModel) {
        if (productCardName == ComponentNames.ProductCardCarouselItem.componentName ||
            productCardName == ComponentNames.ProductCardSprintSaleCarouselItem.componentName ||
            productCardName == ComponentNames.ProductCardCarouselItemList.componentName
        ) {
            masterProductCardGridView?.let {
                productCardView.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.disco_product_card_width)
                it.applyCarousel()
                it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            masterProductCardListView?.let {
                it.applyCarousel()
                productCardView.layoutParams?.width = Resources.getSystem().displayMetrics.widthPixels - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_70)
                it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        } else {
            setProductViewDimens()
        }
        masterProductCardGridView?.setProductModel(productCardModel)
        masterProductCardListView?.setProductModel(productCardModel)
        updateNotifyMeState(dataItem?.notifyMe)

        setWishlist()
        set3DotsWishlistWithAtc(dataItem)
        setSimilarProductWishlist(dataItem)
        checkProductIsFulfillment(productCardModel)
    }
    private fun checkProductIsFulfillment(productCardModel: ProductCardModel) {
        productCardModel.labelGroupList.forEach {
            if (it.position == IS_FULFILLMENT) {
                isFulFillment = true
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

    private fun updateNotifyMeState(notifyMeStatus: Boolean?) {
        val notifyText = masterProductCardItemViewModel?.getNotifyText(notifyMeStatus)
        buttonNotify?.let {
            if (dataItem?.hasNotifyMe == true) {
                it.text = notifyText
                if (notifyMeStatus == true) {
                    it.apply {
                        setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
                        buttonVariant = UnifyButton.Variant.GHOST
                        buttonType = UnifyButton.Type.ALTERNATE
                    }
                } else {
                    it.apply {
                        setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500))
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
        when (view) {
            productCardView -> {
                masterProductCardItemViewModel?.sendTopAdsClick()
                var applink = dataItem?.applinks ?: ""
                if ((fragment as DiscoveryFragment).isAffiliateInitialized) {
                    applink =
                        fragment.createAffiliateLink(applink)
                }
                masterProductCardItemViewModel?.navigate(fragment.context, applink)
                sendClickEvent()
            }
        }
    }

    private fun sendClickEvent() {
        masterProductCardItemViewModel?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackProductCardClick(
                    it.components,
                    it.isUserLoggedIn(),
                    isFulFillment,
                    dataItem?.warehouseId ?: 0
                )
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        masterProductCardItemViewModel?.sendTopAdsView()
        masterProductCardItemViewModel?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .viewProductsList(
                    it.components,
                    it.isUserLoggedIn(),
                    isFulFillment,
                    dataItem?.warehouseId ?: 0
                )
        }
    }

    private fun showNotifyResultToast(toastData: Pair<Boolean, String?>) {
        try {
            if (!toastData.first && !toastData.second.isNullOrEmpty()) {
                Toaster.make(itemView.rootView, toastData.second!!, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
            } else if (toastData.first && !toastData.second.isNullOrEmpty()) {
                Toaster.make(itemView.rootView, toastData.second!!, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            } else {
                Toaster.make(itemView.rootView, itemView.context.getString(R.string.product_card_error_msg), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sentNotifyButtonEvent() {
        masterProductCardItemViewModel?.let { (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackNotifyClick(it.components, it.isUserLoggedIn(), it.getUserID()) }
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
                                    requestingComponent = masterProductCardItemViewModel.components
                                )
                            )
                        }
                    }
                }
            } else {
                masterProductCardItemViewModel.handleATCFailed()
                (fragment as DiscoveryFragment).openLoginScreen()
            }
        }
    }
    companion object {
        const val IS_FULFILLMENT = "fulfillment"
    }
}
