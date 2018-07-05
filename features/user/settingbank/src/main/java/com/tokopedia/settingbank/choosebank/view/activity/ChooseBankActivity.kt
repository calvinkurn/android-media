package com.tokopedia.settingbank.choosebank.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.settingbank.choosebank.view.fragment.ChooseBankFragment

/**
 * @author by nisie on 6/22/18.
 */

class ChooseBankActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChooseBankFragment()
    }

    companion object {
        val PARAM_RESULT_DATA: String? = "RESULT_DATA"

        fun createIntentChooseBank(context: Context): Intent {
            var intent = Intent(context, ChooseBankActivity::class.java)
            return intent
        }

    }
}