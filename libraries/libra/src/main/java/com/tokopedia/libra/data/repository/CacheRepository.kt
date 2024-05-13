package com.tokopedia.libra.data.repository

import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.domain.model.LibraUiModel

interface CacheRepository {

    fun save(owner: LibraOwner, data: LibraUiModel)
    fun get(owner: LibraOwner): LibraUiModel
    fun clear(owner: LibraOwner)
}
