package com.tokopedia.reputation.common.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.reputation.common.R
import com.tokopedia.reputation.common.data.source.cloud.model.ReviewLottieModel


/**
 * @property setListener call this first and populate with your onClick function
 * @property renderInitialReviewWithData If you want to animating the view without any trigger
 * @property
 */
class AnimatedReviewPicker @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var count = 0
    private var countMinus = 4
    private var lastReviewClickAt = 0
    private var reviewClickAt = 0
    private var listOfStarsView: MutableList<ReviewLottieModel> = mutableListOf()
    private var handle = Handler()
    private var listener: AnimatedReviewPickerListener? = null
    private var txtReviewStatus: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.animated_review_layout, this)
        val lottieStars1: LottieAnimationView = view.findViewById(R.id.lottie_star_1)
        val lottieStars2: LottieAnimationView = view.findViewById(R.id.lottie_star_2)
        val lottieStars3: LottieAnimationView = view.findViewById(R.id.lottie_star_3)
        val lottieStars4: LottieAnimationView = view.findViewById(R.id.lottie_star_4)
        val lottieStars5: LottieAnimationView = view.findViewById(R.id.lottie_star_5)
        txtReviewStatus = view.findViewById(R.id.txt_review_status)
        listOfStarsView = mutableListOf(ReviewLottieModel(reviewView = lottieStars1), ReviewLottieModel(reviewView = lottieStars2), ReviewLottieModel(reviewView = lottieStars3), ReviewLottieModel(reviewView = lottieStars4)
                , ReviewLottieModel(reviewView = lottieStars5))
        initReviewPicker()
    }

    private fun initReviewPicker() {
        listOfStarsView.forEachIndexed { index, it ->
            it.reviewView.setOnClickListener {
                reviewClickAt = index
                if (reviewClickAt < lastReviewClickAt) {
                    handle.post(reverseAnimation)
                } else {
                    handle.post(normalAnimation)
                }
                generateReviewText(index + 1)
                if (lastReviewClickAt != reviewClickAt) {
                    listener?.onStarsClick(index + 1)

                }
            }
        }
    }

    private val normalAnimation = object : Runnable {
        override fun run() {
            if (count <= 4) {
                val reviewData = listOfStarsView[count]
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
                count = 0
                handle.removeCallbacks(this)
            }
        }
    }

    private val reverseAnimation = object : Runnable {
        override fun run() {
            if (countMinus >= 0) {
                val reviewData = listOfStarsView[countMinus]
                if (shouldReserveAnim(reviewData)) { // When review clicked is under last review click then reverse animation
                    reviewData.isAnimated = false
                    reviewData.reviewView.reverseAnimationSpeed()
                    reviewData.reviewView.playAnimation()
                }
                countMinus--
                handle.postDelayed(this, 50) // Delay each animation to reach sequential animation
            } else {
                lastReviewClickAt = reviewClickAt
                countMinus = 4
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
            1 -> txtReviewStatus.text = "Sangat Buruk"
            2 -> txtReviewStatus.text = "Buruk"
            3 -> txtReviewStatus.text = "Cukup"
            4 -> txtReviewStatus.text = "Baik"
            5 -> txtReviewStatus.text = "Memuaskan"
        }
    }

    interface AnimatedReviewPickerListener {
        fun onStarsClick(position: Int)
    }
}