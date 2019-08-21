package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.CommissionDetailTypeFactory

/**
 * @author by yoasfs on 2019-08-15
 */
data class EmptyCommissionViewModel(
        val iconRes: String = "https://ecs7.tokopedia.net/img/android/affiliate/xxhdpi/empty_commission.png"
): Visitable<CommissionDetailTypeFactory> {
    override fun type(typeFactory: CommissionDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}