package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeAtfRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Frenzel
 */
@HomeScope
class DynamicPositionRepository @Inject constructor(
    private val homeDispatcher: CoroutineDispatchers,
    private val atfDao: AtfDao,
    private val atfDataRepository: HomeAtfRepository,
    private val atfMapper: AtfMapper,
) {

    private val _cacheFlow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    val cacheFlow: StateFlow<AtfDataList?>
        get() = _cacheFlow

    private val _remoteFlow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    private val remoteFlow: StateFlow<AtfDataList?>
        get() = _remoteFlow

    /**
     * A mediated flow originated from cache & remote source.
     * - On initial state, always expose cached dynamic position
     * - If both cache and remote ready, validate the position.
     *      - If cached position == remote position:
     *          - Merged the two by inserting cached ATF content to remote position
     *          - Expose the combined flow to collector
     *      - If different, then expose remote dynamic position (without content)
     */
    val flow: Flow<AtfDataList?> = combine(cacheFlow, remoteFlow) { cache, remote ->
        if(cache != null && remote != null) {
            if(remote.positionEquals(cache)) {
                remote.copyAtfContentsFrom(cache)
            }
            else {
                // IMPORTANT: when remote position is different than cache,
                // always set needToFetchComponents to true because we need
                // to re-fetch every ATF components data from remote
                remote.copy(needToFetchComponents = true)
            }
        } else cache
    }

    /**
     * Get cached and remote data in parallel
     */
    suspend fun getData() {
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
            listAtfData = atfDao.getAtfDynamicPosition().map(atfMapper::mapCacheToDomainAtfData),
            isCache = true,
            status = AtfDataList.STATUS_SUCCESS,
            needToFetchComponents = true,
        )
        _cacheFlow.emit(cachedData)
    }

    suspend fun getRemoteData(isRefresh: Boolean = false): Boolean {
        try {
            val listAtf = atfDataRepository.getRemoteData().dataList
            // IMPORTANT: needToFetchComponents value depends on isRefresh flag,
            // because when refresh data, we need to make sure all ATF components re-fetch data from remote
            val remoteData = AtfDataList(
                listAtfData = listAtf.mapIndexed(atfMapper::mapRemoteToDomainAtfData),
                isCache = false,
                status = AtfDataList.STATUS_SUCCESS,
                needToFetchComponents = isRefresh,
            )
            _remoteFlow.emit(remoteData)
            return true
        } catch (_: Exception) {
            _remoteFlow.emit(
                AtfDataList(
                    listAtfData = emptyList(),
                    isCache = false,
                    status = AtfDataList.STATUS_ERROR,
                    needToFetchComponents = false,
                )
            )
        }
        return false
    }

    /**
     * Save complete ATF list to cache
     */
    suspend fun saveLatestAtf(value: AtfDataList) {
        atfDao.saveLatestAtf(value.listAtfData.map(atfMapper::mapDomainToCacheEntity))
    }
}
