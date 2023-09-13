package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.coroutines.flow.StateFlow

interface HomeAtfRepository {
    val flow: StateFlow<List<Visitable<*>>>
    suspend fun getData(id: String, cacheStrategy: AtfSource)
    suspend fun getRemote(id: String)
    suspend fun getCache(id: String)
}
