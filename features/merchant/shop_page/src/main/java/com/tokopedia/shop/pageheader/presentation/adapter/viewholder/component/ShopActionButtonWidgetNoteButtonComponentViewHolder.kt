package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.graphics.drawable.BitmapDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.convertUrlToBitmapAndLoadImage
import com.tokopedia.shop.common.util.removeDrawable
import com.tokopedia.shop.databinding.LayoutShopActionButtonWidgetNoteButtonComponentBinding
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding


class ShopActionButtonWidgetNoteButtonComponentViewHolder(
        itemView: View,
        private val listener: Listener
) : AbstractViewHolder<ShopHeaderButtonComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_action_button_widget_note_button_component
        private const val BUTTON_SIZE_FOLD_ABLE = 4
        private const val BUTTON_WIDTH_FOLD_ABLE = 24
        private const val BUTTON_HEIGHT_FOLD_ABLE = 24
        private const val IMAGE_SIZE_FOLD_ABLE = 16
    }

    private val viewBinding: LayoutShopActionButtonWidgetNoteButtonComponentBinding? by viewBinding()
    private val imageButtonShopNote: UnifyButton? = viewBinding?.imageButtonShopNote

    override fun bind(model: ShopHeaderButtonComponentUiModel) {
        if(ShopUtil.isFoldableAndHorizontalScreen){
            imageButtonShopNote?.buttonSize = BUTTON_SIZE_FOLD_ABLE
            imageButtonShopNote?.layoutParams?.height = BUTTON_WIDTH_FOLD_ABLE.toPx()
            imageButtonShopNote?.layoutParams?.width = BUTTON_HEIGHT_FOLD_ABLE.toPx()
        }
        imageButtonShopNote?.setOnClickListener {
            listener.onClickNoteButton(model.link)
        }
        imageButtonShopNote?.let {
            setDrawable(it,model.icon)
        }
    }

    private fun setDrawable(button: UnifyButton, url: String) {
        if (url.isNotBlank()) {
            val imageSize = if (ShopUtil.isFoldableAndHorizontalScreen) {
                IMAGE_SIZE_FOLD_ABLE
            } else {
                itemView.context.resources.getInteger(R.integer.header_button_shop_note_icon_size)
            }
            convertUrlToBitmapAndLoadImage(
                itemView.context,
                url,
                imageSize.toPx()
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