package com.tokopedia.reputation.common.view

import android.content.Context
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.tokopedia.reputation.common.R

class AnimatedStarsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var sendToFavorite: AnimatedVectorDrawableCompat? = null
    private var favoriteToSend: AnimatedVectorDrawableCompat? = null
    private var showingFavorite: Boolean = false

    init {
        init()
    }

    fun init() {
        showingFavorite = true
        favoriteToSend = AnimatedVectorDrawableCompat.create(context, R.drawable.animated_stars)
        sendToFavorite = AnimatedVectorDrawableCompat.create(context, R.drawable.animated_reverse)
        setImageDrawable(favoriteToSend)
    }

    fun morph() {
        val drawable = if (showingFavorite) favoriteToSend else sendToFavorite
        setImageDrawable(drawable)
        drawable?.start()
        showingFavorite = !showingFavorite
    }
}