package com.tokopedia.loginregister.login.stub

import android.app.Activity
import android.content.Intent
import com.tokopedia.loginregister.login.behaviour.activity.LoginActivityStub
import com.tokopedia.loginregister.registerinitial.view.util.RegisterInitialRouterHelper

class RegisterInitialRouterHelperStub: RegisterInitialRouterHelper() {

    override fun goToLoginPage(context: Activity) {
        context.startActivity(Intent(context, LoginActivityStub::class.java))
    }
}