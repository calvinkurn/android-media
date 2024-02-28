package com.tokopedia.discovery_component.widgets.automatecoupon

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery_component.databinding.AutomateCouponListLayoutBinding
import com.tokopedia.discovery_component.widgets.utils.HexColorParser
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class AutomateCouponListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IAutomateCouponView {

    private var binding = AutomateCouponListLayoutBinding
        .inflate(LayoutInflater.from(context), this)

    override fun setModel(couponModel: AutomateCouponModel) {
        (couponModel as? AutomateCouponModel.List)?.let { model ->
            renderBackgroundImage(model.backgroundUrl)
            renderDetails(model)
            renderIcon(model.iconUrl)
            renderExpiredDate(model.timeLimit)
            renderBadge(model.badgeText)
        }
    }

    override fun setState(state: ButtonState) {
        binding.btnAction.text = state.text

        when (state) {
            ButtonState.OutOfStock -> disableActionButton()
            else -> onClicked(state.action)
        }
    }

    override fun onClick(action: () -> Unit) {
        binding.root.setOnClickListener {
            action.invoke()
        }
    }

    //region private methods
    private fun renderDetails(model: AutomateCouponModel.List) {
        with(binding) {
            tvType.render(model.type)
            tvBenefit.render(model.benefit)
            tvFreeText.render(model.tnc)
            renderShopName(model.shopName)
        }
    }

    private fun renderShopName(text: DynamicColorText?) {
        if (text == null || text.value.isEmpty()) {
            binding.tvStoreName.hide()
            return
        }

        val formattedText = text.copy(value = "&#8226 &#160 ${text.value}")
        binding.tvStoreName.render(formattedText)
    }

    private fun renderBackgroundImage(backgroundUrl: String) {
        binding.imgCouponBackground.loadImageWithoutPlaceholder(backgroundUrl)
    }

    private fun renderIcon(iconUrl: String) {
        binding.imgIconType.loadImageWithoutPlaceholder(iconUrl)
    }

    private fun renderBadge(badgeText: String?) {
        binding.remainingBadge.render(badgeText)
    }

    private fun renderExpiredDate(limit: TimeLimit) {
        if (!limit.isAvailable()) {
            binding.tvTimeLimitPrefix.hide()
            binding.tvTimeLimit.hide()
            binding.timerCoupon.hide()
            return
        }

        limit.prefix?.let { binding.tvTimeLimitPrefix.render(it) }
        limit.showExpiredInfo()
    }

    private fun TimeLimit.showExpiredInfo() {
        when (this) {
            is TimeLimit.Timer -> {
                endDate?.showTimer()
            }

            is TimeLimit.Text -> {
                showTimeLimit(endText)
            }
        }
    }

    private fun Date?.showTimer() {
        this?.let {
            val parsedCalendar: Calendar = Calendar.getInstance()
            parsedCalendar.time = it

            binding.timerCoupon.apply {
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                targetDate = parsedCalendar
            }

            binding.timerCoupon.show()
            binding.tvTimeLimit.hide()
        }
    }

    private fun showTimeLimit(text: String?) {
        binding.tvTimeLimit.text = text
        binding.tvTimeLimit.show()

        binding.timerCoupon.hide()
    }

    private fun disableActionButton() {
        binding.btnAction.apply {
            isInverse = false
            isEnabled = false
        }
    }

    private fun onClicked(action: () -> Unit) {
        binding.btnAction.apply {
            isInverse = false
            isEnabled = true
        }
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
