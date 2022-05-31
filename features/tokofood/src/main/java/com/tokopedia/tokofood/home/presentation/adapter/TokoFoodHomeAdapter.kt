package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable

class TokoFoodHomeAdapter(
    typeFactory: TokoFoodHomeAdapterTypeFactory,
    differ: TokoFoodListDiffer
): BaseTokoFoodListAdapter<Visitable<*>, TokoFoodHomeAdapterTypeFactory>(typeFactory, differ)