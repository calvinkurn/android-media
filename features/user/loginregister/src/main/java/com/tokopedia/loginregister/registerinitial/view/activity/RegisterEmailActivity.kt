package com.tokopedia.loginregister.registerinitial.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterEmailFragment
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterEmailFragment.Companion.createInstance

/**
 * @author by nisie on 10/25/18.
 */
class RegisterEmailActivity : BaseSimpleActivity(), HasComponent<Any?> {
    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return createInstance(bundle)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        toolbar.setPadding(0, 0, 30, 0)
    }

    override fun getComponent(): LoginRegisterComponent {
        return DaggerLoginRegisterComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.parent_view) is RegisterEmailFragment) {
            (supportFragmentManager.findFragmentById(R.id.parent_view) as RegisterEmailFragment?)!!.onBackPressed()
        }
        super.onBackPressed()
    }

    companion object {
        const val EXTRA_PARAM_EMAIL = "email"
        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, RegisterEmailActivity::class.java)
        }

        fun getCallingIntentWithEmail(context: Context, email: String,
                                      source: String): Intent {
            val intent = Intent(context, RegisterEmailActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_PARAM_EMAIL, email)
            bundle.putString(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
            intent.putExtras(bundle)
            return intent
        }
    }
}