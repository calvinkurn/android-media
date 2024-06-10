package com.tokopedia.play.view.custom

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.media.loader.loadAsGif
import com.tokopedia.resources.common.R as resourcescommonR

/**
 * Created by mzennis on 18/12/20.
 */
class PlayUnifyLoader : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        if (drawable == null) {
            this.loadAsGif(resourcescommonR.drawable.ic_loading_indeterminate)
        }
    }

    fun pause() {
        gifDrawable { stop() }
    }

    fun start() {
        gifDrawable { start() }
    }

    private fun gifDrawable(handler: Animatable.() -> Unit) {
        val theDrawable = drawable
        if (theDrawable is Animatable) theDrawable.handler()
    }
}
