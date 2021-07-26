package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.graphics.drawable.BitmapDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.*
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderActionWidgetFollowButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.util.TextBaselineSpanAdjuster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx


class ShopActionButtonWidgetFollowButtonComponentViewHolder(
        itemView: View,
        private val shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel,
        private val listener: Listener
) : AbstractViewHolder<ShopHeaderActionWidgetFollowButtonComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_action_button_widget_follow_button_component
    }

    interface Listener {
        fun onImpressionVoucherFollowUnFollowShop()
        fun onClickFollowUnFollowButton(
                componentModel: ShopHeaderButtonComponentUiModel,
                shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel
        )
        fun onImpressionFollowButtonComponent(
                componentModel: ShopHeaderButtonComponentUiModel,
                shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel
        )
    }

    private val buttonFollow: UnifyButton? = itemView.findViewById(R.id.button_shop_follow)

    init {
        val lp = itemView.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            val flexboxLp = lp as FlexboxLayoutManager.LayoutParams
            flexboxLp.flexGrow = 1.0f
        }
    }

    override fun bind(model: ShopHeaderActionWidgetFollowButtonComponentUiModel) {
        buttonFollow?.apply {
            val isShowLoading = model.isButtonLoading
            isLoading = isShowLoading
            if (!isShowLoading)
                text = model.label
            setDrawableLeft(this, model.leftDrawableUrl)
            buttonVariant = UnifyButton.Variant.GHOST
            val isFollowing = model.isFollowing
            buttonType = UnifyButton.Type.ALTERNATE.takeIf { isFollowing } ?: UnifyButton.Type.MAIN
            setOnClickListener {
                if (!isLoading)
                    listener.onClickFollowUnFollowButton(
                            model,
                            shopHeaderWidgetUiModel
                    )
            }
            if(text.isNotEmpty()) {
                addOnImpressionListener(model) {
                    listener.onImpressionFollowButtonComponent(
                            model,
                            shopHeaderWidgetUiModel
                    )
                }
            }
        }
    }

    private fun setDrawableLeft(button: UnifyButton, leftDrawableUrl: String) {
        if (leftDrawableUrl.isNotBlank()) {
            convertUrlToBitmapAndLoadImage(
                    itemView.context,
                    leftDrawableUrl,
                    16.toPx()
            ){
                try {
                    val drawableImage = BitmapDrawable(itemView.resources, it)
                    val left = 0
                    val top = -5
                    val right = drawableImage.intrinsicWidth + 5
                    val bottom = drawableImage.intrinsicHeight
                    drawableImage.setBounds(left, top, right, bottom)
                    val spannableString = SpannableString("   ${button.text}")
                    val imageSpan = ImageSpan(drawableImage)
                    spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val textAscentMultiplier = 0.15
                    spannableString.setSpan(
                            TextBaselineSpanAdjuster(textAscentMultiplier),
                            0,
                            spannableString.length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    button.text = spannableString
                    listener.onImpressionVoucherFollowUnFollowShop()
                }catch (e: Throwable){}
            }
        } else {
            removeCompoundDrawableFollowButton()
        }
    }

    private fun removeCompoundDrawableFollowButton() {
        if (!buttonFollow?.compoundDrawables.isNullOrEmpty()) {
            buttonFollow?.removeDrawable()
        }
    }

}