package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.presentation.adapter.BaseTokoFoodListAdapter

class TokoFoodHomeAdapter(
    typeFactory: TokoFoodHomeAdapterTypeFactory,
    differ: TokoFoodListDiffer
): BaseTokoFoodListAdapter<Visitable<*>, TokoFoodHomeAdapterTypeFactory>(typeFactory, differ)