package com.tokopedia.recharge_credit_card

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import kotlinx.android.synthetic.main.activity_recharge_cc.*

class RechargeCCActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return RechargeCCFragment.newInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_recharge_cc
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar_credit_card
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar_credit_card.addRightIcon(R.drawable.digital_common_ic_tagihan)
        toolbar_credit_card.rightIcons?.let {
            it[0].setOnClickListener {
                RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
            }
        }
    }
}