package com.tokopedia.affiliate.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetTypeFactory
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateRecyclerViewModel @Inject constructor() : BaseViewModel() {
    val itemList: ArrayList<Visitable<AffiliateBottomSheetTypeFactory>> = ArrayList()
}
