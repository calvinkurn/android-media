package com.tokopedia.affiliate.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.AFFILIATE_TRAFFIC_ADB
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetTypeFactory
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.remoteconfig.RemoteConfigInstance
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateRecyclerViewModel @Inject constructor() : BaseViewModel() {
    fun isFeatureWhiteListed(): Boolean {
        return when (RemoteConfigInstance.getInstance().abTestPlatform.getString(
            AFFILIATE_TRAFFIC_ADB,
            ""
        )) {
            AFFILIATE_TRAFFIC_ADB -> true
            else -> false
        }
    }

    val itemList: ArrayList<Visitable<AffiliateBottomSheetTypeFactory>> = ArrayList()
}
