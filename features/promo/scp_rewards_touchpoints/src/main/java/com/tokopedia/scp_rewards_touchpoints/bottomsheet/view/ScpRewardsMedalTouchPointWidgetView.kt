package com.tokopedia.scp_rewards_touchpoints.bottomsheet.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.ScpRewardsMedalTouchPointModel
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.loadImage
import com.tokopedia.scp_rewards_touchpoints.common.util.ViewUtil.rotate
import com.tokopedia.scp_rewards_touchpoints.databinding.LayoutScpRewardsMedalTouchPointWidgetViewBinding

class ScpRewardsMedalTouchPointWidgetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(
    context,
    attrs
) {
    private var binding: LayoutScpRewardsMedalTouchPointWidgetViewBinding = LayoutScpRewardsMedalTouchPointWidgetViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        binding.iuSunburstIcon.rotate()
    }

    private fun LayoutScpRewardsMedalTouchPointWidgetViewBinding.setupUi(
        model: ScpRewardsMedalTouchPointModel
    ) {
        acivBackgroundWidget.loadImage(model.backgroundWidgetImage)
        iuBackgroundIcon.loadImage(model.backgroundIconImage)
        iuSunburstIcon.loadImage(model.sunburstImage)
        iuIcon.loadImage(model.iconImage)
        scpToasterTitle.text = model.title
        scpToasterDescription.text = model.description
    }

    fun setData(
        model: ScpRewardsMedalTouchPointModel
    ) {
        binding.setupUi(
            model = model
        )
    }
}
