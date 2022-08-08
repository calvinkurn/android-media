package com.tokopedia.tokopoints.notification.view

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.tokopoints.notification.R
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.tokopoints.notification.model.PopupNotification
import com.tokopedia.tokopoints.notification.utils.AnalyticsTrackerUtil

class TokoPointsPopupNotificationBottomSheet:BottomSheets() {
    var mData:PopupNotification?=null
    private var couponTitle:String?=null

    override fun getLayoutResourceId(): Int {
        return R.layout.tp_layout_popup_notification
    }

    override fun initView(view: View?) {
        val count = view?.findViewById<TextView>(R.id.text_quota_count)
        val title = view?.findViewById<TextView>(R.id.text_title)
        val desc = view?.findViewById<TextView>(R.id.text_desc)
        val notes = view?.findViewById<TextView>(R.id.text_notes)
        val sender = view?.findViewById<TextView>(R.id.text_sender)
        val banner = view?.findViewById<ImageView>(R.id.img_banner)
        val action = view?.findViewById<Button>(R.id.button_action)

        count?.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(
            count.context, R.drawable.ic_tp_flash), null, null, null)

        action?.text=mData?.buttonText
        if(mData?.catalog==null || TextUtils.isEmpty(mData?.catalog?.title)){
            couponTitle = mData?.title
            title?.gravity = Gravity.CENTER_HORIZONTAL
        } else {
            couponTitle = "${mData?.catalog?.title} ${mData?.catalog?.subTitle}"
        }
        title?.text=couponTitle

        if (!TextUtils.isEmpty(mData?.text)) {
            desc?.visibility = View.VISIBLE
            desc?.text = mData?.text
        }

        if (!TextUtils.isEmpty(mData?.notes)) {
            notes?.visibility = View.VISIBLE
            notes?.text = "\"${mData?.notes}\""
        }

        if (!TextUtils.isEmpty(mData?.sender)) {
            sender?.visibility = View.VISIBLE
            sender?.text = "-${mData?.sender}"
        }

        if (mData?.catalog == null || mData?.catalog?.expired == null || mData?.catalog?.expired!!.isEmpty()) {
            count?.visibility = View.GONE
        } else {
            count?.visibility = View.VISIBLE
            count?.text = mData?.catalog?.expired
        }

        if (mData?.imageURL != null && mData?.imageURL!!.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(banner?.context, banner, mData?.imageURL)
        } else {
            if (mData?.catalog != null && mData?.catalog?.imageUrlMobile != null && mData?.catalog!!.imageUrlMobile!!.isNotEmpty()) {
                ImageHandler.loadImageFitCenter(banner?.context,
                    banner,
                    mData?.catalog?.imageUrlMobile)
            }
        }

        action?.setOnClickListener { view1->
            RouteManager.route(action.context,mData?.appLink)
            dismiss()
            AnalyticsTrackerUtil.sendEvent(requireContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.POPUP_TERIMA_HADIAH,
                AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN_KUPON,
                couponTitle!!)
        }
        updateHeight()
    }

    override fun title(): String? {
        return mData?.title
    }

    fun setData(data:PopupNotification){this.mData=data}

    override fun onCloseButtonClick() {
        super.onCloseButtonClick()
        AnalyticsTrackerUtil.sendEvent(
            requireContext(),
            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
            AnalyticsTrackerUtil.CategoryKeys.POPUP_TERIMA_HADIAH,
            AnalyticsTrackerUtil.ActionKeys.CLICK_CLOSE_BUTTON,
            couponTitle!!
        )
    }
}

