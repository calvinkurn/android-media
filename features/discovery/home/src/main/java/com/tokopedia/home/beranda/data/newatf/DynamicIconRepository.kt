package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DynamicIconRepository @Inject constructor(
    private val atfDao: AtfDao,
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
    }

    override suspend fun getCache(id: String) {
        val data = atfDao.getAtfData(id)
    }

}
