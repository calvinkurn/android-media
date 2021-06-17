package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.view_pm_downgrade_option.view.*

/**
 * Created By @ilhamsuaib on 12/05/21
 */

class PMDowngradeOption : ConstraintLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var isOptionSelected: Boolean = false
        private set

    init {
        View.inflate(context, R.layout.view_pm_downgrade_option, this)
    }

    fun show(label: String, name: String, description: String) {
        tvPmOptionLabel.text = label
        tvPmOptionName.text = name
        tvPmOptionDescription.text = description.parseAsHtml()
    }

    fun setSelectedStatus(isSelected: Boolean) {
        this.isOptionSelected = isSelected
        if (isSelected) {
            containerPmOption.setBackgroundResource(R.drawable.bg_pm_white_bordered_green)
            icPmOptionCheck.visible()
        } else {
            containerPmOption.setBackgroundResource(R.drawable.bg_pm_white_bordered_grey)
            icPmOptionCheck.gone()
        }
    }
}