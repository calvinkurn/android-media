package com.tokopedia.home.beranda.data.newatf.position

import android.util.Log
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfDataList
import com.tokopedia.home.beranda.data.newatf.AtfMapper
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeAtfRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HomeScope
class DynamicPositionRepository @Inject constructor(
    private val homeDispatcher: CoroutineDispatchers,
    private val atfDao: AtfDao,
    private val atfDataRepository: HomeAtfRepository,
) {
    companion object {
        private const val FLOW_TIMEOUT_MILLIS = 5000L
    }

    private val coroutineScope = CoroutineScope(homeDispatcher.io)

    private val _cacheFlow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    private val cacheFlow: StateFlow<AtfDataList?>
        get() = _cacheFlow.stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
            null
        )

    private val _remoteFlow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    private val remoteFlow: StateFlow<AtfDataList?>
        get() = _remoteFlow.stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
            null
        )

    /**
     * A mediated flow originated from cache & remote source.
     * - On initial state, always expose cached dynamic position
     * - If both cache and remote ready, validate the position.
     *      - If cached position == remote position:
     *          - Merged the two by inserting cached ATF content to remote position
     *          - Expose the combined flow to collector
     *      = If different, then expose remote dynamic position (without content)
     */
    val flow: StateFlow<AtfDataList?> = combine(cacheFlow, remoteFlow) { cache, remote ->
        if(cache != null && remote != null) {
            if(remote.positionEquals(cache))
                remote.copyAtfContentsFrom(cache)
            else remote
        } else cache
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
        null
    )

    /**
     * Get cached and remote data in parallel
     */
    suspend fun getData() {
        Log.d("atfflow", "4. DynamicPositionRepository - getData")
        coroutineScope {
            launch(homeDispatcher.io) {
                getCachedData()
            }
            launch(homeDispatcher.io) {
                getRemoteData()
            }
        }
    }

    private suspend fun getCachedData() {
        val cachedData = AtfDataList(
            listAtfData = atfDao.getAtfDynamicPosition().map(AtfMapper::mapCacheToDomainAtfData),
            isCache = true,
            status = AtfDataList.STATUS_SUCCESS,
            needToFetchComponents = true,
        )
        _cacheFlow.emit(cachedData)
        Log.d("atfflow", "4a. DynamicPositionRepository - getCachedData $cachedData")
    }

    suspend fun getRemoteData() {
        try {
            val listAtf = atfDataRepository.getRemoteData().dataList
            val remoteData = AtfDataList(
                listAtfData = listAtf.mapIndexed(AtfMapper::mapRemoteToDomainAtfData),
                isCache = false,
                status = AtfDataList.STATUS_SUCCESS,
                needToFetchComponents = true,
            )
            _remoteFlow.emit(remoteData)
            Log.d("atfflow", "4b. DynamicPositionRepository - getRemoteData $remoteData")
        } catch (_: Exception) {
            Log.d("atfflow", "4b. DynamicPositionRepository - error catched")
            _remoteFlow.emit(
                AtfDataList(
                    listAtfData = emptyList(),
                    isCache = false,
                    status = AtfDataList.STATUS_ERROR,
                    needToFetchComponents = false,
                )
            )
        }
    }

    /**
     * Save complete ATF list to cache
     */
    suspend fun saveLatestAtf(value: AtfDataList) {
        atfDao.saveLatestAtf(value.listAtfData.map(AtfMapper::mapDomainToCacheEntity))
    }
}
