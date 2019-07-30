package com.tokopedia.groupchat.room.view.viewstate

import android.support.v4.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.BackgroundViewModel

/**
 * @author : Steven 01/07/19
 */
class PlayBackgroundHelper(
        model: ChannelInfoViewModel?,
        var activity: FragmentActivity
): PlayBaseHelper(model) {


    var backgroundViewModel: BackgroundViewModel? = null
    private var defaultBackground = arrayListOf(
            R.drawable.bg_play_1,
            R.drawable.bg_play_2,
            R.drawable.bg_play_3
    )

    private var defaultType = arrayListOf(
            "default", "default1", "default2", "default3")


    fun setBackground(it: BackgroundViewModel) {
        backgroundViewModel = it
        resetBackground()
    }

    fun resetBackground() {
        var background: Int
        lateinit var url: String
        backgroundViewModel?.let {
            val index = defaultType.indexOf(it.default)
            background = defaultBackground[Math.max(0, index - 1)]
            url = it.url

            if (url.isBlank()) {
                activity.window?.setBackgroundDrawable(MethodChecker.getDrawable(activity, background))
            } else {
                ImageHandler.loadBackgroundImage(activity.window, url)
            }
        }
    }

    fun setEmptyBackground() {
        activity.window?.setBackgroundDrawable(MethodChecker.getDrawable(activity, R.color.black))
    }

    fun setDefaultBackground() {
        val background = defaultBackground[0]
        activity.window?.setBackgroundDrawable(MethodChecker.getDrawable(activity, background))
    }
}