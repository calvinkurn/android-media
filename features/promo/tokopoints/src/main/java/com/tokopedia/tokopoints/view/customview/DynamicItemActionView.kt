package com.tokopedia.tokopoints.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.unifycomponents.NotificationUnify

class DynamicItemActionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var holderFirstLayout: LinearLayout
    lateinit var holderCenterLayout: LinearLayout
    lateinit var holderRightLayout: LinearLayout
    lateinit var ivFirstLayout: ImageView
    lateinit var ivCenterLayout: ImageView
    lateinit var ivRightLayout: ImageView
    lateinit var tvFirstLayout: TextView
    lateinit var tvCenterLayout: TextView
    lateinit var tvRightLayout: TextView
    lateinit var notifFirstLayout: NotificationUnify
    lateinit var notifCenterLayout: NotificationUnify
    lateinit var notifRightLayout: NotificationUnify
    lateinit var dividerOne: View
    lateinit var dividerTwo: View

    init {
        setUpUI()
    }

    private fun setUpUI() {
        val view = View.inflate(context, R.layout.tp_home_dynamic_action, null)
        holderFirstLayout = view.findViewById(R.id.holder_tokopoint)
        holderCenterLayout = view.findViewById(R.id.holder_coupon)
        holderRightLayout = view.findViewById(R.id.holder_tokomember)
        ivFirstLayout = view.findViewById(R.id.image_tokopoint)
        ivCenterLayout = view.findViewById(R.id.image_voucher)
        ivRightLayout = view.findViewById(R.id.image_tokomember)
        tvFirstLayout = view.findViewById(R.id.label_tokopoint1)
        tvCenterLayout = view.findViewById(R.id.label_voucher)
        tvRightLayout = view.findViewById(R.id.label_tokomember)
        notifFirstLayout = view.findViewById(R.id.notif_tokopoint)
        notifCenterLayout = view.findViewById(R.id.notif_voucher)
        notifRightLayout = view.findViewById(R.id.notif_tokomember)
        dividerOne = view.findViewById(R.id.divider1)
        dividerTwo = view.findViewById(R.id.divider2)
        addView(view)
    }

    fun setFirstLayoutText(title: String) {
        tvFirstLayout.text = title
    }

    fun setFirstLayoutIcon(imgUrl: String) {
        ivFirstLayout.loadImage(imgUrl)
    }

    fun setFirstLayoutNotification(notif: String) {
        notifFirstLayout.show()
        notifFirstLayout.setNotification(notif, NotificationUnify.TEXT_TYPE, NotificationUnify.COLOR_PRIMARY)
    }

    fun setFirstLayoutVisibility(visibility: Int) {
        holderFirstLayout.visibility = visibility
    }

    fun setLayoutClickListener(applink: String?, text: String?) {
        RouteManager.route(context, applink)
        notifFirstLayout.hide()

        text?.let {
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_CLICK_DYNAMICITEM,
                    text)
        }
    }

    fun setCenterLayoutText(title: String) {
        tvCenterLayout.text = title
    }

    fun setCenterLayoutIcon(imgUrl: String) {
        ivCenterLayout.loadImage(imgUrl)
    }

    fun setCenterLayoutNotification(notif: String) {
        notifCenterLayout.show()
        notifCenterLayout.setNotification(notif, NotificationUnify.TEXT_TYPE, NotificationUnify.COLOR_PRIMARY)

    }

    fun setCenterLayoutVisibility(visibility: Int) {
        holderCenterLayout.visibility = visibility
    }

    fun setCenterLayoutClickListener(applink: String?, text: String?) {
        RouteManager.route(context, applink)
        notifCenterLayout.hide()
        text?.let {
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_CLICK_DYNAMICITEM,
                    text)
        }
    }

    fun setRightLayoutText(title: String) {
        tvRightLayout.text = title
    }

    fun setRightLayoutIcon(imgUrl: String) {
        ivRightLayout.loadImage(imgUrl)
    }

    fun setRightLayoutNotification(notif: String) {
        notifRightLayout.show()
        notifRightLayout.setNotification(notif, NotificationUnify.TEXT_TYPE, NotificationUnify.COLOR_PRIMARY)

    }

    fun setRightLayoutVisibility(visibility: Int) {
        holderRightLayout.visibility = visibility
    }

    fun setRightLayoutClickListener(applink: String?, text: String?) {
        RouteManager.route(context, applink)
        notifRightLayout.hide()
        text?.let {
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_CLICK_DYNAMICITEM,
                    text)
        }
    }

    fun setVisibilityDividerOne(visibility: Int) {
        dividerOne.visibility = visibility
    }

    fun setVisibilityDividerTwo(visibility: Int) {
        dividerTwo.visibility = visibility
    }

}