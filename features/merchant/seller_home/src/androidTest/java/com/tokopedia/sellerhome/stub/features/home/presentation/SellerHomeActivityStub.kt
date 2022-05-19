package com.tokopedia.sellerhome.stub.features.home.presentation

import android.content.Context
import android.content.Intent
import com.tokopedia.sellerhome.di.component.HomeDashboardComponent
import com.tokopedia.sellerhome.stub.di.component.BaseAppComponentStubInstance
import com.tokopedia.sellerhome.stub.features.home.di.DaggerSellerHomeComponentStub
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity

/**
 * Created by @ilhamsuaib on 06/12/21.
 */

class SellerHomeActivityStub : SellerHomeActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SellerHomeActivityStub::class.java)
        }
    }

    override fun getComponent(): HomeDashboardComponent {
        val baseComponentStub = BaseAppComponentStubInstance.getBaseAppComponentStub(application)
        return DaggerSellerHomeComponentStub.builder()
            .baseAppComponentStub(baseComponentStub)
            .build()
    }
}