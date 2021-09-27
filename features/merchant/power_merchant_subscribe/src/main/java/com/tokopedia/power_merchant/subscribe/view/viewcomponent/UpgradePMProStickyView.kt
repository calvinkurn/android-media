package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmUpgradePmProStickyBinding

/**
 * Created By @ilhamsuaib on 24/05/21
 */

class UpgradePMProStickyView : LinearLayout {

    private var binding: ViewPmUpgradePmProStickyBinding? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = ViewPmUpgradePmProStickyBinding.inflate(LayoutInflater.from(context), this, true)
        binding?.viewPmUpgradePmProSticky?.setBackgroundResource(R.drawable.bg_pm_upgrade_pm_pro_sticky)
    }
}