package com.tokopedia.scp_rewards.widget.medalDetail

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.scp_rewards.common.utils.hide
import com.tokopedia.scp_rewards.common.utils.parseColor
import com.tokopedia.scp_rewards.common.utils.show
import com.tokopedia.scp_rewards.databinding.WidgetMedalDetailBinding

class MedalDetailView(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalDetailBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: MedalDetail) {
        with(binding) {
            data.sponsorText?.let {
                cvSponsor.show()
                tvBrand.text = it
                tvBrand.setTextColor(parseColor(data.sponsorTextColor) ?: Color.WHITE)
                cvSponsor.setCardBackgroundColor(parseColor(data.sponsorBackgroundColor) ?: Color.BLACK)
            } ?: run {
                cvSponsor.hide()
            }
            tvMedalTitle.text = data.medalTitle
            tvMedalDesc.text = data.medalDescription
            requestLayout()
        }
    }
}
