package com.tokopedia.homecredit.di.component

import com.tokopedia.homecredit.di.scope.HomeCreditScope
import com.tokopedia.homecredit.view.activity.HomeCreditRegisterActivity
import dagger.Component

@HomeCreditScope
@Component
interface HomeCreditComponent {

    fun inject (homeCreditBaseCameraFragment: HomeCreditBaseCameraFragment)
}