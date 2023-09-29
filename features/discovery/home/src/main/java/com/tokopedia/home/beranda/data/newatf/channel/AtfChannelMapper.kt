package com.tokopedia.home.beranda.data.newatf.channel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class AtfChannelMapper @Inject constructor(
    private val dynamicChannelDataMapper: HomeDynamicChannelDataMapper,
) {

    fun DynamicHomeChannel.asVisitableList(
        index: Int,
        isCache: Boolean
    ): List<Visitable<*>> {
        return dynamicChannelDataMapper.mapToDynamicChannelDataModel(
            HomeChannelData(this),
            isCache = isCache,
            addLoadingMore = false,
            useDefaultWhenEmpty = false,
        )
    }
}
