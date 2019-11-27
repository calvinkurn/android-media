package com.tokopedia.officialstore

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.officialstore.common.di.DaggerOfficialStoreComponent
import com.tokopedia.officialstore.common.di.OfficialStoreComponent

class OfficialStoreInstance {
    companion object{
        private var officialStoreComponent: OfficialStoreComponent? = null

        fun getComponent(application: Application): OfficialStoreComponent {
            return officialStoreComponent?.run {
                officialStoreComponent
            } ?: DaggerOfficialStoreComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
    }

}