package com.tokopedia.reputation.common.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.reputation.common.R
import com.tokopedia.reputation.common.data.source.cloud.model.ReviewLottieModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.animated_review_layout.view.*


/**
 * This animated stars using lottie, for better performance use AnimatedReputationView
 * @property setListener call this first and populate with your onClick function
 * @property renderInitialReviewWithData If you want to animating the view without any trigger
 *
 */
class AnimatedReviewPicker @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var count = 1
    private var countMinus = 5
    private var lastReviewClickAt = 0
    private var reviewClickAt = 0
    private var listOfStarsView: MutableList<ReviewLottieModel> = mutableListOf()
    private var handle = Handler()
    private var listener: AnimatedReviewPickerListener? = null
    private var txtReviewStatus: TextView
    private var shouldShowDesc: Boolean = true

    init {
        val styleable = context.obtainStyledAttributes(attrs, R.styleable.AnimatedReviewPicker, 0, 0)
        try {
            shouldShowDesc = styleable.getBoolean(R.styleable.AnimatedReviewPicker_show_desc, false)
        } finally {
            styleable.recycle()
        }

        val view = LayoutInflater.from(context).inflate(R.layout.animated_review_layout, this)
        val lottieStars1: LottieAnimationView = view.findViewById(R.id.lottie_star_1)
        val lottieStars2: LottieAnimationView = view.findViewById(R.id.lottie_star_2)
        val lottieStars3: LottieAnimationView = view.findViewById(R.id.lottie_star_3)
        val lottieStars4: LottieAnimationView = view.findViewById(R.id.lottie_star_4)
        val lottieStars5: LottieAnimationView = view.findViewById(R.id.lottie_star_5)
        txtReviewStatus = view.findViewById(R.id.txt_review_status)
        listOfStarsView = mutableListOf(ReviewLottieModel(reviewView = lottieStars1), ReviewLottieModel(reviewView = lottieStars2), ReviewLottieModel(reviewView = lottieStars3), ReviewLottieModel(reviewView = lottieStars4)
                , ReviewLottieModel(reviewView = lottieStars5))
        if (shouldShowDesc) {
            txt_review_status.visibility = View.VISIBLE
        } else {
            txt_review_status.visibility = View.GONE
        }
        initReviewPicker()
    }

    private fun initReviewPicker() {
        listOfStarsView.forEachIndexed { index, it ->
            it.reviewView.setOnClickListener {
                //Start from 1
                reviewClickAt = index+1
                if (reviewClickAt < lastReviewClickAt) {
                    handle.post(reverseAnimation)
                } else {
                    handle.post(normalAnimation)
                }
                generateReviewText(reviewClickAt)
                if (lastReviewClickAt != reviewClickAt) {
                    listener?.onStarsClick(reviewClickAt)
                }
            }
        }
    }

    private val normalAnimation = object : Runnable {
        override fun run() {
            if (count <= 5) {
                val reviewData = listOfStarsView[count-1]
                if (isNormalAnim(reviewData)) { // Animating in normal way
                    if (isReservedAnim(reviewData)) { // Check if last view is animated then reverse it to make it to normal animation again *If you dont reverse back, the status is still reversed
                        reviewData.reviewView.reverseAnimationSpeed()
                        reviewData.reviewView.playAnimation()
                    } else {
                        reviewData.reviewView.playAnimation()
                    }
                    reviewData.isAnimated = true
                }
                count++
                handle.postDelayed(this, 50) // Delay each animation to reach sequential animation
            } else {
                lastReviewClickAt = reviewClickAt
                count = 1
                handle.removeCallbacks(this)
            }
        }
    }

    private val reverseAnimation = object : Runnable {
        override fun run() {
            if (countMinus >= 1) {
                val reviewData = listOfStarsView[countMinus-1]
                if (shouldReserveAnim(reviewData)) { // When review clicked is under last review click then reverse animation
                    reviewData.isAnimated = false
                    reviewData.reviewView.reverseAnimationSpeed()
                    reviewData.reviewView.playAnimation()
                }
                countMinus--
                handle.postDelayed(this, 50) // Delay each animation to reach sequential animation
            } else {
                lastReviewClickAt = reviewClickAt
                countMinus = 5
                handle.removeCallbacks(this)
            }
        }
    }

    private fun shouldReserveAnim(reviewData: ReviewLottieModel): Boolean {
        return countMinus > reviewClickAt && reviewClickAt <= lastReviewClickAt && reviewData.isAnimated
    }

    private fun isNormalAnim(reviewData: ReviewLottieModel): Boolean {
        return count <= reviewClickAt && !reviewData.isAnimated
    }

    private fun isReservedAnim(reviewData: ReviewLottieModel): Boolean {
        return reviewData.reviewView.speed < 0.0
    }

    fun setListener(listener: AnimatedReviewPickerListener) {
        this.listener = listener
    }

    /**
     * @param reviewClickAt which star should be animating
     */
    fun renderInitialReviewWithData(reviewClickAt: Int) {
        this.reviewClickAt = reviewClickAt
        handle.post(normalAnimation)
        generateReviewText(reviewClickAt + 1)
        listener?.onStarsClick(reviewClickAt)
    }

    fun getReviewClickAt(): Int = reviewClickAt

    private fun generateReviewText(index: Int) {
        when (index) {
            1 -> txtReviewStatus.text = resources.getString(R.string.rating_1_star)
            2 -> txtReviewStatus.text = resources.getString(R.string.rating_2_star)
            3 -> txtReviewStatus.text = resources.getString(R.string.rating_3_star)
            4 -> txtReviewStatus.text = resources.getString(R.string.rating_4_star)
            5 -> txtReviewStatus.text = resources.getString(R.string.rating_5_star)
        }
    }

    interface AnimatedReviewPickerListener {
        fun onStarsClick(position: Int)
    }
}