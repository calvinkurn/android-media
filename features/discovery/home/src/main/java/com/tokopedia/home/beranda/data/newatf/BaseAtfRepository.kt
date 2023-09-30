package com.tokopedia.home.beranda.data.newatf

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Frenzel
 */
abstract class BaseAtfRepository {

    private val _flow: MutableStateFlow<AtfData?> = MutableStateFlow(null)
    val flow: StateFlow<AtfData?>
        get() = _flow

    abstract suspend fun getData(atfMetadata: AtfMetadata)

    suspend fun emitData(atfData: AtfData) {
        _flow.emit(atfData)
    }
}
