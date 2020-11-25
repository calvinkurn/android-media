package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import com.otaliastudios.cameraview.CameraView


/**
 * Created by mzennis on 20/07/20.
 */
class PlayCameraView(context: Context, attrs: AttributeSet) : CameraView(context, attrs) {

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mListener?.onCameraInstantiated()
    }

    interface Listener {
        fun onCameraInstantiated()
    }
}