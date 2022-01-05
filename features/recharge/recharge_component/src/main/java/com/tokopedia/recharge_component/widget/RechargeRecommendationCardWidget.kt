package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.recharge_component.databinding.WidgetRechargeRecommendationCardBinding
import org.jetbrains.annotations.NotNull

class RechargeRecommendationCardWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                 defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var rechargeRecommendationViewBinding: WidgetRechargeRecommendationCardBinding

    init {
        rechargeRecommendationViewBinding = WidgetRechargeRecommendationCardBinding.inflate(LayoutInflater.from(context), this, true)
    }

}