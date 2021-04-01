package com.tokopedia.reputation.common.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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

    var listOfStarsView: List<AnimCreateReviewModel> = listOf()
    var count = 1
    var countMinus = 5
    var lastReview = 0
    var clickAt = 0
    private var handle = Handler()
    private var listener: AnimatedReputationListener? = null
    private var shouldShowDesc = false
    private var starHeight = -1
    private var starWidth = -1

    init {
        val styleable = context.obtainStyledAttributes(attrs, R.styleable.AnimatedRatingPickerCreateReviewView, 0, 0)
        try {
            shouldShowDesc = styleable.getBoolean(R.styleable.AnimatedRatingPickerCreateReviewView_show_description, false)
            starHeight = styleable.getDimensionPixelSize(R.styleable.AnimatedRatingPickerCreateReviewView_star_height, -1)
            starWidth = styleable.getDimensionPixelSize(R.styleable.AnimatedRatingPickerCreateReviewView_star_width, -1)
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
                clickAt = index + 1
                generateReviewText(clickAt)
                if (clickAt < lastReview) {
                    handle.post(reverseAnimation)
                } else {
                    handle.post(normalAnimation)
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
        if (starHeight != -1) {
            arrayOf(anim_1_create_review, anim_2_create_review, anim_3_create_review, anim_4_create_review, anim_5_create_review).forEach {
                it.layoutParams.height = starHeight
            }
        }
    }

    private fun setStarWidth() {
        if (starWidth != -1) {
            arrayOf(anim_1_create_review, anim_2_create_review, anim_3_create_review, anim_4_create_review, anim_5_create_review).forEach {
                it.layoutParams.width = starWidth
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

    private fun shouldReserveAnim(reviewData: AnimCreateReviewModel): Boolean {
        return reviewData.isAnimated
    }

    private fun isNormalAnim(reviewData: AnimCreateReviewModel): Boolean {
        return count <= clickAt && !reviewData.isAnimated
    }

    private fun generateReviewText(index: Int) {
        when (index) {
            1 -> txt_desc_status.text = resources.getString(R.string.rating_1_star_create_review)
            2 -> txt_desc_status.text = resources.getString(R.string.rating_2_star_create_review)
            3 -> txt_desc_status.text = resources.getString(R.string.rating_3_star_create_review)
            4 -> txt_desc_status.text = resources.getString(R.string.rating_4_star_create_review)
            5 -> txt_desc_status.text = resources.getString(R.string.rating_5_star_create_review)
        }
    }

    /**
     * @param reviewClickAt which star should be animating
     */
    fun renderInitialReviewWithData(reviewClickAt: Int) {
        this.clickAt = reviewClickAt
        handle.post(normalAnimation)
        generateReviewText(reviewClickAt)
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
        fun onClick(position: Int) {}
        fun onRatingSelected(rating: Int) {}
    }
}