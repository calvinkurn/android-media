package com.tokopedia.play.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.play.R


/**
 * Created by mzennis on 18/12/20.
 */
class PlayUnifyLoader : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val animatedVectorDrawable by lazy { AnimatedVectorDrawableCompat.create(context, R.drawable.anim_play_unify_loader) }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setImageDrawable(animatedVectorDrawable)
        } else {
            // todo: set loading to support bellow lollipop
        }
    }

    override fun onAttachedToWindow() {
        animatedVectorDrawable?.start()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        animatedVectorDrawable?.stop()
        super.onDetachedFromWindow()
    }
}