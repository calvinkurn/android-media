package com.tokopedia.libra.domain.usecase

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

    operator fun invoke(owner: LibraOwner, clearAll: Boolean) {
        with(cacheRepository) {
            if (clearAll.not()) clear(owner) else clear()
        }
    }
}
