package com.tokopedia.libra

import android.content.Context
import com.tokopedia.libra.domain.model.LibraUiModel
import com.tokopedia.libra.domain.usecase.GetLibraCacheUseCase
import com.tokopedia.libra.domain.usecase.GetLibraRemoteUseCase
import javax.inject.Inject

class DebugLibraInstance @Inject constructor(
    context: Context,
    private var remoteUseCase: GetLibraRemoteUseCase,
    private val cacheUseCase: GetLibraCacheUseCase
) : Libra by LibraInstance(context) {

    suspend fun variants(owner: LibraOwner): LibraUiModel {
        return remoteUseCase(owner)
    }

    fun variantFromCache(owner: LibraOwner): LibraUiModel {
        return cacheUseCase(owner)
    }
}
