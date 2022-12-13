package com.tokopedia.reputation.common.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.reputation.common.R
import com.tokopedia.reputation.common.data.source.cloud.model.AnimCreateReviewModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.animated_rating_picker_create_review.view.*

/**
 * This animated stars using AnimatedVectorDrawable, already support API <21
 * @property setListener call this first and populate with your onClick function
 * @property renderInitialReviewWithData If you want to animating the view without any trigger
 */
class AnimatedRatingPickerCreateReviewView @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val INITIAL_RATING = 0
        private const val INITIAL_COUNT = 1
        private const val INITIAL_COUNT_MINUS = 5
        private const val RATING_ANIMATION_DELAY = 50L

        private const val RATING_ONE = 1
        private const val RATING_TWO = 2
        private const val RATING_THREE = 3
        private const val RATING_FOUR = 4
        private const val RATING_FIVE = 5
    }

    var listOfStarsView: List<AnimCreateReviewModel> = listOf()
    var count = INITIAL_COUNT
    var countMinus = INITIAL_COUNT_MINUS
    var lastReview = INITIAL_RATING
    var clickAt = INITIAL_RATING
    private var handle = Handler(Looper.getMainLooper())
    private var listener: AnimatedReputationListener? = null
    private var shouldShowDesc = false
    private var starHeight = ViewGroup.LayoutParams.MATCH_PARENT
    private var starWidth = ViewGroup.LayoutParams.MATCH_PARENT

    init {
        val styleable = context.obtainStyledAttributes(attrs, R.styleable.AnimatedRatingPickerCreateReviewView)
        try {
            shouldShowDesc = styleable.getBoolean(R.styleable.AnimatedRatingPickerCreateReviewView_show_description, false)
            starHeight = styleable.getDimensionPixelSize(R.styleable.AnimatedRatingPickerCreateReviewView_star_height, ViewGroup.LayoutParams.MATCH_PARENT)
            starWidth = styleable.getDimensionPixelSize(R.styleable.AnimatedRatingPickerCreateReviewView_star_width, ViewGroup.LayoutParams.MATCH_PARENT)
        } finally {
            styleable.recycle()
        }


        LayoutInflater.from(context).inflate(R.layout.animated_rating_picker_create_review, this)
        listOfStarsView = listOf(
                AnimCreateReviewModel(false, anim_1_create_review),
                AnimCreateReviewModel(false, anim_2_create_review),
                AnimCreateReviewModel(false, anim_3_create_review),
                AnimCreateReviewModel(false, anim_4_create_review),
                AnimCreateReviewModel(false, anim_5_create_review)
        )
        listOfStarsView.forEachIndexed { index, animatedStarsView ->
            animatedStarsView.reviewView.setOnClickListener {
                clickAt = index.inc()
                generateReviewText(clickAt)
                cancelPendingAnimations()
                if (clickAt < lastReview) {
                    handle.post(reverseAnimated)
                } else {
                    handle.post(normalAnimated)
                }
                if (lastReview != clickAt) {
                    listener?.onClick(clickAt)
                }
                listener?.onRatingSelected(clickAt)
            }
        }

        if (shouldShowDesc) {
            txt_desc_status.visibility = View.VISIBLE
        } else {
            txt_desc_status.visibility = View.GONE
        }

        setStarHeight()
        setStarWidth()
    }

    private fun setStarHeight() {
        if (starHeight != ViewGroup.LayoutParams.MATCH_PARENT) {
            arrayOf(anim_1_create_review, anim_2_create_review, anim_3_create_review, anim_4_create_review, anim_5_create_review).forEach {
                it.layoutParams.height = starHeight
            }
        }
    }

    private fun setStarWidth() {
        if (starWidth != ViewGroup.LayoutParams.MATCH_PARENT) {
            arrayOf(anim_1_create_review, anim_2_create_review, anim_3_create_review, anim_4_create_review, anim_5_create_review).forEach {
                it.layoutParams.width = starWidth
            }
        }
    }

    private val normalAnimated = object : Runnable {
        override fun run() {
            if (count <= clickAt) {
                val reviewData = listOfStarsView[count.dec()]
                if (isNormalAnim(reviewData)) { // Animating in normal way
                    reviewData.isAnimated = true
                    reviewData.reviewView.morph()
                }
                count++
                handle.postDelayed(this, RATING_ANIMATION_DELAY) // Delay each animation to reach sequential animation
            } else {
                lastReview = clickAt
                count = INITIAL_COUNT
                handle.removeCallbacks(this)
            }
        }
    }

    private val normalNonAnimated = object : Runnable {
        override fun run() {
            if (count <= clickAt) {
                val reviewData = listOfStarsView[count.dec()]
                if (isNormalAnim(reviewData)) {
                    reviewData.isAnimated = true
                    reviewData.reviewView.toggleStar()
                }
                count++
                handle.post(this)
            } else {
                lastReview = clickAt
                count = INITIAL_COUNT
                handle.removeCallbacks(this)
            }
        }
    }

    private val reverseAnimated = object : Runnable {
        override fun run() {
            if (countMinus > clickAt) {
                val reviewData = listOfStarsView[countMinus.dec()]
                if (shouldReserveAnim(reviewData)) { // When review clicked is under last review click then reverse animation
                    reviewData.isAnimated = false
                    reviewData.reviewView.morph()
                }
                countMinus--
                handle.postDelayed(this, RATING_ANIMATION_DELAY) // Delay each animation to reach sequential animation
            } else {
                lastReview = clickAt
                countMinus = INITIAL_COUNT_MINUS
                handle.removeCallbacks(this)
            }
        }
    }

    private val reverseNonAnimated = object : Runnable {
        override fun run() {
            if (countMinus > clickAt) {
                val reviewData = listOfStarsView[countMinus.dec()]
                if (shouldReserveAnim(reviewData)) {
                    reviewData.isAnimated = false
                    reviewData.reviewView.toggleStar()
                }
                countMinus--
                handle.post(this)
            } else {
                lastReview = clickAt
                countMinus = INITIAL_COUNT_MINUS
                handle.removeCallbacks(this)
            }
        }
    }

    private fun shouldReserveAnim(reviewData: AnimCreateReviewModel): Boolean {
        return reviewData.isAnimated
    }

    private fun isNormalAnim(reviewData: AnimCreateReviewModel): Boolean {
        return count <= clickAt && !reviewData.isAnimated
    }

    private fun generateReviewText(index: Int) {
        when (index) {
            RATING_ONE -> txt_desc_status.text = resources.getString(R.string.rating_1_star_create_review)
            RATING_TWO -> txt_desc_status.text = resources.getString(R.string.rating_2_star_create_review)
            RATING_THREE -> txt_desc_status.text = resources.getString(R.string.rating_3_star_create_review)
            RATING_FOUR -> txt_desc_status.text = resources.getString(R.string.rating_4_star_create_review)
            RATING_FIVE -> txt_desc_status.text = resources.getString(R.string.rating_5_star_create_review)
        }
    }

    /**
     * @param reviewClickAt which star should be animating
     */
    fun renderInitialReviewWithData(reviewClickAt: Int, animate: Boolean = true) {
        this.clickAt = reviewClickAt
        cancelPendingAnimations()
        handle.post(if (animate) normalAnimated else normalNonAnimated)
        generateReviewText(reviewClickAt)
    }

    fun setRating(rating: Int, animate: Boolean = true) {
        this.clickAt = rating
        cancelPendingAnimations()
        if (clickAt < lastReview) {
            handle.post(if (animate) reverseAnimated else reverseNonAnimated)
        } else {
            handle.post(if (animate) normalAnimated else normalNonAnimated)
        }
        generateReviewText(rating)
    }

    fun getReviewClickAt(): Int = clickAt

    fun setListener(listener: AnimatedReputationListener) {
        this.listener = listener
    }

    //Reset stars
    fun resetStars() {
        clickAt = INITIAL_RATING
        lastReview = INITIAL_RATING
        listOfStarsView.forEach {
            it.reviewView.resetStars()
            it.isAnimated = false
        }
    }

    private fun cancelPendingAnimations() {
        handle.removeCallbacks(normalAnimated)
        handle.removeCallbacks(normalNonAnimated)
        handle.removeCallbacks(reverseAnimated)
        handle.removeCallbacks(reverseNonAnimated)
    }

    interface AnimatedReputationListener {
        fun onClick(position: Int) {}
        fun onRatingSelected(rating: Int) {}
    }
}
