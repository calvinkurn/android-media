package com.tokopedia.shop.common.view.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.stampprogress.InfoMessage
import com.tokopedia.shop.common.view.BaseMembershipViewHolder
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter

class MembershipItemUnregisteredViewHolder(private val view: View,private val listener: MembershipStampAdapter.MembershipStampAdapterListener) : BaseMembershipViewHolder<InfoMessage>(view) {

    private var bgImgRegistration: ImageView = view.findViewById(R.id.bg_image_register)
    private var txtTitleRegistration: TextView = view.findViewById(R.id.txt_register_title)
    private var txtButtonRegistration: TextView = view.findViewById(R.id.btn_register_membership)

    companion object {
        private const val URL_IMG_BG_MEMBERSHIP = "https://ecs7.tokopedia.net/img/android/membership/bg_membership_banner.png"
    }

    override fun bind(element: InfoMessage) {
        ImageHandler.LoadImage(bgImgRegistration, URL_IMG_BG_MEMBERSHIP)
        txtTitleRegistration.text = element.title
        txtButtonRegistration.text = element.membershipCta.text
        txtButtonRegistration.setOnClickListener {
            listener.goToVoucherOrRegister(element.membershipCta.url)
        }
    }
}