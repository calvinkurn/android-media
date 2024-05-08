package com.tokopedia.libra.domain

import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.data.repository.CacheRepository
import com.tokopedia.libra.domain.model.LibraUiModel
import javax.inject.Inject

class GetLibraCacheUseCase @Inject constructor(
    private val cacheRepository: CacheRepository
) {

    operator fun invoke(owner: LibraOwner): LibraUiModel {
        return cacheRepository.get(owner)
    }
}
