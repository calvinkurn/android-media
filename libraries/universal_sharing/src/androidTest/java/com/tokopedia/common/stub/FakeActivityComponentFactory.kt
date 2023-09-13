package com.tokopedia.common.stub

import UserSessionNonLoginStub
import UserSessionStub
import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.linker.LinkerManager
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.di.DaggerUniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareModule
import com.tokopedia.universal_sharing.di.UniversalShareUseCaseModule
import com.tokopedia.user.session.UserSessionInterface

class FakeActivityComponentFactory : ActivityComponentFactory() {

    var isLogin: Boolean = false

    override fun createActivityComponent(): UniversalShareComponent {
        return DaggerUniversalShareComponent.builder()
            .baseAppComponent((LinkerManager.getInstance().context.applicationContext as BaseMainApplication).baseAppComponent)
            .universalShareModule(object : UniversalShareModule(){
                override fun provideUserSession(context: Context): UserSessionInterface {
                    return if (isLogin) {
                        UserSessionStub(context)
                    } else {
                        UserSessionNonLoginStub(context)
                    }
                }
            })
            .universalShareUseCaseModule(UniversalShareUseCaseModule())
            .build()
    }

}
