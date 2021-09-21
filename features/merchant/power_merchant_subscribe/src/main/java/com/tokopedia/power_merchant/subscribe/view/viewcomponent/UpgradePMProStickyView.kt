package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.view_pm_upgrade_pm_pro_sticky.view.*

/**
 * Created By @ilhamsuaib on 24/05/21
 */

class UpgradePMProStickyView : LinearLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_pm_upgrade_pm_pro_sticky, this)
        viewPmUpgradePmProSticky.setBackgroundResource(R.drawable.bg_pm_upgrade_pm_pro_sticky)
    }
}