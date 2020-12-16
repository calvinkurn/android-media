package com.tokopedia.loginregister.registerinitial.stub

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.registerinitial.di.MockRegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity

class RegisterInitialActivityStub: RegisterInitialActivity() {
    override fun getNewFragment(): Fragment {
       return RegisterInitialFragmentStub()
    }

    fun setupTestFragment(): Fragment {
        val newFragment = newFragment
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, tagFragment)
                .commit()
        return newFragment
    }
}