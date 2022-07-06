package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable

class TokoFoodHomeAdapter(
    typeFactory: TokoFoodHomeAdapterTypeFactory,
    differ: TokoFoodListDiffer
): BaseTokoFoodListAdapter<Visitable<*>, TokoFoodHomeAdapterTypeFactory>(typeFactory, differ)