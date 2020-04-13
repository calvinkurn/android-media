package com.tokopedia.officialstore.official.presentation.dynamic_channel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class MixLeftAdapter(
        typeFactoryImpl: OfficialStoreFlashSaleCardViewTypeFactoryImpl
) : BaseListAdapter<Visitable<*>, OfficialStoreFlashSaleCardViewTypeFactoryImpl>(typeFactoryImpl)
