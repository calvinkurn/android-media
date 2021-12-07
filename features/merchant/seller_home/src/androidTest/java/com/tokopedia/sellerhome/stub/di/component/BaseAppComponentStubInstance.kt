package com.tokopedia.sellerhome.stub.di.component

import android.app.Application
import com.tokopedia.sellerhome.stub.di.module.AppModuleStub

/**
 * Created by @ilhamsuaib on 25/11/21.
 */

object BaseAppComponentStubInstance {

    private lateinit var baseAppComponentStub: BaseAppComponentStub

    fun getBaseAppComponentStub(application: Application): BaseAppComponentStub {
        if (!::baseAppComponentStub.isInitialized) {
            baseAppComponentStub = DaggerBaseAppComponentStub.builder()
                .appModuleStub(AppModuleStub(application))
                .build()
        }

        return baseAppComponentStub
    }
}