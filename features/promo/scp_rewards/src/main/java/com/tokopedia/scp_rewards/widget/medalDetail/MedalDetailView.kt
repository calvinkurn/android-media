package com.tokopedia.scp_rewards.widget.medalDetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards.databinding.WidgetMedalDetailBinding

class MedalDetailView(private val context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalDetailBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: MedalDetail) {
        with(binding) {
            data.sponsorInformation?.let {
                tvBrand.text = it
                cvSponsor.visible()
            }
            tvMedalTitle.text = data.medalTitle
            tvMedalDesc.text = data.medalDescription
        }
    }
}
