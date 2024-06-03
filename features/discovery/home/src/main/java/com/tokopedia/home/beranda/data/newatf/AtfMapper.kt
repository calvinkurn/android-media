package com.tokopedia.home.beranda.data.newatf

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.model.GetTargetedTicker
import com.tokopedia.home.beranda.data.newatf.balance.BalanceWidgetMapper
import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerMapper
import com.tokopedia.home.beranda.data.newatf.channel.AtfChannelMapper
import com.tokopedia.home.beranda.data.newatf.icon.DynamicIconMapper
import com.tokopedia.home.beranda.data.newatf.mission.MissionWidgetMapper
import com.tokopedia.home.beranda.data.newatf.shorten.ShortenWidgetMapper
import com.tokopedia.home.beranda.data.newatf.ticker.mapper.TargetedTickerMapper
import com.tokopedia.home.beranda.data.newatf.ticker.mapper.TickerMapper
import com.tokopedia.home.beranda.data.newatf.todo.TodoWidgetMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.domain.model.Ticker
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.model.AtfContent
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home.beranda.data.newatf.balance.DynamicBalanceWidgetModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.home.beranda.data.model.AtfData as OldAtfData
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class AtfMapper @Inject constructor(
    private val homepageBannerMapper: HomepageBannerMapper,
    private val dynamicIconMapper: DynamicIconMapper,
    private val tickerMapper: TickerMapper,
    private val atfChannelMapper: AtfChannelMapper,
    private val missionWidgetMapper: MissionWidgetMapper,
    private val todoWidgetMapper: TodoWidgetMapper,
    private val balanceWidgetMapper: BalanceWidgetMapper,
) {
    fun mapRemoteToDomainAtfData(
        position: Int,
        data: OldAtfData
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
            style = AtfData.AtfStyle(
                isBleeding = data.style?.isBleeding.orFalse(),
                heightRatio = data.style?.heightRatio.orZero(),
                widthRatio = data.style?.widthRatio.orZero(),
                gradientColor = data.style?.gradientColor ?: emptyList(),
            ),
            isCache = false,
        )
    }

    fun mapCacheToDomainAtfData(
        data: AtfCacheEntity,
    ): AtfData {
        val style = safe(OldAtfData.AtfStyle()) {
            Gson().fromJson(data.style, OldAtfData.AtfStyle::class.java)
        }

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
            atfStatus = data.status,
            isCache = true,
            lastUpdate = data.lastUpdate,
            style = AtfData.AtfStyle(
                isBleeding = style.isBleeding,
                heightRatio = style.heightRatio,
                widthRatio = style.widthRatio,
                gradientColor = style.gradientColor,
            ),
        )
    }

    fun mapDomainToCacheEntity(
        data: AtfData,
    ): AtfCacheEntity {
        val style = safe("") { Gson().toJson(data.style) }

        return AtfCacheEntity(
            id = data.atfMetadata.id,
            position = data.atfMetadata.position,
            name = data.atfMetadata.name,
            component = data.atfMetadata.component,
            param = data.atfMetadata.param,
            isOptional = data.atfMetadata.isOptional,
            content = data.getAtfContentAsJson(),
            status = data.atfStatus,
            isShimmer = data.atfMetadata.isShimmer,
            lastUpdate = data.lastUpdate,
            style = style
        )
    }

    private fun AtfCacheEntity.getAtfContent(): AtfContent? {
        return when(this.component) {
            AtfKey.TYPE_BANNER, AtfKey.TYPE_BANNER_V2 -> content?.getAtfContent<BannerDataModel>()
            AtfKey.TYPE_ICON, AtfKey.TYPE_ICON_V2 -> content?.getAtfContent<DynamicHomeIcon>()
            AtfKey.TYPE_TICKER -> content?.getAtfContent<GetTargetedTicker>() ?: content?.getAtfContent<Ticker>()
            AtfKey.TYPE_HORIZONTAL,
            AtfKey.TYPE_CHANNEL -> content?.getAtfContent<DynamicHomeChannel>()
            AtfKey.TYPE_MISSION,
            AtfKey.TYPE_MISSION_V2,
            AtfKey.TYPE_MISSION_V3 -> content?.getAtfContent<HomeMissionWidgetData.GetHomeMissionWidget>()
            AtfKey.TYPE_TODO -> content?.getAtfContent<HomeTodoWidgetData.GetHomeTodoWidget>()
            AtfKey.TYPE_BALANCE -> content?.getAtfContent<DynamicBalanceWidgetModel>()
            else -> null
        }
    }

    private inline fun <reified T> String.getAtfContent(): T? {
        val gson = Gson()
        return gson.fromJson(this, T::class.java)
    }

    fun mapToVisitableList(data: AtfDataList?): List<Visitable<*>> {
        val visitables = mutableListOf<Visitable<*>>()
        data?.listAtfData?.forEachIndexed { index, value ->
            value.atfContent?.run {
                when(this) {
                    is BannerDataModel -> visitables.add(homepageBannerMapper.asVisitable(this, index, value))
                    is DynamicHomeIcon -> visitables.add(dynamicIconMapper.asVisitable(this, value))
                    is Ticker -> tickerMapper.asVisitable(this, value)?.let { visitables.add(it) }
                    is GetTargetedTicker -> TargetedTickerMapper.asVisitable(this, value)?.let { visitables.add(it) }
                    is HomeMissionWidgetData.GetHomeMissionWidget -> visitables.add(missionWidgetMapper.asVisitable(this, index, value))
                    is HomeTodoWidgetData.GetHomeTodoWidget -> todoWidgetMapper.asVisitable(this, index, value)?.let { visitables.add(it) }
                    is DynamicHomeChannel -> {
                        when (value.atfMetadata.component) {
                            AtfKey.TYPE_CHANNEL -> visitables.addAll(atfChannelMapper.asVisitableList(this, index, value))
                            AtfKey.TYPE_HORIZONTAL -> visitables.add(ShortenWidgetMapper.to2SquareUiModel(this, value, index))
                        }
                    }
                    is DynamicBalanceWidgetModel -> visitables.add(balanceWidgetMapper.asVisitable(this, value.atfStatus))
                }
            }
        }
        return visitables
    }

    private fun <T> safe(default: T, invoke: () -> T): T {
        return try { invoke() } catch (_: Throwable) { default }
    }
}
