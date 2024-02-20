package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.graphics.drawable.BitmapDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.*
import com.tokopedia.shop.databinding.LayoutShopActionButtonWidgetFollowButtonComponentBinding
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderActionWidgetFollowButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTextBaselineSpanAdjuster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder(
    itemView: View,
    private val shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel,
    private val listener: Listener
) : AbstractViewHolder<ShopPageHeaderActionWidgetFollowButtonComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_action_button_widget_follow_button_component
        private const val TOP_BOUND = 5
        private const val RIGHT_BOUND_COUNTER = 5
        private const val TEST_ASCENT_MULTIPLIER = 0.15
        private const val BUTTON_SIZE_FOLD_ABLE = 4

    }

    interface Listener {
        fun onImpressionVoucherFollowUnFollowShop()
        fun onClickFollowUnFollowButton(
            componentModel: ShopPageHeaderButtonComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
        )
        fun onImpressionFollowButtonComponent(
            componentModel: ShopPageHeaderButtonComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
        )
    }

    private val viewBinding: LayoutShopActionButtonWidgetFollowButtonComponentBinding? by viewBinding()
    private val buttonFollow: UnifyButton? = viewBinding?.buttonShopFollow

    init {
        val lp = itemView.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            val flexboxLp = lp as FlexboxLayoutManager.LayoutParams
            flexboxLp.flexGrow = 1.0f
        }
    }

    override fun bind(model: ShopPageHeaderActionWidgetFollowButtonComponentUiModel) {
        val isFollowing = model.isFollowing
        buttonFollow?.apply {
            if(ShopUtil.isFoldable){
                buttonFollow.buttonSize = UnifyButton.Size.SMALL
            }
            val isShowLoading = model.isButtonLoading
            isLoading = isShowLoading
            if (!isShowLoading)
                text = model.label
            if (isFollowing) {
                removeCompoundDrawableFollowButton()
                model.leftDrawableUrl = ""
            } else {
                setDrawableLeft(this, model.leftDrawableUrl, model.isNeverFollow)
            }
            setOnClickListener {
                if (!isLoading)
                    listener.onClickFollowUnFollowButton(
                        model,
                        shopPageHeaderWidgetUiModel
                    )
            }
            if (text.isNotEmpty()) {
                addOnImpressionListener(model) {
                    listener.onImpressionFollowButtonComponent(
                        model,
                        shopPageHeaderWidgetUiModel
                    )
                }
            }
        }
    }

    private fun setDrawableLeft(button: UnifyButton, leftDrawableUrl: String, isUserNeverFollow: Boolean) {
        if (leftDrawableUrl.isNotBlank() && isUserNeverFollow) {
            convertUrlToBitmapAndLoadImage(
                itemView.context,
                leftDrawableUrl,
                16.toPx()
            ) {
                try {
                    val drawableImage = BitmapDrawable(itemView.resources, it)
                    val left = 0
                    val right = drawableImage.intrinsicWidth + RIGHT_BOUND_COUNTER
                    val bottom = drawableImage.intrinsicHeight
                    drawableImage.setBounds(left, -TOP_BOUND, right, bottom)
                    val spannableString = SpannableString("   ${button.text}")
                    val imageSpan = ImageSpan(drawableImage)
                    spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableString.setSpan(
                        ShopPageHeaderTextBaselineSpanAdjuster(TEST_ASCENT_MULTIPLIER),
                        0,
                        spannableString.length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    button.text = spannableString
                    listener.onImpressionVoucherFollowUnFollowShop()
                } catch (e: Throwable) {}
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
