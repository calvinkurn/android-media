package com.tokopedia.universal_sharing.view.bottomsheet.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactory

class UniversalSharingPostPurchaseAdapter(
    typeFactory: UniversalSharingTypeFactory
) : BaseListAdapter<Visitable<in UniversalSharingTypeFactory>, UniversalSharingTypeFactory>(
    typeFactory
) {

}
