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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationTracker
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.state.BottomSheetType
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationFilterListener
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.fragment.ProductCardListDialog
import com.tokopedia.notifcenter.presentation.fragment.ProductStockHandlerDialog
import com.tokopedia.notifcenter.util.endLess
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.notifcenter.presentation.fragment.NotificationLongerTextDialog.Companion.createInstance as LongerTextDialog

abstract class BaseNotificationFragment : BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        NotificationItemListener,
        NotificationFilterListener {

    private var longerTextDialog: BottomSheetDialogFragment? = null

    @Inject lateinit var userSession: UserSessionInterface

    abstract fun bottomFilterView(): FloatingButtonUnify?
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
        bottomFilterView()?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
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
        super.getRecyclerView(view)?.endLess({
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
            if (markAllReadCounter == 0L) it?.hide()
            else it?.show()
        }
    }

    override fun getAnalytic(): NotificationUpdateAnalytics {
        return NotificationUpdateAnalytics()
    }

    override fun showNotificationDetail(bottomSheet: BottomSheetType, element: NotificationItemViewBean) {
        when (bottomSheet) {
            is BottomSheetType.LongerContent -> showLongerContent(element)
//            is BottomSheetType.ProductCheckout -> showProductCheckout(element) (Disabled)
            is BottomSheetType.StockHandler -> showStockHandlerDialog(element)
            else -> {}
        }
    }

    private fun showStockHandlerDialog(element: NotificationItemViewBean) {
        element.getAtcProduct()?.let {
            if (it.stock < 1) {
                ProductStockHandlerDialog(
                        element = element,
                        listener = this
                ).show(childFragmentManager, TAG_PRODUCT_STOCK)
            }
        }
    }

    private fun showProductCheckout(element: NotificationItemViewBean) {
        context?.let {
            ProductCardListDialog(
                    element = element,
                    listener = this
            ).show(childFragmentManager, TAG_PRODUCT_LIST)
        }
    }

    private fun showLongerContent(element: NotificationItemViewBean) {
        if (longerTextDialog?.isAdded == true) return

        val bundle = Bundle().apply {
            putString(PARAM_CONTENT_IMAGE, element.contentUrl)
            putString(PARAM_CONTENT_IMAGE_TYPE, element.typeLink.toString())
            putString(PARAM_CTA_APPLINK, element.appLink)
            putString(PARAM_CONTENT_TEXT, element.bodyHtml)
            putString(PARAM_CONTENT_TITLE, element.title)
            putString(PARAM_BUTTON_TEXT, element.btnText)
            putString(PARAM_TEMPLATE_KEY, element.templateKey)
            putString(PARAM_NOTIF_ID, element.notificationId)
        }

        if (longerTextDialog == null) {
            longerTextDialog = LongerTextDialog(bundle)
        } else {
            longerTextDialog?.arguments = bundle
        }

        if (childFragmentManager.findFragmentByTag(TAG_LONGER_TEXT) == null) {
            longerTextDialog?.show(childFragmentManager, TAG_LONGER_TEXT)
        }
    }

    override fun onSuccessAddToCart(message: String) {
        val onActionClick = View.OnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
        }

        view?.let {
            val toasterPosition = resources.getInteger(R.integer.toaster_y_position)
            Toaster.toasterCustomBottomHeight = toasterPosition
            Toaster.make(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.notifcenter_title_view),
                    onActionClick
            )
        }
    }

    override fun showMessageError(e: Throwable?) {
        view?.let {
            val errorMessage = ErrorHandler.getErrorMessage(it.context, e)
            showToastMessageError(errorMessage)
        }
    }

    protected fun showToastMessageError(message: String) {
        view?.let { Toaster.showError(it, message, Snackbar.LENGTH_LONG) }
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
    override fun addProductToCart(product: ProductData, onSuccessAddToCart: (DataModel) -> Unit) {}

    override fun initInjector() {
        (activity as NotificationActivity)
                .notificationComponent
                .inject(this)
    }

    override fun onItemMultipleStockHandlerClick(notification: NotificationItemViewBean) {}

    companion object {
        private const val TAG_PRODUCT_STOCK = "Product Stock Handler"
        private const val TAG_PRODUCT_LIST = "Product List Card"
        private const val TAG_LONGER_TEXT = "Longer Text Bottom Sheet"

        const val PARAM_CONTENT_TITLE = "content title"
        const val PARAM_CONTENT_TEXT = "content text"
        const val PARAM_CONTENT_IMAGE = "content image"
        const val PARAM_CONTENT_IMAGE_TYPE = "content image type"
        const val PARAM_CTA_APPLINK = "cta applink"
        const val PARAM_BUTTON_TEXT = "button text"
        const val PARAM_TEMPLATE_KEY = "template key"
        const val PARAM_NOTIF_ID = "notification id"
    }
}