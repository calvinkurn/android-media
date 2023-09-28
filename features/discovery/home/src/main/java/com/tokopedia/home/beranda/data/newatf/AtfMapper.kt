package com.tokopedia.home.beranda.data.newatf

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.newatf.AtfMapper.mapToVisitableList
import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerMapper.asVisitable
import com.tokopedia.home.beranda.data.newatf.icon.DynamicIconMapper.asVisitable
import com.tokopedia.home.beranda.data.newatf.mission.MissionWidgetMapper.asVisitable
import com.tokopedia.home.beranda.data.newatf.ticker.TickerMapper.asVisitable
import com.tokopedia.home.beranda.data.newatf.todo.TodoWidgetMapper.asVisitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.domain.model.Ticker
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.model.AtfContent
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData

object AtfMapper {
    fun mapRemoteToDomainAtfData(
        position: Int,
        data: com.tokopedia.home.beranda.data.model.AtfData
    ): AtfData {
        return AtfData(
            atfMetadata = AtfMetadata(
                id = data.id,
                position = position,
                name = data.name,
                component = data.component,
                param = data.param,
                isOptional = data.isOptional,
                isShimmer = data.isShimmer,
            ),
            isCache = false,
        )
    }

    fun mapRemoteToCache(
        position: Int,
        data: com.tokopedia.home.beranda.data.model.AtfData
    ): AtfCacheEntity {
        return AtfCacheEntity(
            id = data.id,
            position = position,
            name = data.name,
            component = data.component,
            param = data.param,
            isOptional = data.isOptional,
            status = AtfKey.STATUS_LOADING,
            isShimmer = data.isShimmer,
        )
    }

    fun mapRemoteToCache(
        atfData: AtfData
    ): AtfCacheEntity {
        return AtfCacheEntity(
            id = atfData.atfMetadata.id,
            position = atfData.atfMetadata.position,
            name = atfData.atfMetadata.name,
            component = atfData.atfMetadata.component,
            param = atfData.atfMetadata.param,
            isOptional = atfData.atfMetadata.isOptional,
            content = atfData.getAtfContentAsJson(),
            status = atfData.atfStatus,
            isShimmer = atfData.atfMetadata.isShimmer,
        )
    }

    fun mapCacheToDomainAtfData(
        data: AtfCacheEntity,
    ): AtfData {
        return AtfData(
             atfMetadata = AtfMetadata(
                 id = data.id,
                 position = data.position,
                 name = data.name,
                 component = data.component,
                 param = data.param,
                 isOptional = data.isOptional,
                 isShimmer = data.isShimmer,
            ),
            atfContent = data.getAtfContent(),
            isCache = true,
            lastUpdate = data.lastUpdate,
        )
    }

    private fun AtfCacheEntity.getAtfContent(): AtfContent? {
        return when(this.component) {
            AtfKey.TYPE_BANNER -> content?.getAtfContent<BannerDataModel>()
            AtfKey.TYPE_ICON -> content?.getAtfContent<DynamicHomeIcon>()
            AtfKey.TYPE_TICKER -> content?.getAtfContent<Ticker>()
            AtfKey.TYPE_CHANNEL -> content?.getAtfContent<DynamicHomeChannel>()
            AtfKey.TYPE_MISSION -> content?.getAtfContent<HomeMissionWidgetData.GetHomeMissionWidget>()
            AtfKey.TYPE_TODO -> content?.getAtfContent<HomeTodoWidgetData.GetHomeTodoWidget>()
            else -> null
        }
    }

    private inline fun <reified T> String.getAtfContent(): T? {
        val gson = Gson()
        return gson.fromJson(this, T::class.java)
    }

    fun AtfDataList.mapToVisitableList(): List<Visitable<*>> {
        val visitables = mutableListOf<Visitable<*>>()
        listAtfData.forEachIndexed { index, value ->
            value.atfContent.run {
                when(this) {
                    is BannerDataModel -> visitables.add(this.asVisitable(index, value.isCache, value.lastUpdate))
                    is DynamicHomeIcon -> visitables.add(this.asVisitable(value.atfMetadata.id, index, value.isCache))
                    is Ticker -> this.asVisitable(index, value.isCache)?.let { visitables.add(it) }
                    is HomeMissionWidgetData.GetHomeMissionWidget -> visitables.add(this.asVisitable(value.atfMetadata, index))
                    is HomeTodoWidgetData.GetHomeTodoWidget -> visitables.add(this.asVisitable(value.atfMetadata, index))
//                    is DynamicHomeChannel -> this.asVisitableList()
                }
            }
        }
        return visitables
    }
}
