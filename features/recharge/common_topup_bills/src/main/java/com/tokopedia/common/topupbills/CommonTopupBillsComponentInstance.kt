package com.tokopedia.common.topupbills

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.di.DaggerCommonTopupBillsComponent
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent
import com.tokopedia.common_digital.common.di.DigitalCommonComponent

/**
 * @author by resakemal on 21/08/19
 */
object CommonTopupBillsComponentInstance {

    fun getCommonTopupBillsComponent(application: Application): CommonTopupBillsComponent {
        val commonTopupBillsComponent: CommonTopupBillsComponent by lazy {
            val digitalCommonComponent = DaggerDigitalCommonComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
            DaggerCommonTopupBillsComponent.builder()
                    .digitalCommonComponent(digitalCommonComponent)
                    .build()
        }
        return commonTopupBillsComponent
    }
}