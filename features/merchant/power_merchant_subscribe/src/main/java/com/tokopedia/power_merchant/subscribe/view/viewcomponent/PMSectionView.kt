package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By @ilhamsuaib on 24/04/21
 */

class PowerMerchantSectionView : LinearLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val tvTitle: Typography
    private val viewUnderline: View

    init {
        View.inflate(context, R.layout.view_pm_section, this)

        tvTitle = findViewById(R.id.tvPmSectionTitle)
        viewUnderline = findViewById(R.id.viewPmSectionUnderline)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setSelectedStatus(isSelected: Boolean) {
        if (isSelected) {
            tvTitle.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
            viewUnderline.setBackgroundColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
        } else {
            tvTitle.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            viewUnderline.setBackgroundResource(R.drawable.pm_section_underline_disabled)
        }
    }
}