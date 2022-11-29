package com.tokopedia.loginregister.redefineregisteremail.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.redefineregisteremail.di.RedefineRegisterEmailComponent

class RedefineRegisterEmailActivity :
    BaseSimpleActivity(),
    HasComponent<RedefineRegisterEmailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
    }

    override fun getLayoutRes(): Int = R.layout.activity_redefine_register_email

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): RedefineRegisterEmailComponent {
        return ActivityComponentFactory.instance.createRedefineRegisterEmailComponent(application)
    }

    override fun onBackPressed() {
        try {
            val fragment =
                supportFragmentManager.fragments.last().childFragmentManager.fragments.last()
            if (fragment is TkpdBaseV4Fragment && fragment.isVisible) {
                val handled = fragment.onFragmentBackPressed()
                if (handled) {
                    return
                }
            }
        } catch (_: Exception) {
        }

        super.onBackPressed()
    }
}
