package com.tokopedia.loginregister.login.behaviour.di.modules

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.di.LoginRegisterModule
import com.tokopedia.loginregister.common.di.LoginRegisterScope
import com.tokopedia.loginregister.login.behaviour.di.BaseAppComponentStub
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

@LoginRegisterScope
@SessionCommonScope
@Component(modules = [LoginRegisterModule::class, SessionModule::class], dependencies = [BaseAppComponentStub::class])
interface MockLoginRegisterComponent: LoginRegisterComponent {
//    fun inject(loginUiTes: LoginUiTes)
//    fun inject(activity: LoginBase)
//    fun inject(fragment: LoginEmailPhoneFragmentStub)
}