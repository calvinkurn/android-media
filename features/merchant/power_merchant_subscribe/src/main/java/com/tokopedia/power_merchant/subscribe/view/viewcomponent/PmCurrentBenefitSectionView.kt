package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.view_pm_current_benefit_section.view.*

/**
 * Created By @ilhamsuaib on 08/05/21
 */

class PmCurrentBenefitSectionView : ConstraintLayout {
    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_pm_current_benefit_section, this)
    }

    fun setOnExpandedChanged(shouldExpanded: Boolean) {
        if (shouldExpanded) {
            icPmExpandableTitleIcon.setImage(IconUnify.CHEVRON_DOWN)
            viewPmExpandableHorLine.visible()
        } else {
            icPmExpandableTitleIcon.setImage(IconUnify.CHEVRON_UP)
            viewPmExpandableHorLine.gone()
        }
    }
}