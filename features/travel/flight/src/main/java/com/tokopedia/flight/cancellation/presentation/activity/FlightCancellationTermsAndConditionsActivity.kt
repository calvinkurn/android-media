package com.tokopedia.flight.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationTermsAndConditionsFragment

class FlightCancellationTermsAndConditionsActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return FlightCancellationTermsAndConditionsFragment.createInstance()
    }

    override fun isShowCloseButton(): Boolean = true

    companion object {

        fun createIntent(context: Context?): Intent =
                Intent(context, FlightCancellationTermsAndConditionsActivity::class.java)

    }
}