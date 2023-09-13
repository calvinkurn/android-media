package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.model.AtfData

object AtfMapper {
    fun AtfData.mapToNewAtfData(): AtfMetadata {
        return AtfMetadata(
            id = id,
            name = name,
            component = component,
            param = param,
            isOptional = isOptional,
        )
    }

    fun AtfCacheEntity.mapToNewAtfData(): AtfMetadata {
        return AtfMetadata(
            id = id,
            name = name,
            component = component,
            param = param,
            isOptional = isOptional,
        )
    }
}
