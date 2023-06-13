package com.tokopedia.shop.common.view.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.GO_TO_MEMBERSHIP_REGISTER
import com.tokopedia.shop.common.data.viewmodel.ItemUnregisteredViewModel
import com.tokopedia.shop.common.view.BaseMembershipViewHolder
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter

class MembershipItemUnregisteredViewHolder(view: View, private val listener: MembershipStampAdapter.MembershipStampAdapterListener) : BaseMembershipViewHolder<ItemUnregisteredViewModel>(view) {

    private var bgImgRegistration: ImageView = view.findViewById(R.id.bg_image_register)
    private var txtTitleRegistration: TextView = view.findViewById(R.id.txt_register_title)
    private var txtButtonRegistration: TextView = view.findViewById(R.id.btn_register_membership)

    companion object {
        private const val URL_IMG_BG_MEMBERSHIP = TokopediaImageUrl.URL_IMG_BG_MEMBERSHIP
    }

    override fun bind(element: ItemUnregisteredViewModel) {
        ImageHandler.LoadImage(bgImgRegistration, URL_IMG_BG_MEMBERSHIP)
        txtTitleRegistration.text = element.bannerTitle
        txtButtonRegistration.text = element.btnText
        txtButtonRegistration.setOnClickListener {
            listener.goToVoucherOrRegister(element.url, GO_TO_MEMBERSHIP_REGISTER)
        }
    }
}
