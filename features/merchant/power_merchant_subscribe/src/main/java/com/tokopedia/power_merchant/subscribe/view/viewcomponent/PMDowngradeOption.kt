package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmDowngradeOptionBinding

/**
 * Created By @ilhamsuaib on 12/05/21
 */

class PMDowngradeOption : ConstraintLayout {

    private var binding: ViewPmDowngradeOptionBinding? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var isOptionSelected: Boolean = false
        private set

    init {
        binding = ViewPmDowngradeOptionBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun show(label: String, name: String, description: String) = binding?.run {
        tvPmOptionLabel.text = label
        tvPmOptionName.text = name
        tvPmOptionDescription.text = description.parseAsHtml()
    }

    fun setSelectedStatus(isSelected: Boolean) {
        this.isOptionSelected = isSelected
        binding?.run {
            if (isSelected) {
                containerPmOption.setBackgroundResource(R.drawable.bg_pm_white_bordered_green)
                icPmOptionCheck.visible()
            } else {
                containerPmOption.setBackgroundResource(R.drawable.bg_pm_white_bordered_grey)
                icPmOptionCheck.gone()
            }
        }
    }
}