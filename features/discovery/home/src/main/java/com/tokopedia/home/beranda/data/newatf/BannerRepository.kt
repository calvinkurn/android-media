package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.domain.interactor.repository.HomePageBannerRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BannerRepository @Inject constructor(
    private val atfDao: AtfDao,
    private val homePageBannerRepository: HomePageBannerRepository,
): HomeAtfRepository {
    override val flow: StateFlow<List<Visitable<*>>>
        get() = _flow
    private var _flow: MutableStateFlow<List<Visitable<*>>> = MutableStateFlow(emptyList())

    override suspend fun getData(id: String, cacheStrategy: AtfSource) {
        coroutineScope {
            if(cacheStrategy == AtfSource.REMOTE) {
                launch { getRemote(id) }
            } else {
                launch { getCache(id) }
                launch { getRemote(id) }
            }
        }
    }

    override suspend fun getRemote(id: String) {
        val data = homePageBannerRepository.getRemoteData()

    }

    override suspend fun getCache(id: String) {
        val data = atfDao.getAtfData(id)
    }

}
