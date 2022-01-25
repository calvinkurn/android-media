package com.tokopedia.digital.home.presentation.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

abstract class ParallaxScrollEffectListener(
    private val layoutManager: LinearLayoutManager
): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (layoutManager.findFirstVisibleItemPosition() == 0){
            val firstView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
            firstView?.let {
                val distanceFromLeft = it.left - getPixelSize()
                val translateX = distanceFromLeft * 0.2f
                translatedX(translateX)

                if (distanceFromLeft <= 0){
                    val itemSize = it.width.toFloat()
                    val alpha = (abs(distanceFromLeft) / itemSize * 0.80f)
                    setAlpha(1 - alpha)
                }else{
                    setAlpha(1f)
                }
            }
        }
    }

    abstract fun translatedX(translatedX: Float)
    abstract fun setAlpha(alpha: Float)
    abstract fun getPixelSize(): Int
}