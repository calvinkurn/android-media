package com.tokopedia.tokopedianow.home.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowQuestProgressBarCircleViewBinding
import com.tokopedia.unifycomponents.BaseCustomView

class HomeQuestProgressCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    init {
        LayoutTokopedianowQuestProgressBarCircleViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }
}
