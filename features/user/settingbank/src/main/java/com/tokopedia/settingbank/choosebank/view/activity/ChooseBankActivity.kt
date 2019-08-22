package com.tokopedia.settingbank.choosebank.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.settingbank.choosebank.view.fragment.ChooseBankFragment

/**
 * @author by nisie on 6/22/18.
 */

class ChooseBankActivity : BaseSimpleActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default)
    }


    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChooseBankFragment()
    }

    companion object {
        val PARAM_RESULT_DATA: String? = "RESULT_DATA"
        val PARAM_BANK_ID: String = "bank_id"

        fun createIntentChooseBank(context: Context): Intent {
            var intent = Intent(context, ChooseBankActivity::class.java)
            return intent
        }

        fun createIntentChooseBank(context: Context, bankId: String): Intent {
            var intent = Intent(context, ChooseBankActivity::class.java)
            intent.putExtra(PARAM_BANK_ID, bankId)
            return intent
        }

    }
}