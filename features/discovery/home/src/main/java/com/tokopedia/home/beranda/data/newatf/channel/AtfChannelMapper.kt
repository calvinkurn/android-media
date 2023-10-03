package com.tokopedia.home.beranda.data.newatf.channel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class AtfChannelMapper @Inject constructor(
    private val dynamicChannelDataMapper: HomeDynamicChannelDataMapper,
) {

    fun asVisitableList(
        data: DynamicHomeChannel,
        index: Int,
        atfData: AtfData,
    ): List<Visitable<*>> {
        return dynamicChannelDataMapper.mapToDynamicChannelDataModel(
            HomeChannelData(data),
            isCache = atfData.isCache,
            addLoadingMore = false,
            useDefaultWhenEmpty = false,
        )
    }
}
