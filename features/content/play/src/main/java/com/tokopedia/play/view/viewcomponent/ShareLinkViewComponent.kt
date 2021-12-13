package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created By : Jonathan Darwin on November 01, 2021
 */
class ShareLinkViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val listener: Listener
) : ViewComponent(container, idRes) {

    private val ivShareLink = findViewById<IconUnify>(R.id.ic_play_share_link)

    init {
        ivShareLink.setOnClickListener {
            listener.onShareIconClick(this)
        }
    }

    fun setIsShareable(isShow: Boolean) {
        if (isShow) ivShareLink.show() else ivShareLink.hide()
    }

    interface Listener {
        fun onShareIconClick(view: ShareLinkViewComponent)
    }
}