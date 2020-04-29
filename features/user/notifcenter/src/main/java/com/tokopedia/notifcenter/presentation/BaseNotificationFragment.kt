package com.tokopedia.notifcenter.presentation

import android.animation.LayoutTransition
import android.os.Build
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.button.BottomActionView
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationTracker
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.UserInfo
import com.tokopedia.notifcenter.data.state.BottomSheetType
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationFilterListener
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.fragment.NotificationLongerTextDialog
import com.tokopedia.notifcenter.presentation.fragment.NotificationProductCardDialog
import com.tokopedia.notifcenter.presentation.fragment.ProductStockReminderDialog
import com.tokopedia.notifcenter.util.endLess
import com.tokopedia.purchase_platform.common.constant.ATC_AND_BUY
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

abstract class BaseNotificationFragment: BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        NotificationItemListener,
        NotificationFilterListener {

    private lateinit var longerTextDialog: BottomSheetDialogFragment

    @Inject lateinit var userSession: UserSessionInterface

    abstract fun bottomFilterView(): BottomActionView?
    abstract fun analytics(): NotificationTracker

    //last notification id
    protected var cursor = ""

    /*
    * last item of recyclerView;
    * for tracking purpose
    * */
    private var lastListItem = 0

    /*
     * track mark all as read counter
     * counting notification item to as read
     * */
    protected var markAllReadCounter = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //enable transition of filter type on kitkat above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bottomFilterView()?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        }
    }

    protected open fun onSuccessMarkAllRead() {
        markAllReadCounter = 0L
        notifyBottomActionView()
    }

    override fun itemClicked(notification: NotificationItemViewBean, adapterPosition: Int) {
        analytics().trackNotificationClick(notification)
    }

    protected fun updateMarkAllReadCounter() {
        markAllReadCounter -= 1
    }

    protected fun onListLastScroll(view: View) {
        super.getRecyclerView(view).endLess({
            if (it < 0) { // going up
                notifyBottomActionView()
            } else if (it > 0) { // going down
                bottomFilterView()?.hide()
            }
        }, {
            if (it > lastListItem) {
                lastListItem = it
            }
        })
    }

    protected fun notifyBottomActionView() {
        bottomFilterView().let {
            if (markAllReadCounter == 0L) {
                it?.hide()
            } else {
                it?.show()
            }
        }
    }

    override fun getAnalytic(): NotificationUpdateAnalytics {
        return NotificationUpdateAnalytics()
    }

    override fun addProductToCheckout(userInfo: UserInfo, element: NotificationItemViewBean) {
        try {
            val atcAndBuyAction = ATC_AND_BUY
            val needToRefresh = true
            val minimumOrder = "1"
            startActivity(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
                putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, element.getAtcProduct()?.shop?.id.toString())
                putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, element.getAtcProduct()?.shop?.name)
                putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, element.getAtcProduct()?.productId)
                putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, minimumOrder)
                putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, element.getAtcProduct()?.productId)
                putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcAndBuyAction)
                putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needToRefresh)
                putExtra(ApplinkConst.Transaction.EXTRA_REFERENCE, ApplinkConst.NOTIFICATION)
                putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, element.getAtcProduct()?.name)
                putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, element.getAtcProduct()?.price?.toFloat())
                putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
                putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_LABEL, element.getAtcEventLabel())
                putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_ACTION, element.getBuyEventAction())
            })
        } catch (e: Exception) {}
    }

    override fun showNotificationDetail(bottomSheet: BottomSheetType, element: NotificationItemViewBean) {
        when (bottomSheet) {
            is BottomSheetType.LongerContent -> showLongerContent(element)
            is BottomSheetType.ProductCheckout -> showProductCheckout(element)
            is BottomSheetType.StockHandler -> showStockHandlerDialog(element)
        }
    }

    private fun showStockHandlerDialog(element: NotificationItemViewBean) {
        element.getAtcProduct()?.let {
            if (it.stock < 1) {
                context?.let { context ->
                    ProductStockReminderDialog(
                            context = context,
                            fragmentManager = childFragmentManager,
                            userSession = userSession,
                            listener = this
                    ).show(element)
                }
            }
        }
    }

    private fun showProductCheckout(element: NotificationItemViewBean) {
        context?.let {
            NotificationProductCardDialog(
                    context = it,
                    fragmentManager = childFragmentManager,
                    listener = this
            ).show(element)
        }
    }

    private fun showLongerContent(element: NotificationItemViewBean) {
        val bundle = Bundle()

        bundle.putString(PARAM_CONTENT_IMAGE, element.contentUrl)
        bundle.putString(PARAM_CONTENT_IMAGE_TYPE, element.typeLink.toString())
        bundle.putString(PARAM_CTA_APPLINK, element.appLink)
        bundle.putString(PARAM_CONTENT_TEXT, element.bodyHtml)
        bundle.putString(PARAM_CONTENT_TITLE, element.title)
        bundle.putString(PARAM_BUTTON_TEXT, element.btnText)
        bundle.putString(PARAM_TEMPLATE_KEY, element.templateKey)

        if (!::longerTextDialog.isInitialized) {
            longerTextDialog = NotificationLongerTextDialog.createInstance(bundle)
        } else {
            longerTextDialog.arguments = bundle
        }

        if (!longerTextDialog.isAdded) {
            longerTextDialog.show(childFragmentManager, "Longer Text Bottom Sheet")
        }
    }

    protected fun showToastMessageError(message: String) {
        view?.let { Toaster.showError(it, message, Snackbar.LENGTH_LONG) }
    }

    override fun onSuccessReminderStock() {
        view?.let { view ->
            context?.let {
                Toaster.make(
                        view,
                        it.getString(R.string.product_reminder_success),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        it.getString(R.string.notifcenter_btn_title_ok),
                        View.OnClickListener {  }
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        trackScrollListToBottom()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackScrollListToBottom()
    }

    private fun trackScrollListToBottom() {
        if (lastListItem > 0) {
            analytics().trackScrollBottom(lastListItem.toString())
        }
    }

    override fun trackNotificationImpression(element: NotificationItemViewBean) {
        analytics().saveNotificationImpression(element)
    }

    override fun sentFilterAnalytic(analyticData: String) {
        analytics().trackClickFilterRequest(analyticData)
    }

    override fun isHasNotification(): Boolean = false

    //unused method
    override fun onItemClicked(t: Visitable<*>?) = Unit
    override fun getScreenName(): String = ""
    override fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit) {}

    override fun initInjector() {
        (activity as NotificationActivity)
                .notificationComponent
                .inject(this)
    }

    companion object {
        const val PARAM_CONTENT_TITLE = "content title"
        const val PARAM_CONTENT_TEXT = "content text"
        const val PARAM_CONTENT_IMAGE = "content image"
        const val PARAM_CONTENT_IMAGE_TYPE = "content image type"
        const val PARAM_CTA_APPLINK = "cta applink"
        const val PARAM_BUTTON_TEXT = "button text"
        const val PARAM_TEMPLATE_KEY = "template key"
    }
}