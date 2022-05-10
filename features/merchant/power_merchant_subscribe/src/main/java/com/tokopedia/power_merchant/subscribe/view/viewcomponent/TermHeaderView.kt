package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmTermsHeaderBinding

/**
 * Created By @ilhamsuaib on 24/04/21
 */

class TermHeaderView : LinearLayout {

    private var binding: ViewPmTermsHeaderBinding? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var onClickCallback: ((Boolean) -> Unit)? = null
    private var isExpanded = true

    init {
        binding = ViewPmTermsHeaderBinding.inflate(LayoutInflater.from(context), this, true)
        binding?.icPmProBadge?.loadImage(PMConstant.Images.PM_PRO_BADGE)
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

    fun setOnSectionHeaderClickListener(callback: (isExpanded: Boolean) -> Unit) {
        this.onClickCallback = callback
    }
}