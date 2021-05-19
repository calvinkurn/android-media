package com.tokopedia.loginregister.login.behaviour.activity

import com.tokopedia.loginregister.login.behaviour.di.LoginComponentStub
import com.tokopedia.loginregister.login.view.activity.LoginActivity

class LoginActivityStub: LoginActivity() {

    lateinit var loginComponentStub: LoginComponentStub

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    override fun initializeLoginComponent(): LoginComponentStub {
        return loginComponentStub
    }

    fun setupTestFragment(loginComponentStub: LoginComponentStub) {
        this.loginComponentStub = loginComponentStub
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, TAG)
                .commit()
    }

    override fun getTagFragment(): String {
        return TAG
    }

    companion object {
        const val TAG = "loginfragment"
    }
}