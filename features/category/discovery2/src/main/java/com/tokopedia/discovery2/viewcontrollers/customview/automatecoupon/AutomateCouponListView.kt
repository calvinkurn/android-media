package com.tokopedia.discovery2.viewcontrollers.customview.automatecoupon

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.discovery2.databinding.AutomateCouponListLayoutBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
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
        renderExpiredDate(model.endDate)
        renderBadge(model.badgeText)
    }

    //region private methods
    private fun renderDetails(model: AutomateCouponModel) {
        with(binding) {
            tvType.text = model.type
            tvBenefit.text = model.benefit
            tvFreeText.text = model.freeText
        }
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

    private fun renderExpiredDate(endDate: Date?) {
        if (endDate == null) {
            binding.timerGroup.hide()
            return
        }

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

        binding.tvTimerPrefix.text = "Berakhir dalam"
        binding.timerGroup.show()
    }
    //endregion
}
