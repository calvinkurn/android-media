package com.tokopedia.otp.verification.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.otp.verification.view.fragment.ResultFragment

class ResultActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ResultFragment.createInstance(bundle)
    }

    companion object {
        fun getCallingIntent(context: Context?, extras: Bundle?): Intent? {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtras(extras ?: Bundle())
            return intent
        }
    }

}