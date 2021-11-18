package com.tokopedia.broadcaster.statsnerd.data.mapper

import com.tokopedia.broadcaster.statsnerd.data.entity.StatsNerdLog
import com.tokopedia.broadcaster.uimodel.LoggerUIModel

fun List<StatsNerdLog>.mapToUI(): MutableList<LoggerUIModel> {
    return map {
        LoggerUIModel(
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
            userId = it.userId,
            isPacketLossIncreasing = it.packetLossIncrease
        )
    }.toMutableList()
}

fun LoggerUIModel.mapToData(): StatsNerdLog {
    return StatsNerdLog(
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
        userId = userId,
        packetLossIncrease = isPacketLossIncreasing
    )
}