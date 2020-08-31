package com.tokopedia.play.broadcaster.view.widget

import android.content.Context
import android.util.AttributeSet
import com.yalantis.ucrop.view.GestureCropImageView

/**
 * @author by furqan on 06/06/2020
 */
class PlayCropImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : GestureCropImageView(context, attrs, defStyleAttr) {

    fun getCurrentImageCorners(): FloatArray = mCurrentImageCorners

}