package com.tokopedia.home_component.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.tokopedia.home_component.databinding.HomeComponentRibbonBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.home_component.R as home_componentR

internal class RibbonView: LinearLayout {

    private var binding: HomeComponentRibbonBinding? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        orientation = VERTICAL
        inflate(context, home_componentR.layout.home_component_ribbon, this).let {
            binding = HomeComponentRibbonBinding.bind(it)
        }
    }

    fun render(text: String) {
        if (text.isNotEmpty()) showRibbon(text)
        else hide()
    }

    private fun showRibbon(text: String) {
        show()
        binding?.homeComponentRibbonText?.text = text
    }
}
