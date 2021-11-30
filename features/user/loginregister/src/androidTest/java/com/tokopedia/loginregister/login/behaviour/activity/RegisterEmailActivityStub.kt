package com.tokopedia.loginregister.login.behaviour.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.login.behaviour.di.RegisterInitialComponentStub
import com.tokopedia.loginregister.login.behaviour.fragment.RegisterEmailFragmentStub
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterEmailActivity

class RegisterEmailActivityStub: RegisterEmailActivity() {
    lateinit var registerInitialComponentStub: RegisterInitialComponentStub

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    override fun initializeRegisterInitialComponent(): RegisterInitialComponentStub {
        return registerInitialComponentStub
    }

    fun setupTestFragment(loginRegisterComponent: RegisterInitialComponentStub) {
        this.registerInitialComponentStub = loginRegisterComponent
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, newFragment, TAG)
            .commit()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        intent?.extras?.let {
            bundle.putAll(it)
        }
        return RegisterEmailFragmentStub.createInstance(bundle)
    }

    companion object {
        const val TAG = "registerEmailActivity"
    }
}