package com.tokopedia.play.view.custom

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

/**
 * Created by mzennis on 18/12/20.
 */
class PlayUnifyLoader : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        if (drawable == null) {
            Glide.with(context)
                    .asGif()
                    .load(com.tokopedia.resources.common.R.drawable.ic_loading_indeterminate)
                    .into(this)
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