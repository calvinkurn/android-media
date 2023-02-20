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
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderActionWidgetFollowButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.util.TextBaselineSpanAdjuster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class ShopActionButtonWidgetFollowButtonComponentViewHolder(
    itemView: View,
    private val shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel,
    private val listener: Listener
) : AbstractViewHolder<ShopHeaderActionWidgetFollowButtonComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_action_button_widget_follow_button_component
        private const val TOP_BOUND = 5
        private const val RIGHT_BOUND_COUNTER = 5
        private const val TEST_ASCENT_MULTIPLIER = 0.15
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

    private val viewBinding: LayoutShopActionButtonWidgetFollowButtonComponentBinding? by viewBinding()
    private val buttonFollow: UnifyButton? = viewBinding?.buttonShopFollow

    init {
        val lp = itemView.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            val flexboxLp = lp as FlexboxLayoutManager.LayoutParams
            flexboxLp.flexGrow = 1.0f
        }
    }

    override fun bind(model: ShopHeaderActionWidgetFollowButtonComponentUiModel) {
        val shopFollowButtonVariantType = ShopUtil.getShopFollowButtonAbTestVariant().orEmpty()
        val isFollowing = model.isFollowing
        buttonFollow?.apply {
            when (shopFollowButtonVariantType) {
                RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_OLD -> {
                    // existing/old variant type follow button
                    buttonSize = UnifyButton.Size.MICRO
                    buttonVariant = UnifyButton.Variant.GHOST
                    buttonType = UnifyButton.Type.ALTERNATE.takeIf { isFollowing } ?: UnifyButton.Type.MAIN
                }
                RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_SMALL -> {
                    // new variant type follow button micro size
                    buttonSize = UnifyButton.Size.MICRO
                    buttonVariant = UnifyButton.Variant.GHOST.takeIf { isFollowing } ?: UnifyButton.Variant.FILLED
                    buttonType = UnifyButton.Type.ALTERNATE.takeIf { isFollowing } ?: UnifyButton.Type.MAIN
                }
                RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_BIG -> {
                    // new variant type follow button small size
                    buttonSize = UnifyButton.Size.SMALL
                    buttonVariant = UnifyButton.Variant.GHOST.takeIf { isFollowing } ?: UnifyButton.Variant.FILLED
                    buttonType = UnifyButton.Type.ALTERNATE.takeIf { isFollowing } ?: UnifyButton.Type.MAIN
                }
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
                        shopHeaderWidgetUiModel
                    )
            }
            if (text.isNotEmpty()) {
                addOnImpressionListener(model) {
                    listener.onImpressionFollowButtonComponent(
                        model,
                        shopHeaderWidgetUiModel
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
                        TextBaselineSpanAdjuster(TEST_ASCENT_MULTIPLIER),
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
