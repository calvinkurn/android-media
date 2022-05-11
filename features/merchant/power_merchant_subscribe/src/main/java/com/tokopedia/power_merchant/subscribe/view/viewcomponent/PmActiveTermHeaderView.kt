package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmActiveTermsHeaderBinding
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmTermsHeaderBinding

/**
 * Created By @ilhamsuaib on 24/04/21
 */

class PmActiveTermHeaderView : LinearLayout {

    private var binding: ViewPmActiveTermsHeaderBinding? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var onClickCallback: ((Boolean) -> Unit)? = null
    private var isExpanded = true

    init {
        binding = ViewPmActiveTermsHeaderBinding.inflate(LayoutInflater.from(context), this, true)

        binding?.root?.setOnClickListener {
            isExpanded = !isExpanded
            changeIconExpandedStatus()
            onClickCallback?.invoke(isExpanded)
        }
    }

    private fun changeIconExpandedStatus() = binding?.run {
        if (isExpanded) {
            icPmHeaderTerms.setImage(IconUnify.CHEVRON_UP)
        } else {
            icPmHeaderTerms.setImage(IconUnify.CHEVRON_DOWN)
        }
    }

    fun setExpanded(isExpanded: Boolean) {
        this.isExpanded = isExpanded
        changeIconExpandedStatus()
    }
    fun setTermStatus(isEligible: Boolean) {
        binding?.tvPmHeaderTermsStatus?.isVisible = isEligible
    }

    fun setOnSectionHeaderClickListener(callback: (isExpanded: Boolean) -> Unit) {
        this.onClickCallback = callback
    }
}