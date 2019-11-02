package com.tokopedia.reputation.common.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.reputation.common.R
import com.tokopedia.reputation.common.data.source.cloud.model.AnimModel
import kotlinx.android.synthetic.main.animated_reputation_picker.view.*

class AnimatedReputationView @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {


    var listOfStarsView: List<AnimModel> = listOf()
    var count = 1
    var countMinus = 5
    var lastReview = 0
    var clickAt = 0
    private var handle = Handler()
    private var listener: AnimatedReputationListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.animated_reputation_picker, this)
        listOfStarsView = listOf(
                AnimModel(false, anim_1),
                AnimModel(false, anim_2),
                AnimModel(false, anim_3),
                AnimModel(false, anim_4),
                AnimModel(false, anim_5)
        )
        listOfStarsView.forEachIndexed { index, animatedStarsView ->
            animatedStarsView.reviewView.setOnClickListener {

                clickAt = index+1
                if (clickAt < lastReview) {
                    handle.post(reverseAnimation)
                    listener?.onClick(clickAt)
                } else {
                    handle.post(normalAnimation)
                    listener?.onClick(clickAt)
                }
            }
        }
    }

    private val normalAnimation = object : Runnable {
        override fun run() {
            if (count <= clickAt) {
                val reviewData = listOfStarsView[count-1]
                if (isNormalAnim(reviewData)) { // Animating in normal way
                    reviewData.isAnimated = true
                    reviewData.reviewView.morph()
                }
                count++
                handle.postDelayed(this, 50) // Delay each animation to reach sequential animation
            } else {
                lastReview = clickAt
                count = 1
                handle.removeCallbacks(this)
            }
        }
    }

    private val reverseAnimation = object : Runnable {
        override fun run() {
            if (countMinus > clickAt) {
                val reviewData = listOfStarsView[countMinus-1]
                if (shouldReserveAnim(reviewData)) { // When review clicked is under last review click then reverse animation
                    reviewData.isAnimated = false
                    reviewData.reviewView.morph()
                }
                countMinus--
                handle.postDelayed(this, 50) // Delay each animation to reach sequential animation
            } else {
                lastReview = clickAt
                countMinus = 5
                handle.removeCallbacks(this)
            }
        }
    }

    private fun shouldReserveAnim(reviewData: AnimModel): Boolean {
        return reviewData.isAnimated
    }

    private fun isNormalAnim(reviewData: AnimModel): Boolean {
        return count <= clickAt && !reviewData.isAnimated
    }

    fun setListener(listener: AnimatedReputationListener) {
        this.listener = listener
    }

    fun init(){
        listOfStarsView.forEach {
            it.reviewView.init()
        }
    }

    interface AnimatedReputationListener{
        fun onClick(position:Int)
    }
}