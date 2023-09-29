package com.tokopedia.home.beranda.data.newatf

import android.util.Log
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class AtfRepository (
    private val atfDao: AtfDao
) {

    private val _flow: MutableStateFlow<AtfData?> = MutableStateFlow(null)
    val flow: StateFlow<AtfData?>
        get() = _flow

    abstract suspend fun getData(atfMetadata: AtfMetadata)

    suspend fun emitData(atfData: AtfData) {
        Log.d("atfflow", "emitData: $flow $atfData")
        _flow.emit(atfData)
    }
}
