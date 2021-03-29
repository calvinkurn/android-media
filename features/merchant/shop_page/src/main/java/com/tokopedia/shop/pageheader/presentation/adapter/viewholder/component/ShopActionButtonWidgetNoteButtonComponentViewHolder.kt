package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx


class ShopActionButtonWidgetNoteButtonComponentViewHolder(
        itemView: View
) : AbstractViewHolder<ShopHeaderButtonComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_action_button_widget_note_button_component
    }

    private val imageButtonShopNote: UnifyButton? = itemView.findViewById(R.id.image_button_shop_note)

    override fun bind(model: ShopHeaderButtonComponentUiModel) {
        Glide.with(itemView.context)
                .asBitmap()
                .load(model.icon)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        val drawable = BitmapDrawable(itemView.context.resources, Bitmap.createScaledBitmap(resource,24,24,false))
                        imageButtonShopNote?.setPadding(6.toPx(),0,0, 0)
                        imageButtonShopNote?.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
    }

}