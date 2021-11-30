package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmCurrentBenefitSectionBinding

/**
 * Created By @ilhamsuaib on 08/05/21
 */

class PmCurrentBenefitSectionView : ConstraintLayout {

    private var binding: ViewPmCurrentBenefitSectionBinding? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        binding = ViewPmCurrentBenefitSectionBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setOnExpandedChanged(shouldExpanded: Boolean) = binding?.run {
        if (shouldExpanded) {
            icPmExpandableTitleIcon.setImage(IconUnify.CHEVRON_UP)
            viewPmExpandableHorLine.visible()
        } else {
            icPmExpandableTitleIcon.setImage(IconUnify.CHEVRON_DOWN)
            viewPmExpandableHorLine.gone()
        }
    }
}