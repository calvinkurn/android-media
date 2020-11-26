package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.android.parcel.Parcelize

/**
 * @author : Steven 19/06/19
 */
@Parcelize
class VideoStreamViewModel constructor(var isActive: Boolean = false,
                                       var isLive: Boolean = false,
                                       var androidStreamHD: String = "",
                                       var androidStreamSD: String = "",
                                       var iosStreamHD: String = "",
                                       var iosStreamSD: String = "",
                                       var orientation: String = ORIENTATION_VERTICAL)
    : Visitable<Any>, Parcelable {

    companion object {
        const val TYPE = "video_stream"
        const val ORIENTATION_VERTICAL = "vertical"
        const val ORIENTATION_HORIZONTAL = "horizontal"

    }

    override fun type(typeFactory: Any?): Int {
        return 0
    }
}