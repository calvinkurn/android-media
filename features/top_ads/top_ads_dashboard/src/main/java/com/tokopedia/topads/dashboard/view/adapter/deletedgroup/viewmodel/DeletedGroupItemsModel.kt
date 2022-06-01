package com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.DeletedGroupItemsAdapterTypeFactory

abstract class DeletedGroupItemsModel {
    abstract fun type(typesFactory: DeletedGroupItemsAdapterTypeFactory): Int
}
