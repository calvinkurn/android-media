package com.tokopedia.tokopedianow.common.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowChipListDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChipListAdapterTypeFactory
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class TokoNowChipListAdapter(
    typeFactory: TokoNowChipListAdapterTypeFactory
) : BaseTokopediaNowListAdapter<Visitable<*>, TokoNowChipListAdapterTypeFactory>(
    typeFactory,
    TokoNowChipListDiffer()
)