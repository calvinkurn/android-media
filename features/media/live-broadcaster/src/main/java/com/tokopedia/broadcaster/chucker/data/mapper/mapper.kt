package com.tokopedia.broadcaster.chucker.data.mapper

import com.tokopedia.broadcaster.chucker.data.entity.ChuckerLog
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel

fun List<ChuckerLog>.mapToUI(): MutableList<ChuckerLogUIModel> {
    return map {
        ChuckerLogUIModel(
            id = it.id,
            url = it.url,
            connectionId = it.connectionId,
            fps = it.fps,
            bandwidth = it.bandwidth,
            traffic = it.traffic,
            startTime = it.startTime,
            endTime = it.endTime,
            videoWidth = it.videoWidth,
            videoHeight = it.videoHeight,
            videoBitrate = it.videoBitrate,
            audioType = it.audioType,
            audioRate = it.audioRate,
            audioBitrate = it.audioBitrate,
            bitrateMode = it.bitrateMode,
            appVersion = it.appVersion,
            userId = it.userId
        )
    }.toMutableList()
}

fun ChuckerLogUIModel.mapToData(): ChuckerLog {
    return ChuckerLog(
        url = url,
        connectionId = connectionId,
        fps = fps,
        bandwidth = bandwidth,
        traffic = traffic,
        startTime = startTime,
        endTime = endTime,
        videoWidth = videoWidth,
        videoHeight = videoHeight,
        videoBitrate = videoBitrate,
        audioType = audioType,
        audioRate = audioRate,
        audioBitrate = audioBitrate,
        bitrateMode = bitrateMode,
        appVersion = appVersion,
        userId = userId
    )
}