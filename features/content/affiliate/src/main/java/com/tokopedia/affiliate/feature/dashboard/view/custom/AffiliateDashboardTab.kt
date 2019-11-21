package com.tokopedia.affiliate.feature.dashboard.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R

/**
 * Created by jegul on 2019-09-23.
 */
class AffiliateDashboardTab : LinearLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context?, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    private val tvTitle: TextView

    init {
        val view = View.inflate(context, R.layout.item_tab_dashboard, this)
        with(view) {
            tvTitle = findViewById(R.id.tv_title)
        }
    }

    override fun getLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setActive(isActive: Boolean) {
        tvTitle.setTextColor(
                MethodChecker.getColor(
                        context,
                        if (isActive) com.tokopedia.design.R.color.Green_G500
                        else com.tokopedia.design.R.color.Neutral_N700_44
                )
        )
    }
}