package com.tokopedia.topupbills.telco.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoFragment

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class TelcoProductActivity : BaseTelcoActivity() {

    override fun getNewFragment(): Fragment {
        return DigitalTelcoFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateTitle(getString(R.string.digital_title_telco_page))
    }

    companion object {

        fun newInstance(context: Context): Intent {
            val intent = Intent(context, TelcoProductActivity::class.java)
            return intent
        }

    }

    override fun onBackPressed() {
        finish()
    }
}