package com.tokopedia.sellerhome.stub.di.component

import android.content.Context
import com.tokopedia.sellerhome.stub.di.module.AppModuleStub

/**
 * Created by @ilhamsuaib on 25/11/21.
 */

object BaseAppComponentStubInstance {

    private lateinit var baseAppComponentStub: BaseAppComponentStub

    fun getBaseAppComponentStub(context: Context): BaseAppComponentStub {
        if (!::baseAppComponentStub.isInitialized) {
            baseAppComponentStub = DaggerBaseAppComponentStub.builder()
                .appModuleStub(AppModuleStub(context))
                .build()
        }

        return baseAppComponentStub
    }
}