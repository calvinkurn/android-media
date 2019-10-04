package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.CommissionDetailTypeFactory

/**
 * @author by yoasfs on 2019-08-12
 */

class CommissionDetailItemViewModel (
        val itemSent: Int = 0,
        val affCommission:Int = 0,
        val affCommissionFmt: String = "",
        val affInvoice: String = "",
        val affInvoiceUrl: String = "",
        val txTimeFmt: String = "",
        val txTime: String = "",
        val tkpdInvoice: String = "",
        val tkpdInvoiceUrl: String = "",
        val tkpdCommission: Int = 0,
        val tkpdCommissionFmt: String = "",
        val netCommission: Int = 0,
        val netCommissionFmt: String = ""
) : Visitable<CommissionDetailTypeFactory> {

    override fun type(typeFactory: CommissionDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}