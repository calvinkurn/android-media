package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By @ilhamsuaib on 24/04/21
 */

class TermHeaderView : LinearLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val tvTermStatus: Typography
    private val icExpandedStatus: IconUnify
    private var onClickCallback: ((Boolean) -> Unit)? = null
    private var isExpanded = true

    init {
        View.inflate(context, R.layout.view_pm_terms_header, this)

        tvTermStatus = findViewById(R.id.tvPmHeaderTermsStatus)
        icExpandedStatus = findViewById(R.id.icPmHeaderTerms)

        setOnClickListener {
            changeIconExpandedStatus()
            onClickCallback?.invoke(isExpanded)
        }
    }

    private fun changeIconExpandedStatus() {
        isExpanded = !isExpanded
        if (isExpanded) {
            icExpandedStatus.setImage(IconUnify.CHEVRON_UP)
        } else {
            icExpandedStatus.setImage(IconUnify.CHEVRON_DOWN)
        }
    }

    fun setEligibility(isEligible: Boolean) {
        tvTermStatus.isVisible = isEligible
    }

    fun setOnSectionHeaderClickListener(callback: (isExpanded: Boolean) -> Unit) {
        this.onClickCallback = callback
    }
}