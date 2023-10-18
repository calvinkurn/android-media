package com.tokopedia.recharge_credit_card.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.recharge_credit_card.databinding.WidgetCcEmptyRecomWidgetBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeCCEmptyRecommendationWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): BaseCustomView(context, attrs, defStyleAttr) {

    private val binding: WidgetCcEmptyRecomWidgetBinding = WidgetCcEmptyRecomWidgetBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
}
