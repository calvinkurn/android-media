package com.tokopedia.shop.settings.basicinfo.view.viewholder

import android.text.TextUtils
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditProfilePictureModel
import kotlinx.android.synthetic.main.item_shop_edit_profile_picture.view.*

class ShopEditProfilePictureViewHolder(
        view: View,
        private val listener: ShopEditProfilePictureListener?
): AbstractViewHolder<ShopEditProfilePictureModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_edit_profile_picture
        val POSITION = 0
    }

    override fun bind(model: ShopEditProfilePictureModel) {
        updatePhotoUI(model.savedLocalImageUrl, model.logoUrl)
        setupShopAvatar()
    }

    private fun setupShopAvatar() {
        itemView.imageAvatar.setOnClickListener { listener?.onImageChangeAvatarClick() }
        itemView.textChangeAvatar.setOnClickListener { listener?.onTextChangeAvatarClick() }
    }

    private fun updatePhotoUI(savedLocalImageUrl: String?, logoUrl: String?) {
        if (TextUtils.isEmpty(savedLocalImageUrl)) {
            if (TextUtils.isEmpty(logoUrl)) {
                itemView.imageAvatar.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.ic_shop_edit_avatar))
            } else {
                ImageHandler.LoadImage(itemView.imageAvatar, logoUrl)
            }
        } else {
            ImageHandler.LoadImage(itemView.imageAvatar, savedLocalImageUrl)
        }
    }

    interface ShopEditProfilePictureListener {
        fun onImageChangeAvatarClick()
        fun onTextChangeAvatarClick()
    }
}