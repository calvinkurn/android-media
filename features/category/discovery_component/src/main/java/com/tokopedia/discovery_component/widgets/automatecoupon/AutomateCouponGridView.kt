package com.tokopedia.discovery_component.widgets.automatecoupon

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery_component.R
import com.tokopedia.discovery_component.databinding.AutomateCouponGridLayoutBinding
import com.tokopedia.discovery_component.widgets.utils.HexColorParser
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography

class AutomateCouponGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IAutomateCouponView {

    private var binding =
        AutomateCouponGridLayoutBinding.inflate(LayoutInflater.from(context), this)

    override fun setModel(couponModel: AutomateCouponModel) {
        (couponModel as? AutomateCouponModel.Grid)?.let { model ->
            renderBackgroundImage(model.backgroundUrl)
            renderDetails(model)
            renderShopLogo(model.iconUrl)
            renderBadge(model.badgeText)
            renderShopInfo(model.iconUrl, model.shopName)
        }
    }

    override fun setState(state: ButtonState) {
        binding.btnAction.text = state.text

        when (state) {
            ButtonState.OutOfStock -> disableActionButton()
            else -> onClicked(state.action)
        }
    }

    //region private methods
    private fun renderDetails(model: AutomateCouponModel.Grid) {
        with(binding) {
            tvType.render(model.type)
            tvBenefit.render(model.benefit)
            tvFreeText.render(model.tnc)
        }
    }

    private fun renderShopInfo(iconUrl: String?, shopName: DynamicColorText?) {
        if (iconUrl.isNullOrEmpty() || shopName?.value?.isEmpty() == true) {
            binding.shopProfileGroup.hide()
            binding.tvShopName.hide()
            setCardHeight(R.dimen.automate_coupon_without_shop_profile_height)
            return
        }

        setCardHeight(R.dimen.automate_coupon_height)
        renderShopLogo(iconUrl)
        renderShopName(shopName)
    }

    private fun setCardHeight(@DimenRes resId: Int) {
        val height = context.resources.getDimensionPixelSize(resId)
        binding.cardView.setLayoutHeight(height)
    }

    private fun renderShopName(text: DynamicColorText?) {
        text?.let {
            binding.tvShopName.show()
            binding.tvShopName.render(it)
        }
    }

    private fun renderBackgroundImage(backgroundUrl: String) {
        binding.imgCouponBackground.loadImageWithoutPlaceholder(backgroundUrl)
    }

    private fun renderShopLogo(iconUrl: String?) {
        binding.shopProfileGroup.show()
        binding.imgShopLogo.loadImageWithoutPlaceholder(iconUrl)
    }

    private fun renderBadge(badgeText: String?) {
//        binding.remainingBadge.render(badgeText)
    }

    private fun disableActionButton() {
        binding.btnAction.apply {
            isInverse = false
            isEnabled = false
        }
    }

    private fun onClicked(action: () -> Unit) {
        binding.btnAction.setOnClickListener {
            action.invoke()
        }
    }

    private fun Typography.render(dynamicColorText: DynamicColorText) {
        text = MethodChecker.fromHtml(dynamicColorText.value)
        HexColorParser.parse(dynamicColorText.colorHex) {
            setTextColor(it)
        }
    }
    //endregion
}
