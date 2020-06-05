package com.tokopedia.tokopoints.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.LobDetails
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.unifycomponents.BottomSheetUnify

class StartPurchaseBottomSheet : BottomSheetUnify() {
    var mLobDetails: LobDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val displaymetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        val screenHeight = displaymetrics.heightPixels
        customPeekHeight=screenHeight
        showCloseIcon = true
        setCloseClickListener { onCloseButtonClick() }
        mLobDetails?.title?.let { setTitle(it) }
        val view = View.inflate(requireContext(), R.layout.tp_bottosheet_container, null)
        setChild(view)
        initView(view)
    }

    fun initView(view: View) {
        if (mLobDetails == null) {
            dismiss()
            return
        }
        val desc = view.findViewById<TextView>(R.id.text_description)
        if (!TextUtils.isEmpty(mLobDetails?.description)) {
            desc.text = mLobDetails?.description
        }
        val linearLayout = view.findViewById<LinearLayout>(R.id.container_lob)
        val inflater = LayoutInflater.from(view.context)
        if (mLobDetails?.lobs != null) {
            for (item in mLobDetails?.lobs!!) {
                val itemView = inflater.inflate(R.layout.tp_bottomsheet_lob_item, null, false)
                val title = itemView.findViewById<TextView>(R.id.text_title)
                title.text = item.text
                linearLayout.addView(itemView)
                itemView.setOnClickListener { view1: View? ->
                    val uri = if (item.appLink.isEmpty()) item.url else item.appLink
                    if (uri.startsWith(CommonConstant.TickerMapKeys.TOKOPEDIA)) {
                        RouteManager.route(itemView.context, uri)
                    } else {
                        RouteManager.route(itemView.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, uri))
                    }
                }
                val icon = itemView?.findViewById<ImageView>(R.id.img_lob)
                when {
                    item?.text.equals("Beli", ignoreCase = true) -> {
                        icon?.setImageDrawable(MethodChecker.getDrawable(activity, R.drawable.ic_tp_buy))
                        AnalyticsTrackerUtil.sendEvent(view.context,
                                AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                                AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_BELI,
                                "")
                    }
                    item.text.equals("Kereta", ignoreCase = true) -> {
                        icon?.setImageDrawable(MethodChecker.getDrawable(activity, R.drawable.ic_tp_train))
                        AnalyticsTrackerUtil.sendEvent(view.context,
                                AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                                AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_KARETA,
                                "")
                    }
                    item.text.equals("Pesawat", ignoreCase = true) -> {
                        icon?.setImageDrawable(MethodChecker.getDrawable(activity, R.drawable.ic_tp_pesawat))
                        AnalyticsTrackerUtil.sendEvent(view.context,
                                AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                                AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_PESAWAT,
                                "")
                    }
                    item.text.equals("Bayar", ignoreCase = true) -> {
                        icon?.setImageDrawable(MethodChecker.getDrawable(activity, R.drawable.ic_tp_bayar))
                        AnalyticsTrackerUtil.sendEvent(view.context,
                                AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                                AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_BAYAR,
                                "")
                    }
                }
            }
        }
    }

    fun setData(lobDetails: LobDetails?) {
        mLobDetails = lobDetails
    }

    protected fun onCloseButtonClick() {
        AnalyticsTrackerUtil.sendEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                AnalyticsTrackerUtil.ActionKeys.CLICK_CLOSE_BUTTON,
                AnalyticsTrackerUtil.EventKeys.TOKOPOINTS_LUCKY_EGG_CLOSE_LABEL)
    }
}