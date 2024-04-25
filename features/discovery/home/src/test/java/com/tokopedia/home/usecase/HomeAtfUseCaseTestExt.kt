package com.tokopedia.home.usecase

import com.tokopedia.home.beranda.data.newatf.AtfDataList
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.repository.mockBanner
import com.tokopedia.home.repository.mockChannels
import com.tokopedia.home.repository.mockDynamicIcon
import com.tokopedia.home.repository.mockMission4SquareWidget
import com.tokopedia.home.repository.mockMissionWidget
import com.tokopedia.home.repository.mockTicker
import com.tokopedia.home.repository.mockTodoWidget

fun mockDynamicPositionComplete() = listOf(
    mockTicker(AtfKey.STATUS_SUCCESS, true),
    mockBanner(AtfKey.STATUS_SUCCESS, true),
    mockDynamicIcon(AtfKey.STATUS_SUCCESS, true),
    mockMissionWidget(AtfKey.STATUS_SUCCESS, true),
    mockMission4SquareWidget(AtfKey.STATUS_SUCCESS, true),
    mockTodoWidget(AtfKey.STATUS_SUCCESS, true),
    mockChannels(AtfKey.STATUS_SUCCESS, true)
)

fun mockDynamicPositionSuccess(
    isCache: Boolean = false
) = AtfDataList(
    listAtfData = mockDynamicPositionComplete(),
    isCache = isCache,
    needToFetchComponents = isCache,
    status = AtfDataList.STATUS_SUCCESS
)

fun mockDynamicPositionCacheEmpty() = AtfDataList(
    listAtfData = emptyList(),
    isCache = true,
    needToFetchComponents = true,
    status = AtfDataList.STATUS_SUCCESS
)

fun mockDynamicPositionRemoteFailedCacheExists() = AtfDataList(
    listAtfData = mockDynamicPositionComplete(),
    isCache = true,
    status = AtfDataList.STATUS_ERROR,
    needToFetchComponents = false
)
