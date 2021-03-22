package com.tokopedia.gm.common.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import kotlinx.android.synthetic.main.view_gmc_power_merchant_grade.view.*

/**
 * Created By @ilhamsuaib on 20/03/21
 */

class PowerMerchantGradeView : RelativeLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_gmc_power_merchant_grade, this)
    }

    fun show(data: PowerMerchantInterruptUiModel) {
        containerCardPm.setBackgroundResource(R.drawable.bg_gmc_power_merchant_card)
    }
}