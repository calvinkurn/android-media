package com.tokopedia.travelhomepage.destination.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.travelhomepage.destination.presentation.fragment.TravelHomepageDestinationFragment

class TravelHomepageDestinationActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment = TravelHomepageDestinationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
