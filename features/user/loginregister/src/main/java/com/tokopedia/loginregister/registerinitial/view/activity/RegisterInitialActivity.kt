package com.tokopedia.loginregister.registerinitial.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.registerinitial.di.DaggerRegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment.Companion.createInstance
import com.tokopedia.telemetry.ITelemetryActivity

/**
 * @author by nisie on 10/2/18.
 */
class RegisterInitialActivity : BaseSimpleActivity(), HasComponent<RegisterInitialComponent>,
    ITelemetryActivity {

    private var registerInitialComponent: RegisterInitialComponent? = null

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return createInstance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
    }

    override fun getComponent(): RegisterInitialComponent {
        return registerInitialComponent ?: initializeRegisterInitialComponent()
    }

    private fun initializeRegisterInitialComponent(): RegisterInitialComponent {
        return ActivityComponentFactory.instance
            .createRegisterComponent(application)
            .also {
                registerInitialComponent = it
            }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.parent_view) is RegisterInitialFragment) {
            (supportFragmentManager.findFragmentById(R.id.parent_view) as RegisterInitialFragment?)?.onBackPressed()
        }
        super.onBackPressed()
    }

    override fun getToolbarResourceID(): Int {
        return R.id.unifytoolbar
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_login_register
    }

    companion object {
        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, RegisterInitialActivity::class.java)
        }
    }

    override fun getTelemetrySectionName() = "register"
}