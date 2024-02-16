package com.tokopedia.bmsm_widget.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.bmsm_widget.R
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by @ilhamsuaib on 29/11/23.
 */

class BmsmRibbonView : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    private var tvBmsmGiftCount: Typography? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_bmsm_ribbon, this, true)
        tvBmsmGiftCount = findViewById(R.id.tvBmsmGiftCount)
    }

    fun setText(text: String) {
        tvBmsmGiftCount?.text = text
    }
}