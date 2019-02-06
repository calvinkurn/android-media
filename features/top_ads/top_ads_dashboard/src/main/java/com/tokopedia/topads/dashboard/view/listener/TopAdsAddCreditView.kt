package com.tokopedia.topads.dashboard.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.topads.dashboard.data.model.DataCredit

/**
 * Created by Nathaniel on 11/24/2016.
 */

interface TopAdsAddCreditView : CustomerView {

    fun onCreditListLoaded(creditList: List<DataCredit>)

    fun onLoadCreditListError()
}
