package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.reading.data.UserReviewStats
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewBasicInfoWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var ratingStars: ImageUnify? = null
    private var timeStamp: Typography? = null
    private var reviewerName: Typography? = null
    private var variant: Typography? = null
    private var reviewerStats: Typography? = null
    private var profilePicture: ImageUnify? = null

    private var isProductReview = false
    private var isAnonymous = false
    private var userId = ""
    private var feedbackId = ""
    private var listener: ReviewBasicInfoListener? = null

    private fun init() {
        View.inflate(context, R.layout.widget_review_basic_info, this)
        bindViews()
    }

    private fun bindViews() {
        ratingStars = findViewById(R.id.review_item_rating)
        timeStamp = findViewById(R.id.review_item_timestamp)
        reviewerName = findViewById(R.id.review_item_reviewer_name)
        variant = findViewById(R.id.review_item_variant)
        reviewerStats = findViewById(R.id.review_item_reviewer_stats)
        profilePicture = findViewById(R.id.review_item_reviewer_image)
    }

    fun setRating(rating: Int) {
        ratingStars?.apply {
            loadImage(getReviewStar(rating))
            if (shouldShowCredibility()) {
                setOnClickListener {
                    goToCredibility()
                    trackOnUserInfoClicked()
                }
            } else {
                setOnClickListener { }
            }
        }
    }

    fun setCreateTime(createTime: String) {
        timeStamp?.apply {
            text = createTime
            if (shouldShowCredibility()) {
                setOnClickListener {
                    goToCredibility()
                    trackOnUserInfoClicked()
                }
            } else {
                setOnClickListener { }
            }
        }
    }

    fun setReviewerName(name: String) {
        reviewerName?.apply {
            text = name
            if (shouldShowCredibility()) {
                setOnClickListener {
                    goToCredibility()
                    trackOnUserInfoClicked()
                }
            } else {
                setOnClickListener {}
            }
        }
    }

    fun invertColors() {
        val color = ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White_96
        )
        timeStamp?.setTextColor(color)
        reviewerName?.setTextColor(color)
        variant?.setTextColor(color)
        reviewerStats?.setTextColor(color)
    }

    fun setVariantName(variantName: String) {
        if (variantName.isEmpty()) return
        this.variant?.apply {
            text = context.getString(R.string.review_gallery_variant, variantName)
            show()
        }
    }

    fun setStats(
        userStats: List<UserReviewStats>
    ) {
        if (!shouldShowCredibility()) {
            reviewerStats?.hide()
            return
        }
        var textToShow = ""
        userStats.forEach {
            if (it.formatted.isNotBlank()) {
                if (textToShow.isNotBlank()) {
                    textToShow += " "
                }
                textToShow += context.getString(
                    R.string.review_reading_reviewer_stats,
                    it.formatted
                )
            }
        }
        if (textToShow.isNotBlank()) {
            reviewerStats?.apply {
                text = textToShow
                show()
                setOnClickListener {
                    goToCredibility()
                    trackOnUserInfoClicked()
                }
            }
        }
    }

    fun setReviewerImage(imageUrl: String) {
        profilePicture?.apply {
            if (isProductReview) {
                loadImage(imageUrl)
                show()
                if (!isAnonymous) {
                    setOnClickListener {
                        goToCredibility()
                        trackOnUserInfoClicked()
                    }
                } else {
                    setOnClickListener {}
                }
            } else {
                hideReviewerImage()
            }
        }
    }

    private fun hideReviewerImage() {
        profilePicture?.hide()
    }

    fun setListener(reviewBasicInfoListener: ReviewBasicInfoListener) {
        this.listener = reviewBasicInfoListener
        if (shouldShowCredibility()) {
            setOnClickListener {
                listener?.onUserNameClicked(userId)
            }
        } else {
            setOnClickListener {  }
        }

    }

    fun setCredibilityData(
        isProductReview: Boolean,
        isAnonymous: Boolean,
        userId: String,
        feedbackId: String
    ) {
        this.isProductReview = isProductReview
        this.isAnonymous = isAnonymous
        this.userId = userId
        this.feedbackId = feedbackId
    }

    private fun goToCredibility() {
        listener?.onUserNameClicked(userId)
    }

    private fun trackOnUserInfoClicked() {
        listener?.trackOnUserInfoClicked(feedbackId, userId, reviewerStats?.text.toString())
    }

    fun shouldShowCredibility(): Boolean {
        return isProductReview && !isAnonymous
    }
}