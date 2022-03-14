package com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.DeletedGroupItemsAdapterTypeFactory

class DeletedGroupItemsEmptyModel : DeletedGroupItemsModel() {
    override fun type(typesFactory: DeletedGroupItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}
