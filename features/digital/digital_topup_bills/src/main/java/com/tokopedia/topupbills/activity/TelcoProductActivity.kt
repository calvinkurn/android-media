package com.tokopedia.topupbills.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topupbills.TelcoProductFragment

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class TelcoProductActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return TelcoProductFragment.newInstance()
    }

    companion object {

        fun newInstance(context : Context): Intent {
            val intent = Intent(context, TelcoProductActivity::class.java)
            return intent
        }

    }
}