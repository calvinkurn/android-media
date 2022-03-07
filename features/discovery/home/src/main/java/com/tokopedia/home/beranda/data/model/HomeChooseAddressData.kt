package com.tokopedia.home.beranda.data.model

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class HomeChooseAddressData(
        var isActive: Boolean = true,
        var localCacheModel: LocalCacheModel = LocalCacheModel()
) {
    fun setLocalCacheModel(localCacheModel: LocalCacheModel?): HomeChooseAddressData {
        localCacheModel?.let {
            this.localCacheModel = localCacheModel
        }
        return  this
    }

    fun toLocalCacheModel(): LocalCacheModel {
        return localCacheModel
    }
}