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
                val translateX = distanceFromLeft * DISTANCE_X
                translatedX(translateX)

                if (distanceFromLeft <= 0){
                    val itemSize = it.width.toFloat()
                    val alpha = (abs(distanceFromLeft) / itemSize * ALPHA_ITEM_PER_DISTANCE)
                    setAlpha(ALPHA_SUBTRACTOR - alpha)
                }else{
                    setAlpha(ALPHA_1)
                }
            }
        }
    }

    abstract fun translatedX(translatedX: Float)
    abstract fun setAlpha(alpha: Float)
    abstract fun getPixelSize(): Int

    companion object{
        private const val ALPHA_1 = 1f
        private const val ALPHA_SUBTRACTOR = 1
        private const val ALPHA_ITEM_PER_DISTANCE = 0.80f
        private const val DISTANCE_X = 0.2f
    }
}