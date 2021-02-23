package com.tokopedia.home.beranda.data.model

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class HomeChooseAddressData(
        var isActive: Boolean = true,
        val localCacheModel: LocalCacheModel? = null
)