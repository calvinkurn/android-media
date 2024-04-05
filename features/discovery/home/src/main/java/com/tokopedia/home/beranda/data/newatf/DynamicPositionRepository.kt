package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeAtfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Created by Frenzel
 */
@HomeScope
class DynamicPositionRepository @Inject constructor(
    private val atfDao: AtfDao,
    private val atfDataRepository: HomeAtfRepository,
    private val atfMapper: AtfMapper
) {

    private val _cacheFlow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    private val cacheFlow: StateFlow<AtfDataList?>
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
     *      - If data error, expose cached data but with remote status
     *      - If different, then expose remote dynamic position (without content)
     *        and marks as need to fetch components
     */
    val flow: Flow<AtfDataList?> = combine(cacheFlow, remoteFlow) { cache, remote ->
        if (cache != null && remote != null) {
            if (remote.positionEquals(cache)) {
                // if remote position same with cache,
                // populate cache data to remote position
                remote.copyAtfContentsFrom(cache)
            } else if (remote.isDataError()) {
                // when remote failed, show user cached data
                // but still set the status flag as failed
                // to show error toaster
                cache.copy(
                    needToFetchComponents = false,
                    status = remote.status
                )
            } else {
                // IMPORTANT: when remote position is different than cache,
                // always set needToFetchComponents to true because we need
                // to re-fetch every ATF components data from remote
                remote.copy(needToFetchComponents = true)
            }
        } else if(remote != null) {
            // if remote comes faster than cache, need to fetch each data
            remote.copy(needToFetchComponents = true)
        } else {
            cache
        }
    }

    suspend fun getCachedData() {
        val cachedData = AtfDataList(
            listAtfData = atfDao.getAtfDynamicPosition().map(atfMapper::mapCacheToDomainAtfData),
            isCache = true,
            status = AtfDataList.STATUS_SUCCESS,
            needToFetchComponents = true
        )
        _cacheFlow.emit(cachedData)
    }

    suspend fun getRemoteData(isRefresh: Boolean = false) {
        try {
            val listAtf = atfDataRepository.getRemoteData().dataList
            // IMPORTANT: needToFetchComponents value depends on isRefresh flag,
            // because when refresh data, we need to make sure all ATF components re-fetch data from remote
            val remoteData = AtfDataList(
                listAtfData = listAtf.mapIndexed(atfMapper::mapRemoteToDomainAtfData),
                isCache = false,
                status = AtfDataList.STATUS_SUCCESS,
                needToFetchComponents = isRefresh
            )
            _remoteFlow.emit(remoteData)
        } catch (_: Exception) {
            _remoteFlow.emit(
                AtfDataList(
                    listAtfData = emptyList(),
                    isCache = false,
                    status = AtfDataList.STATUS_ERROR,
                    needToFetchComponents = false
                )
            )
        }
    }

    /**
     * Save complete ATF list to cache
     */
    suspend fun saveLatestAtf(value: AtfDataList) {
        atfDao.saveLatestAtf(value.listAtfData.map(atfMapper::mapDomainToCacheEntity))
    }
}
