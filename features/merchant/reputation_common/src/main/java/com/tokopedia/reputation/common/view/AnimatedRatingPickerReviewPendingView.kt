package com.tokopedia.reputation.common.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.reputation.common.R
import com.tokopedia.reputation.common.data.source.cloud.model.AnimReviewPendingModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.animated_rating_picker_review_pending.view.*

/**
 * This animated stars using AnimatedVectorDrawable, already support API <21
 * @property setListener call this first and populate with your onClick function
 * @property renderInitialReviewWithData If you want to animating the view without any trigger
 */
class AnimatedRatingPickerReviewPendingView @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var listOfStarsView: List<AnimReviewPendingModel> = listOf()
    var count = 1
    var countMinus = 5
    var lastReview = 0
    var clickAt = 0
    private var handle = Handler()
    private var listener: AnimatedReputationListener? = null

    init {

        LayoutInflater.from(context).inflate(R.layout.animated_rating_picker_review_pending, this)
        listOfStarsView = listOf(
                AnimReviewPendingModel(false, anim_1_review_pending),
                AnimReviewPendingModel(false, anim_2_review_pending),
                AnimReviewPendingModel(false, anim_3_review_pending),
                AnimReviewPendingModel(false, anim_4_review_pending),
                AnimReviewPendingModel(false, anim_5_review_pending)
        )
        listOfStarsView.forEachIndexed { index, animatedStarsView ->
            animatedStarsView.reviewView.setOnClickListener {
                clickAt = index + 1
                if (clickAt < lastReview) {
                    handle.post(reverseAnimation)
                } else {
                    handle.post(normalAnimation)
                }
                if (lastReview != clickAt) {
                    listener?.onClick(clickAt)
                }
            }
        }
        
    }

    private val normalAnimation = object : Runnable {
        override fun run() {
            if (count <= clickAt) {
                val reviewData = listOfStarsView[count - 1]
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
                val reviewData = listOfStarsView[countMinus - 1]
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

    private fun shouldReserveAnim(reviewData: AnimReviewPendingModel): Boolean {
        return reviewData.isAnimated
    }

    private fun isNormalAnim(reviewData: AnimReviewPendingModel): Boolean {
        return count <= clickAt && !reviewData.isAnimated
    }

    /**
     * @param reviewClickAt which star should be animating
     */
    fun renderInitialReviewWithData(reviewClickAt: Int) {
        this.clickAt = reviewClickAt
        handle.post(normalAnimation)
    }


    fun getReviewClickAt(): Int = clickAt

    fun setListener(listener: AnimatedReputationListener) {
        this.listener = listener
    }

    //Reset stars
    fun resetStars() {
        clickAt = 0
        lastReview = 0
        listOfStarsView.forEach {
            it.reviewView.resetStars()
            it.isAnimated = false
        }
    }

    interface AnimatedReputationListener {
        fun onClick(position: Int)
    }
}