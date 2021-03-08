package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 28/08/20
 */
class VideoLoadingComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val ivLoading = findViewById<LoaderUnify>(R.id.iv_loading)
    private val ivLoadingText = findViewById<Typography>(R.id.iv_loading_text)

    fun showWaitingState() {
        showLoadingForWaiting()
    }

    fun show(source: BufferSource) {
        if (source != BufferSource.Broadcaster) showLoadingOnly()
        else showLoadingForSellerBuffer()

        show()
    }

    private fun showLoadingWithText() {
        ivLoadingText.visible()
        show()
    }

    private fun showLoadingOnly() {
        ivLoadingText.gone()
        show()
    }

    private fun showLoadingForWaiting() {
        ivLoadingText.setText(R.string.play_buffer_reason_waiting)

        showLoadingWithText()
    }

    private fun showLoadingForSellerBuffer() {
        ivLoadingText.setText(R.string.play_buffer_reason_seller_side)

        showLoadingWithText()
    }
}