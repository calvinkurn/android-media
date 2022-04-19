package com.tokopedia.play.view.custom.multiplelikes

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout

class PlayLikeBubblesView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var bubbles = emptyList<Bubble>()

    init {
        setWillNotDraw(false)
        clipChildren = false
        clipToPadding = false
        clipToOutline = false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(bubbles) {
            bubbles.forEach {
                it.draw(canvas)
            }
        }
    }

    fun setBubbles(bubbles: List<Bubble>) {
        this.bubbles = bubbles
    }
}