package com.tokopedia.common.stub

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common.di.AppStubComponent
import com.tokopedia.common.di.DaggerUniversalShareStubComponent
import com.tokopedia.common.di.UniversalShareStubModule
import com.tokopedia.linker.LinkerManager
import com.tokopedia.universal_sharing.di.UniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareUseCaseModule
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet

class UniversalShareBottomSheetStub : UniversalShareBottomSheet() {

    override fun getComponent(): UniversalShareComponent? {
        return DaggerUniversalShareStubComponent.builder().appStubComponent((LinkerManager.getInstance().context.applicationContext as BaseMainApplication).baseAppComponent as AppStubComponent)
            .universalShareStubModule(UniversalShareStubModule()).universalShareUseCaseModule(UniversalShareUseCaseModule()).build()    }
}
