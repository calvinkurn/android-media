package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.CommissionDetailTypeFactory

/**
 * @author by yoasfs on 2019-08-12
 */

class CommissionDetaiItemViewModel (
        val id: Int = 0
) : Visitable<CommissionDetailTypeFactory> {

    override fun type(typeFactory: CommissionDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}