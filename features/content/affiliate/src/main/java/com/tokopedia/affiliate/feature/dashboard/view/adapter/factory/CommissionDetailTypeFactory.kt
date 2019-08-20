package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailItemViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyCommissionViewModel

/**
 * @author by yoasfs on 2019-08-12
 */
interface CommissionDetailTypeFactory {

    fun type(commissionDetailHeaderViewModel: CommissionDetailHeaderViewModel): Int

    fun type(commissionDetailItemViewModel: CommissionDetailItemViewModel): Int

    fun type(emptyCommissionViewModel: EmptyCommissionViewModel): Int
}