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

    lateinit var holderTLLayout: LinearLayout
    lateinit var tvTLLabel: TextView
    lateinit var ivTLLayout: ImageView
    lateinit var tvTLLayout: TextView
    lateinit var notifTLLayout: NotificationUnify
    lateinit var holderTRLayout: LinearLayout
    lateinit var tvTRLabel: TextView
    lateinit var ivTRLayout: ImageView
    lateinit var tvTRLayout: TextView
    lateinit var notifTRLayout: NotificationUnify
    lateinit var holderBLLayout: LinearLayout
    lateinit var ivBLLayout: ImageView
    lateinit var tvBLLayout: TextView
    lateinit var tvBLLabel: TextView
    lateinit var notifBLLayout: NotificationUnify
    lateinit var tvBRLabel: TextView
    lateinit var holderBRLayout: LinearLayout
    lateinit var tvBRLayout: TextView
    lateinit var ivBRLayout: ImageView
    lateinit var notifBRLayout: NotificationUnify

    init {
        setUpUI()
    }

    private fun setUpUI() {
        val view = View.inflate(context, R.layout.tp_home_dynamic_action, null)
        holderTLLayout = view.findViewById(R.id.holder_tokomember)
        ivTLLayout = view.findViewById(R.id.image_tokomember)
        tvTLLayout = view.findViewById(R.id.label_tokomember)
        tvTLLabel = view.findViewById(R.id.label_tokomember2)
        notifTLLayout = view.findViewById(R.id.notif_tokomember)

        holderTRLayout = view.findViewById(R.id.holder_bbo)
        ivTRLayout = view.findViewById(R.id.image_bbo)
        tvTRLayout = view.findViewById(R.id.label_bbo)
        tvTRLabel = view.findViewById(R.id.label_bbo2)
        notifTRLayout = view.findViewById(R.id.notif_bbo)

        holderBRLayout = view.findViewById(R.id.holder_tokopoint)
        ivBRLayout = view.findViewById(R.id.image_tokopoint)
        tvBRLayout = view.findViewById(R.id.label_tokopoint1)
        tvBRLabel = view.findViewById(R.id.label_tokopoint2)
        notifBRLayout = view.findViewById(R.id.notif_tokopoint)

        holderBLLayout = view.findViewById(R.id.holder_topquest)
        ivBLLayout = view.findViewById(R.id.image_topquest)
        tvBLLayout = view.findViewById(R.id.label_topquest)
        tvBLLabel = view.findViewById(R.id.label_topquest2)
        notifBLLayout = view.findViewById(R.id.notif_topquest)
        addView(view)
    }

    fun setLayoutText(title: String, position: Int) {
        when (position) {
            FIRST_TAB -> tvTLLayout.text = title
            SECOND_TAB -> tvBLLayout.text = title
            THIRD_TAB -> tvBRLayout.text = title
            else ->tvTRLayout.text = title
        }
    }

    fun setLayoutLabel(label: String, position: Int) {
        when (position) {
            FIRST_TAB -> {
                tvTLLabel.show()
                tvTLLabel.text = label
            }
            SECOND_TAB -> {
                tvBLLabel.show()
                tvBLLabel.text = label
            }
            THIRD_TAB -> {
                tvBRLabel.show()
                tvBRLabel.text = label
            }
            else ->{
                tvTRLabel.show()
                tvTRLabel.text = label
            }
        }
    }

    fun setLayoutIcon(imgUrl: String, position: Int) {
        when (position) {
            FIRST_TAB -> ivTLLayout.loadImage(imgUrl)
            SECOND_TAB -> ivBLLayout.loadImage(imgUrl)
            THIRD_TAB -> ivBRLayout.loadImage(imgUrl)
            else -> ivTRLayout.loadImage(imgUrl)
        }
    }

    fun setLayoutVisibility(visibility: Int, position: Int) {
        when (position) {
            FIRST_TAB -> holderTLLayout.visibility = visibility
            SECOND_TAB -> holderBLLayout.visibility = visibility
            THIRD_TAB -> holderBRLayout.visibility = visibility
            else -> holderTRLayout.visibility = visibility
        }
    }

    fun setLayoutNotification(notif: String, position: Int) {
        when (position) {
            FIRST_TAB -> {
                notifTLLayout.show()
                notifTLLayout.setNotification(
                    notif,
                    NotificationUnify.NONE_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            }
            SECOND_TAB -> {
                notifBLLayout.show()
                notifBLLayout.setNotification(
                    notif,
                    NotificationUnify.NONE_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            }
            THIRD_TAB -> {
                notifBRLayout.show()
                notifBRLayout.setNotification(
                    notif,
                    NotificationUnify.NONE_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            }
            else ->{
                notifTRLayout.show()
                notifTRLayout.setNotification(
                    notif,
                    NotificationUnify.NONE_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            }
        }
    }

    fun hideNotification(position: Int) {
        when (position) {
            FIRST_TAB -> notifTLLayout.hide()
            SECOND_TAB -> notifBLLayout.hide()
            THIRD_TAB -> notifBRLayout.hide()
            else -> notifTRLayout.hide()
        }
    }

    fun setLayoutClicklistener(applink: String?, text: String?, position: Int) {
        hideNotification(position)
        RouteManager.route(context, applink)
        text?.let {
            AnalyticsTrackerUtil.sendEvent(
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_CLICK_DYNAMICITEM,
                text,
                AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
            )
        }
    }

    companion object {
        const val FIRST_TAB = 0
        const val SECOND_TAB = 1
        const val THIRD_TAB = 2
    }

}