package com.tokopedia.reputation.common.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.tokopedia.reputation.common.R
import com.tokopedia.reputation.common.data.source.cloud.model.AnimModel
import com.tokopedia.reputation.common.databinding.AnimatedReputationPickerBinding
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * This animated stars using AnimatedVectorDrawable, already support API <21
 * @property setListener call this first and populate with your onClick function
 * @property renderInitialReviewWithData If you want to animating the view without any trigger
 */
class AnimatedReputationView @JvmOverloads constructor(
    context: Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var listOfStarsView: List<AnimModel> = listOf()
    var count = 1
    var countMinus = 5
    var lastReview = 0
    var clickAt = 0

    /**
     * Determine whether the stars is reviewable (clickable) or not
     */
    var reviewable = true

    private var handle = Handler(Looper.getMainLooper())
    private var listener: AnimatedReputationListener? = null
    private var shouldShowDesc = false

    private val binding by lazy {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.animated_reputation_picker, this, false)
        AnimatedReputationPickerBinding.bind(view)
    }

    init {
        val styleable =
            context.obtainStyledAttributes(attrs, R.styleable.AnimatedReviewPicker, 0, 0)
        try {
            shouldShowDesc = styleable.getBoolean(R.styleable.AnimatedReviewPicker_show_desc, false)
        } finally {
            styleable.recycle()
        }

        listOfStarsView = listOf(
            AnimModel(false, binding.anim1),
            AnimModel(false, binding.anim2),
            AnimModel(false, binding.anim3),
            AnimModel(false, binding.anim4),
            AnimModel(false, binding.anim5)
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
            }
        }

        binding.txtDescStatus.visibility = if (shouldShowDesc) {
            View.VISIBLE
        } else {
            View.GONE
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

    private fun shouldReserveAnim(reviewData: AnimModel): Boolean {
        return reviewData.isAnimated
    }

    private fun isNormalAnim(reviewData: AnimModel): Boolean {
        return count <= clickAt && !reviewData.isAnimated
    }

    private fun generateReviewText(index: Int) = with(binding.txtDescStatus) {
        text = when (index) {
            1 -> resources.getString(R.string.rating_1_star)
            2 -> resources.getString(R.string.rating_2_star)
            3 -> resources.getString(R.string.rating_3_star)
            4 -> resources.getString(R.string.rating_4_star)
            5 -> resources.getString(R.string.rating_5_star)
            else -> ""
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

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (reviewable) {
            super.onInterceptTouchEvent(ev)
        } else {
            !reviewable
        }
    }

    interface AnimatedReputationListener {
        fun onClick(position: Int)
    }
}
