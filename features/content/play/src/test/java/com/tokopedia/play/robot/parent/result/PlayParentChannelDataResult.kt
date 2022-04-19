package com.tokopedia.play.robot.parent.result

import com.tokopedia.play.view.storage.PlayChannelData
import org.assertj.core.api.Assertions
import kotlin.properties.Delegates

/**
 * Created by jegul on 10/02/21
 */
class PlayParentChannelDataResult {

    constructor(result: PlayChannelData) {
        isError = false
        mData = result
    }

    constructor(error: Throwable) {
        isError = true
        mData = null
    }

    private var isError by Delegates.notNull<Boolean>()
    private var mData: PlayChannelData? = null

    fun getData(): PlayChannelData = mData ?: error("Data not available")

    fun isAvailable(): PlayParentChannelDataResult {
        Assertions
                .assertThat(isError)
                .isFalse

        return this
    }

    fun isError(): PlayParentChannelDataResult {
        Assertions
                .assertThat(isError)
                .isTrue

        return this
    }

    fun isDataEqualTo(data: PlayChannelData): PlayParentChannelDataResult {
        Assertions
                .assertThat(mData)
                .isEqualTo(data)

        return this
    }

    fun isDataNotEqualTo(data: PlayChannelData): PlayParentChannelDataResult {
        Assertions
                .assertThat(mData)
                .isNotEqualTo(data)

        return this
    }
}