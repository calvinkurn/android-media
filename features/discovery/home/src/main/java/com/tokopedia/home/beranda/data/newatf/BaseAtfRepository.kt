package com.tokopedia.home.beranda.data.newatf

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Frenzel
 */
abstract class BaseAtfRepository {

    private val _flow: MutableStateFlow<List<AtfData?>> = MutableStateFlow(emptyList())
    val flow: StateFlow<List<AtfData?>>
        get() = _flow

    abstract suspend fun getData(atfMetadata: AtfMetadata)

    suspend fun emitData(atfData: AtfData) {
        try {
            flow.value.let { currentList ->
                val index = currentList.indexOfFirst { it?.atfMetadata == atfData.atfMetadata }
                val newList = currentList.toMutableList().apply {
                    if(index == -1) add(atfData)
                    else set(index, atfData)
                }
                _flow.emit(newList)
            }
        } catch (_: Exception) { }
    }
}
