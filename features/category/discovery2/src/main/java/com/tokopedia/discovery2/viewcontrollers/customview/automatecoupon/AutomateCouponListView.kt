package com.tokopedia.discovery2.viewcontrollers.customview.automatecoupon

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.databinding.AutomateCouponListLayoutBinding
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

    override fun setModel(model: AutomateCouponModel) {
        renderBackgroundImage(model.backgroundUrl)
        renderDetails(model)
        renderIcon(model.iconUrl)
        renderExpiredDate(model.endDate, model.timeLimitPrefix)
        renderBadge(model.badgeText)
    }

    //region private methods
    private fun renderDetails(model: AutomateCouponModel) {
        with(binding) {
            tvType.render(model.type)
            tvBenefit.render(model.benefit)
            tvFreeText.render(model.tnc)
            renderShopName(model.shopName)
        }
    }

    private fun renderShopName(text: DynamicColorText?) {
        if (text == null) {
            binding.tvStoreName.hide()
            return
        }

        val formattedText = text.copy(value = "&#8226 &#160 ${text.value}")
        binding.tvStoreName.render(formattedText)
    }

    private fun renderBackgroundImage(backgroundUrl: String) {
        binding.imgCouponBackground.loadImageWithoutPlaceholder(backgroundUrl)
    }

    private fun renderIcon(iconUrl: String?) {
        if (iconUrl.isNullOrEmpty()) {
            binding.imgIconType.hide()
            return
        }

        binding.imgIconType.loadImageWithoutPlaceholder(iconUrl)
    }

    private fun renderBadge(badgeText: String?) {
        binding.remainingBadge.render(badgeText)
    }

    private fun renderExpiredDate(endDate: Date?, prefixText: DynamicColorText?) {
        if (endDate == null || prefixText?.value?.isEmpty() == true) {
            binding.timerGroup.hide()
            return
        }

        prefixText?.let { binding.tvTimeLimitPrefix.render(it) }
        endDate.showExpiredInfo()
    }

    @SuppressLint("SetTextI18n")
    private fun Date.showExpiredInfo() {
        val parsedCalendar: Calendar = Calendar.getInstance()
        parsedCalendar.time = this

        binding.timerCoupon.apply {
            backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            targetDate = parsedCalendar
        }

        binding.timerGroup.show()
    }

    private fun Typography.render(dynamicColorText: DynamicColorText) {
        text = MethodChecker.fromHtml(dynamicColorText.value)
        setTextColor(Color.parseColor(dynamicColorText.colorHex))
    }
    //endregion
}
