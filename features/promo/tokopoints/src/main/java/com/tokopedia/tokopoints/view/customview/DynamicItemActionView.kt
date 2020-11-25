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
    lateinit var tvFirstLabel: TextView
    lateinit var tvCenterLabel: TextView
    lateinit var tvRightLabel: TextView
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
        tvFirstLabel = view.findViewById(R.id.label_tokopoint2)
        tvCenterLabel = view.findViewById(R.id.label_voucher2)
        tvRightLabel = view.findViewById(R.id.label_tokomember2)
        notifFirstLayout = view.findViewById(R.id.notif_tokopoint)
        notifCenterLayout = view.findViewById(R.id.notif_voucher)
        notifRightLayout = view.findViewById(R.id.notif_tokomember)
        dividerOne = view.findViewById(R.id.divider1)
        dividerTwo = view.findViewById(R.id.divider2)
        addView(view)
    }

    fun setLayoutText(title: String, position: Int) {
        when (position) {
            FIRST_TAB -> tvFirstLayout.text = title
            SECOND_TAB -> tvCenterLayout.text = title
            else -> tvRightLayout.text = title
        }
    }

    fun setLayoutLabel(label: String, position: Int) {
        when (position) {
            FIRST_TAB -> {
                tvFirstLabel.show()
                tvFirstLabel.text = label
            }
            SECOND_TAB -> {
                tvCenterLabel.show()
                tvCenterLabel.text = label
            }
            else -> {
                tvRightLabel.show()
                tvRightLabel.text = label
            }
        }
    }

    fun setLayoutIcon(imgUrl: String, position: Int) {
        when (position) {
            FIRST_TAB -> ivFirstLayout.loadImage(imgUrl)
            SECOND_TAB -> ivCenterLayout.loadImage(imgUrl)
            else -> ivRightLayout.loadImage(imgUrl)
        }
    }

    fun setLayoutVisibility(visibility: Int, position: Int) {
        when (position) {
            FIRST_TAB -> holderFirstLayout.visibility = visibility
            SECOND_TAB -> holderCenterLayout.visibility = visibility
            else -> holderRightLayout.visibility = visibility
        }
    }

    fun setLayoutNotification(notif: String, position: Int) {
        when (position) {
            FIRST_TAB -> {
                notifFirstLayout.show()
                notifFirstLayout.setNotification(notif, NotificationUnify.NONE_TYPE, NotificationUnify.COLOR_PRIMARY)
            }
            SECOND_TAB -> {
                notifCenterLayout.show()
                notifCenterLayout.setNotification(notif, NotificationUnify.NONE_TYPE, NotificationUnify.COLOR_PRIMARY)
            }
            else -> {
                notifRightLayout.show()
                notifRightLayout.setNotification(notif, NotificationUnify.NONE_TYPE, NotificationUnify.COLOR_PRIMARY)
            }
        }
    }

    fun hideNotification(position: Int) {
        when (position) {
            FIRST_TAB -> notifFirstLayout.hide()
            SECOND_TAB -> notifCenterLayout.hide()
            else -> notifRightLayout.hide()
        }
    }

    fun setLayoutClicklistener(applink: String?, text: String?, position: Int) {
        when (position) {
            FIRST_TAB -> notifFirstLayout.hide()
            SECOND_TAB -> notifCenterLayout.hide()
            else ->
                notifRightLayout.hide()
        }
        RouteManager.route(context, applink)
        text?.let {
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_CLICK_DYNAMICITEM,
                    text)
        }
    }

    fun setVisibilityDivider(visibility: Int, position: Int) {
        when (position) {
            1 -> dividerOne.visibility = visibility
            else -> dividerTwo.visibility = visibility
        }
    }

    companion object {
        const val FIRST_TAB = 0
        const val SECOND_TAB = 1
    }

}