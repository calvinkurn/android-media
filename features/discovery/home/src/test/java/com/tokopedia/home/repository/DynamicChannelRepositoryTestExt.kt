package com.tokopedia.home.repository

import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMapper
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.DynamicPositionRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeAtfRepository
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.domain.model.Ticker
import com.tokopedia.home.beranda.domain.model.Tickers
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import io.mockk.mockk

fun createDynamicPositionRepository(
    atfDao: AtfDao = mockk(relaxed = true),
    atfDataRepository: HomeAtfRepository = mockk(relaxed = true),
    atfMapper: AtfMapper = mockk(relaxed = true)
): DynamicPositionRepository {
    return DynamicPositionRepository(
        atfDao = atfDao,
        atfDataRepository = atfDataRepository,
        atfMapper = atfMapper
    )
}

fun mockTicker(
    status: Int = AtfKey.STATUS_SUCCESS,
    isCache: Boolean = false,
    position: Int = 1
) = AtfData(
    atfMetadata = AtfMetadata(
        id = 1,
        name = "ticker",
        position = position,
        component = AtfKey.TYPE_TICKER,
        param = "",
        isOptional = false,
        isShimmer = false
    ),
    atfContent = if (status == AtfKey.STATUS_SUCCESS) Ticker(listOf(Tickers(), Tickers())) else null,
    atfStatus = status,
    isCache = isCache
)

fun mockBanner(
    status: Int = AtfKey.STATUS_SUCCESS,
    isCache: Boolean = false,
    position: Int = 2
) = AtfData(
    atfMetadata = AtfMetadata(
        id = 2,
        name = "banner",
        position = position,
        component = AtfKey.TYPE_BANNER,
        param = "",
        isOptional = false,
        isShimmer = false
    ),
    atfContent = if (status == AtfKey.STATUS_SUCCESS) BannerDataModel(slides = listOf(BannerSlidesModel(), BannerSlidesModel())) else null,
    atfStatus = status,
    isCache = isCache
)

fun mockDynamicIcon(
    status: Int = AtfKey.STATUS_SUCCESS,
    isCache: Boolean = false,
    position: Int = 3
) = AtfData(
    atfMetadata = AtfMetadata(
        id = 3,
        name = "icon",
        position = position,
        component = AtfKey.TYPE_ICON,
        param = "",
        isOptional = false,
        isShimmer = false
    ),
    atfContent = if (status == AtfKey.STATUS_SUCCESS) {
        DynamicHomeIcon(
            dynamicIcon = listOf(
                DynamicHomeIcon.DynamicIcon(),
                DynamicHomeIcon.DynamicIcon()
            )
        )
    } else {
        null
    },
    atfStatus = status,
    isCache = isCache
)

fun mockMissionWidget(
    status: Int = AtfKey.STATUS_SUCCESS,
    isCache: Boolean = false,
    position: Int = 4
) = AtfData(
    atfMetadata = AtfMetadata(
        id = 5,
        name = "mission",
        position = position,
        component = AtfKey.TYPE_MISSION,
        param = "",
        isOptional = false,
        isShimmer = false
    ),
    atfContent = if (status == AtfKey.STATUS_SUCCESS) {
        HomeMissionWidgetData.GetHomeMissionWidget(
            missions = listOf(
                HomeMissionWidgetData.Mission(),
                HomeMissionWidgetData.Mission()
            )
        )
    } else {
        null
    },
    atfStatus = status,
    isCache = isCache
)

fun mockTodoWidget(
    status: Int = AtfKey.STATUS_SUCCESS,
    isCache: Boolean = false,
    position: Int = 5
) = AtfData(
    atfMetadata = AtfMetadata(
        id = 4,
        name = "todo",
        position = position,
        component = AtfKey.TYPE_TODO,
        param = "",
        isOptional = false,
        isShimmer = false
    ),
    atfContent = if (status == AtfKey.STATUS_SUCCESS) {
        HomeTodoWidgetData.GetHomeTodoWidget(
            todos = listOf(
                HomeTodoWidgetData.Todo(),
                HomeTodoWidgetData.Todo()
            )
        )
    } else {
        null
    },
    atfStatus = status,
    isCache = isCache
)

fun mockChannels(
    status: Int = AtfKey.STATUS_SUCCESS,
    isCache: Boolean = false,
    position: Int = 6
) = AtfData(
    atfMetadata = AtfMetadata(
        id = 6,
        name = "channel",
        position = position,
        component = AtfKey.TYPE_CHANNEL,
        param = "",
        isOptional = false,
        isShimmer = false
    ),
    atfContent = if (status == AtfKey.STATUS_SUCCESS) {
        DynamicHomeChannel(
            channels = listOf(
                DynamicHomeChannel.Channels(id = "123", layout = DynamicHomeChannel.Channels.LAYOUT_VPS_WIDGET),
                DynamicHomeChannel.Channels(id = "234", layout = DynamicHomeChannel.Channels.LAYOUT_MIX_LEFT)
            )
        )
    } else {
        null
    },
    atfStatus = status,
    isCache = isCache
)

fun mockMission4SquareWidget(
    status: Int = AtfKey.STATUS_SUCCESS,
    isCache: Boolean = false,
    position: Int = 7
) = AtfData(
    atfMetadata = AtfMetadata(
        id = 7,
        name = "mission_widget_v3",
        position = position,
        component = AtfKey.TYPE_MISSION_V3,
        param = "",
        isOptional = false,
        isShimmer = false
    ),
    atfContent = if (status == AtfKey.STATUS_SUCCESS) {
        HomeMissionWidgetData.GetHomeMissionWidget(
            missions = listOf(
                HomeMissionWidgetData.Mission(),
                HomeMissionWidgetData.Mission()
            )
        )
    } else {
        null
    },
    atfStatus = status,
    isCache = isCache
)
