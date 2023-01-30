package com.tokopedia.campaign.components.ineligibleaccessview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.campaign.databinding.CampaignCommonLayoutIneligibleAccessViewBinding
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage

class IneligibleAccessView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){

    private var binding: CampaignCommonLayoutIneligibleAccessViewBinding

    init {
        binding = CampaignCommonLayoutIneligibleAccessViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    data class Param(
        val imageUrl: String,
        val ineligibleReasonTitle: String,
        val ineligibleReasonDescription: String,
        val buttonTitle: String,
        val onButtonClick: () -> Unit
    )


    fun show(param: Param) {
        binding.imgIneligibleAccess.loadImage(param.imageUrl)
        binding.tpgTitle.text = param.ineligibleReasonTitle
        binding.tpgDescription.text = param.ineligibleReasonDescription
        binding.btnReadFeatureArticle.text = param.buttonTitle
        binding.btnReadFeatureArticle.setOnClickListener { param.onButtonClick() }
        visible()
    }

}
