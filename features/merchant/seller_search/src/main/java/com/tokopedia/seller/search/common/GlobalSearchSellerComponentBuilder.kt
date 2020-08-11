package com.tokopedia.seller.search.common

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.seller.search.common.di.component.DaggerGlobalSearchSellerComponent
import com.tokopedia.seller.search.common.di.component.GlobalSearchSellerComponent

class GlobalSearchSellerComponentBuilder {

    companion object {
        private var searchSellerComponent: GlobalSearchSellerComponent? = null

        fun getComponent(application: Application): GlobalSearchSellerComponent {
            return searchSellerComponent?.run { searchSellerComponent }
                    ?: DaggerGlobalSearchSellerComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}