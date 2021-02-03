package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.emptystate.EmptyStateModel
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository

const val TITLE = "Cari dengan kurangi filternya dulu, yuk!"
const val DESCRIPTION = "Produk tidak ditemukan. Coba cari dengan mengurangi filter yang sedang aktif."

class CategoryEmptyStateRepository : EmptyStateRepository {
    override fun getEmptyStateData(component: ComponentsItem): EmptyStateModel {
        return EmptyStateModel(isHorizontal = true,
                title = TITLE,
                description = DESCRIPTION)
    }
}