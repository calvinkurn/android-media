package com.tokopedia.promocheckout.list

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule

/**
 * @author by resakemal on 04/09/19
 */
object PromoCheckoutListComponentInstance {
    private lateinit var promoCheckoutListComponent: PromoCheckoutListComponent

    fun getPromoCheckoutListComponent(application: Application): PromoCheckoutListComponent {
        if (!::promoCheckoutListComponent.isInitialized) {
            promoCheckoutListComponent = DaggerPromoCheckoutListComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .promoCheckoutListModule(PromoCheckoutListModule())
                    .build()
        }

        return promoCheckoutListComponent
    }
}