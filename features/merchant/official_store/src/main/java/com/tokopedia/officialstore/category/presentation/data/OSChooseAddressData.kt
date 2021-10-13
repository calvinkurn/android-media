package com.tokopedia.officialstore.category.presentation.data

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class OSChooseAddressData(
        var isActive: Boolean = true,
        var localCacheModel: LocalCacheModel = LocalCacheModel()
) {
    fun setLocalCacheModel(localCacheModel: LocalCacheModel?): OSChooseAddressData {
        localCacheModel?.let {
            this.localCacheModel = localCacheModel
        }
        return  this
    }

    fun toLocalCacheModel(): LocalCacheModel {
        return localCacheModel
    }
}