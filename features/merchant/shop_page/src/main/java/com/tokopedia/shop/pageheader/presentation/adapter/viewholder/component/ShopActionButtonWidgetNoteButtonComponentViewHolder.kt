package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.graphics.drawable.BitmapDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.convertUrlToBitmapAndLoadImage
import com.tokopedia.shop.common.util.removeDrawable
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx


class ShopActionButtonWidgetNoteButtonComponentViewHolder(
        itemView: View,
        private val listener: Listener
) : AbstractViewHolder<ShopHeaderButtonComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_action_button_widget_note_button_component
    }

    private val imageButtonShopNote: UnifyButton? = itemView.findViewById(R.id.image_button_shop_note)

    override fun bind(model: ShopHeaderButtonComponentUiModel) {
        imageButtonShopNote?.setOnClickListener {
            listener.onClickNoteButton(model.link)
        }
        imageButtonShopNote?.let {
            setDrawable(it,model.icon)
        }
    }

    private fun setDrawable(button: UnifyButton, url: String) {
        if (url.isNotBlank()) {
            convertUrlToBitmapAndLoadImage(
                    itemView.context,
                    url,
                    16.toPx()
            ){
                try {
                    val drawableImage = BitmapDrawable(itemView.resources, it)
                    val right = drawableImage.intrinsicWidth
                    val bottom = drawableImage.intrinsicHeight
                    drawableImage.setBounds(0, 0, right, bottom)
                    val spannableString = SpannableString(" ")
                    val imageSpan = ImageSpan(drawableImage)
                    spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    button.text = spannableString
                    button.setPadding(6.toDp(),4.toDp(),4.toDp(),6.toDp())
                }catch (e: Throwable){}
            }
        } else {
            removeCompoundDrawableButton()
        }
    }

    private fun removeCompoundDrawableButton() {
        if (!imageButtonShopNote?.compoundDrawables.isNullOrEmpty()) {
            imageButtonShopNote?.removeDrawable()
        }
    }

    interface Listener {
        fun onClickNoteButton(link: String)
    }

}