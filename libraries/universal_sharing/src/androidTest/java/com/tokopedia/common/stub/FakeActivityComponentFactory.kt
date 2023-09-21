package com.tokopedia.common.stub

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.di.DaggerUniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareModule
import com.tokopedia.universal_sharing.di.UniversalShareUseCaseModule
import com.tokopedia.user.session.UserSessionInterface

class FakeActivityComponentFactory : ActivityComponentFactory() {

    var isLogin: Boolean = false

    override fun createActivityComponent(context: Context): UniversalShareComponent {
        return DaggerUniversalShareComponent.builder()
            .baseAppComponent((context as BaseMainApplication).baseAppComponent)
            .universalShareModule(object : UniversalShareModule() {
                override fun provideUserSession(context: Context): UserSessionInterface {
                    return UserSessionStub(isLogin, context)
                }
            })
            .universalShareUseCaseModule(UniversalShareUseCaseModule())
            .build()
    }

}
