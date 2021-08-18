package com.tokopedia.broadcaster.chucker.data.mapper

import com.tokopedia.broadcaster.chucker.data.entity.ChuckerLog
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel

fun ChuckerLog.mapToUI(): ChuckerLogUIModel {
    return ChuckerLogUIModel(
        connectionId = connectionId,
        startTime = startTime,
        endTime = endTime,
        videoWidth = videoWidth,
        videoHeight = videoHeight,
        videoBitrate = videoBitrate,
        audioType = audioType,
        audioRate = audioRate,
        bitrateMode = bitrateMode,
        ipDevice = ipDevice,
        appVersion = appVersion,
        userId = userId
    )
}

fun List<ChuckerLog>.mapToUI(): List<ChuckerLogUIModel> {
    return map {
        ChuckerLogUIModel(
            connectionId = it.connectionId,
            startTime = it.startTime,
            endTime = it.endTime,
            videoWidth = it.videoWidth,
            videoHeight = it.videoHeight,
            videoBitrate = it.videoBitrate,
            audioType = it.audioType,
            audioRate = it.audioRate,
            bitrateMode = it.bitrateMode,
            ipDevice = it.ipDevice,
            appVersion = it.appVersion,
            userId = it.userId
        )
    }.toList()
}

fun ChuckerLogUIModel.mapToData(): ChuckerLog {
    return ChuckerLog(
        connectionId = connectionId,
        startTime = startTime,
        endTime = endTime,
        videoWidth = videoWidth,
        videoHeight = videoHeight,
        videoBitrate = videoBitrate,
        audioType = audioType,
        audioRate = audioRate,
        bitrateMode = bitrateMode,
        ipDevice = ipDevice,
        appVersion = appVersion,
        userId = userId
    )
}

fun List<ChuckerLogUIModel>.mapToData(): List<ChuckerLog> {
    return map {
        ChuckerLog(
            connectionId = it.connectionId,
            startTime = it.startTime,
            endTime = it.endTime,
            videoWidth = it.videoWidth,
            videoHeight = it.videoHeight,
            videoBitrate = it.videoBitrate,
            audioType = it.audioType,
            audioRate = it.audioRate,
            bitrateMode = it.bitrateMode,
            ipDevice = it.ipDevice,
            appVersion = it.appVersion,
            userId = it.userId
        )
    }.toList()
}