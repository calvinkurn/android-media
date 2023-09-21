package com.tokopedia.scp_rewards.widget.medalDetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.scp_rewards.common.utils.hide
import com.tokopedia.scp_rewards.common.utils.show
import com.tokopedia.scp_rewards.databinding.WidgetMedalDetailBinding
import com.tokopedia.scp_rewards_common.parseColor

class MedalDetailView(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalDetailBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: MedalDetail) {
        with(binding) {
            if (!data.sponsorText.isNullOrEmpty()) {
                cvSponsor.show()
                tvBrand.text = data.sponsorText
                tvBrand.setTextColor(parseColor(data.sponsorTextColor) ?: ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                cvSponsor.setCardBackgroundColor(parseColor(data.sponsorBackgroundColor) ?: ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN1000))
            } else {
                cvSponsor.hide()
            }
            tvMedalTitle.text = data.medalTitle
            tvMedalDesc.text = data.medalDescription
            requestLayout()
        }
    }
}
