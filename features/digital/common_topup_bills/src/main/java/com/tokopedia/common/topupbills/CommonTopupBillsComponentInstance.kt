package com.tokopedia.common.topupbills

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.di.DaggerCommonTopupBillsComponent

/**
 * @author by resakemal on 21/08/19
 */
object CommonTopupBillsComponentInstance {
    private lateinit var hotelComponent: CommonTopupBillsComponent

    fun getCommonTopupBillsComponent(application: Application): CommonTopupBillsComponent {
        if (!::hotelComponent.isInitialized) {
            hotelComponent = DaggerCommonTopupBillsComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }

        return hotelComponent
    }
}