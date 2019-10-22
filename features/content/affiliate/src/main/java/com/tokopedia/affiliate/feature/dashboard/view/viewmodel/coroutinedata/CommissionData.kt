package com.tokopedia.affiliate.feature.dashboard.view.viewmodel.coroutinedata

import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailItemViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionTransactionViewModel

/**
 * @author by yoasfs on 2019-08-14
 */

data class CommissionData (
    var commissionDetailHeaderViewModel: CommissionDetailHeaderViewModel = CommissionDetailHeaderViewModel(),
    var commissionTransactionViewModel: CommissionTransactionViewModel = CommissionTransactionViewModel()
)