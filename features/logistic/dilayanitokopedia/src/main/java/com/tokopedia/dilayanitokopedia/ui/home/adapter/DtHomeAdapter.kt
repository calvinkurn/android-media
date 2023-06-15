package com.tokopedia.dilayanitokopedia.ui.home.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.common.view.adapter.base.BaseDtListAdapter

class DtHomeAdapter(
    typeFactory: DtHomeAdapterTypeFactory,
    differ: HomeListDiffer
) : BaseDtListAdapter<Visitable<*>, DtHomeAdapterTypeFactory>(typeFactory, differ)
