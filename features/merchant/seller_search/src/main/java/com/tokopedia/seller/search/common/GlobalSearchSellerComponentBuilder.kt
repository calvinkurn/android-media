package com.tokopedia.seller.search.common

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.seller.search.common.di.component.GlobalSearchSellerComponent

class ReviewSellerComponentBuilder {

    companion object {
        private var reviewSellerComponent: GlobalSearchSellerComponent? = null

        fun getComponent(application: Application): GlobalSearchSellerComponent {
            return reviewSellerComponent?.run { reviewSellerComponent }
                    ?: DaggerGlobalSearchSellerComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}