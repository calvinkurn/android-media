package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.CommissionDetailTypeFactory

/**
 * @author by yoasfs on 2019-08-12
 */

data class CommissionDetailHeaderViewModel (
        val price: Int = 0,
        val priceFmt: String = "",
        val isActive: Boolean = false,
        val commission: Int = 0,
        val commissionFmt: String = "",
        val totalClick:Int = 0,
        val totalSold: Int = 0,
        val totalCommission: Int = 0,
        val totalCOmmissionFmt: String = "",
        val shopId: String = "",
        val shopName: String = "",
        val productId: Int = 0,
        val productName: String = "",
        val productImg: String = ""
) : Visitable<CommissionDetailTypeFactory> {
    override fun type(typeFactory: CommissionDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}