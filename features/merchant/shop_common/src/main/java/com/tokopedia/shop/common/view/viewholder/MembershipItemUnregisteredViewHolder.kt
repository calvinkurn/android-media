package com.tokopedia.shop.common.view.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.stampprogress.InfoMessage
import com.tokopedia.shop.common.view.BaseMembershipViewHolder

class MembershipItemUnregisteredViewHolder(private val view: View) : BaseMembershipViewHolder<InfoMessage>(view) {

    companion object {
        private const val URL_IMG_BG_MEMBERSHIP = "https://ecs7.tokopedia.net/img/android/membership/bg_membership_banner.png"
    }

    private var bgImgRegistration: ImageView = view.findViewById(R.id.bg_image_register)
    private var txtTitleRegistration: TextView = view.findViewById(R.id.txt_register_title)
    private var txtButtonRegistration: TextView = view.findViewById(R.id.btn_register_membership)

    override fun bind(element: InfoMessage) {
        ImageHandler.LoadImage(bgImgRegistration, URL_IMG_BG_MEMBERSHIP)
        txtTitleRegistration.text = element.title
        txtButtonRegistration.text = element.membershipCta.text
        txtButtonRegistration.setOnClickListener {
            RouteManager.route(view.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, element.membershipCta.url))
        }
    }
}