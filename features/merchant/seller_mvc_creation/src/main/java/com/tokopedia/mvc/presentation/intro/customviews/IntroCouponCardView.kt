package com.tokopedia.mvc.presentation.intro.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.mvc.databinding.SmvcIntroCard1Binding
import com.tokopedia.unifycomponents.BaseCustomView

class IntroCouponCardView  @JvmOverloads constructor(
    context: Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0
): BaseCustomView(context, attrs, defStyleAttr) {

    var binding: SmvcIntroCard1Binding? = null

    init {
        init()
    }

    private fun init() {
        binding = SmvcIntroCard1Binding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setText(mainTitle: String, subtitle: String) {
        binding?.title?.text = mainTitle
        binding?.subtitle?.text = subtitle
    }
}
