package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * Created by jegul on 28/08/20
 */
class VideoLoadingComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val ivLoading = findViewById<LoaderUnify>(R.id.iv_loading)
    private val ivLoadingText = findViewById<TextView>(R.id.iv_loading_text)

    fun show(source: BufferSource) {
        if (source == BufferSource.Viewer) showLoadingCircle()
        else showLoadingText()

        show()
    }

    private fun showLoadingText() {
        ivLoadingText.visible()
        ivLoading.gone()
    }

    private fun showLoadingCircle() {
        ivLoading.visible()
        ivLoadingText.gone()
    }
}