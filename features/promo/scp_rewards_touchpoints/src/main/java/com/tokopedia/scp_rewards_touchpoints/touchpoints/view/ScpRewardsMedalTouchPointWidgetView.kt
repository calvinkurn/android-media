package com.tokopedia.scp_rewards_touchpoints.touchpoints.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.scp_rewards_common.utils.ViewUtil.rotate
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.loadImage
import com.tokopedia.scp_rewards_touchpoints.databinding.LayoutScpRewardsMedalTouchPointWidgetViewBinding
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model.ScpRewardsMedalTouchPointModel

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
        scpToasterTitle.text = model.title
        scpToasterSubtitle.text = model.subtitle
        iuIcon.loadImage(model.iconImage)
        iuSunburstIcon.loadImage(model.sunburstImage)
        iuBackgroundIcon.loadImage(model.backgroundIconImage)
        acivBackgroundWidget.loadImage(model.backgroundWidgetImage)
        icuChevron.showWithCondition(model.chevronIsShown)
    }

    fun setData(
        model: ScpRewardsMedalTouchPointModel
    ) {
        binding.setupUi(
            model = model
        )
    }
}
