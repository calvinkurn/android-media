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

    fun setLayoutText(title: String, id: Int) {
        when (id) {
            TOKOMEMBER -> tvTLLayout.text = title
            TOPQUEST -> tvBLLayout.text = title
            KUPON -> tvBRLayout.text = title
            BBO -> tvTRLayout.text = title
        }
    }

    fun setLayoutLabel(label: String, id: Int) {
        when (id) {
            TOKOMEMBER -> {
                tvTLLabel.show()
                tvTLLabel.text = label
            }
            TOPQUEST -> {
                tvBLLabel.show()
                tvBLLabel.text = label
            }
            KUPON -> {
                tvBRLabel.show()
                tvBRLabel.text = label
            }
            BBO -> {
                tvTRLabel.show()
                tvTRLabel.text = label
            }
        }
    }

    fun setLayoutIcon(imgUrl: String, id: Int) {
        when (id) {
            TOKOMEMBER -> ivTLLayout.loadImage(imgUrl)
            TOPQUEST -> ivBLLayout.loadImage(imgUrl)
            KUPON -> ivBRLayout.loadImage(imgUrl)
            BBO -> ivTRLayout.loadImage(imgUrl)
        }
    }

    fun setLayoutVisibility(visibility: Int, id: Int) {
        when (id) {
            TOKOMEMBER -> holderTLLayout.visibility = visibility
            TOPQUEST -> holderBLLayout.visibility = visibility
            KUPON -> holderBRLayout.visibility = visibility
            BBO -> holderTRLayout.visibility = visibility
        }
    }

    fun setLayoutNotification(notif: String, id: Int) {
        when (id) {
            TOKOMEMBER -> {
                notifTLLayout.show()
                notifTLLayout.setNotification(
                    notif,
                    NotificationUnify.NONE_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            }
            TOPQUEST -> {
                notifBLLayout.show()
                notifBLLayout.setNotification(
                    notif,
                    NotificationUnify.NONE_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            }
            KUPON -> {
                notifBRLayout.show()
                notifBRLayout.setNotification(
                    notif,
                    NotificationUnify.NONE_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            }
            BBO -> {
                notifTRLayout.show()
                notifTRLayout.setNotification(
                    notif,
                    NotificationUnify.NONE_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            }
        }
    }

    fun hideNotification(id: Int) {
        when (id) {
            TOKOMEMBER -> notifTLLayout.hide()
            TOPQUEST -> notifBLLayout.hide()
            KUPON -> notifBRLayout.hide()
            BBO -> notifTRLayout.hide()
        }
    }

    fun setLayoutClicklistener(applink: String?, text: String?, id: Int) {
        hideNotification(id)
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
        const val TOKOMEMBER = 1
        const val TOPQUEST = 2
        const val KUPON = 3
        const val BBO = 4
    }
}