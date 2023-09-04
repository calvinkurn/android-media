package com.tokopedia.reviewcommon.feature.reviewer.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.UserReviewStats
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoThreeDotsListener
import com.tokopedia.reviewcommon.util.getReviewStar
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

open class ProductReviewBasicInfoWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var ratingStars: ImageUnify? = null
    private var timeStamp: Typography? = null
    private var reviewerName: Typography? = null
    private var dividerReviewerLabel: Typography? = null
    private var tvReviewerLabel: Typography? = null
    private var variant: Typography? = null
    private var reviewerStats: Typography? = null
    private var profilePicture: ImageUnify? = null
    private var icThreeDots: IconUnify? = null

    private var isProductReview = false
    private var isAnonymous = false
    private var userId = ""
    private var feedbackId = ""
    private var listener: ReviewBasicInfoListener? = null

    private fun init() {
        inflateView()
        bindViews()
    }

    private fun bindViews() {
        ratingStars = findViewById(R.id.iv_review_item_rating)
        timeStamp = findViewById(R.id.tv_review_item_timestamp)
        reviewerName = findViewById(R.id.tv_review_item_reviewer_name)
        dividerReviewerLabel = findViewById(R.id.divider_review_item_reviewer_name_label)
        tvReviewerLabel = findViewById(R.id.tv_review_item_reviewer_label)
        variant = findViewById(R.id.tv_review_item_variant)
        reviewerStats = findViewById(R.id.tv_review_item_reviewer_statistic)
        profilePicture = findViewById(R.id.iv_review_item_reviewer_profile_picture)
        icThreeDots = findViewById(R.id.ic_review_item_three_dots)
    }

    protected open fun inflateView() {
        View.inflate(context, R.layout.widget_product_review_basic_info, this)
    }

    fun setRating(rating: Int) {
        ratingStars?.apply {
            loadImage(getReviewStar(rating))
            if (shouldShowCredibility()) {
                setOnClickListener {
                    goToCredibility()
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
                }
            } else {
                setOnClickListener {}
            }
        }
    }

    fun setReviewerLabel(reviewerLabel: String) {
        tvReviewerLabel?.apply {
            text = reviewerLabel
            setOnClickListener {
                goToCredibility()
            }
            showWithCondition(reviewerLabel.isNotBlank())
        }
        dividerReviewerLabel?.showWithCondition(reviewerLabel.isNotBlank())
    }

    fun invertColors() {
        val color = ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        )
        timeStamp?.setTextColor(color)
        reviewerName?.setTextColor(color)
        variant?.setTextColor(color)
        reviewerStats?.setTextColor(color)
        reviewerStats?.alpha = 0.88f
        dividerReviewerLabel?.setTextColor(color)
        tvReviewerLabel?.setTextColor(
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_TN400
            )
        )
    }

    fun setVariantName(variantName: String) {
        variant?.apply {
            text = context.getString(R.string.review_gallery_variant, variantName)
            showWithCondition(variantName.isNotBlank())
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
                    textToShow += " • "
                }
                textToShow += it.formatted
            }
        }
        if (textToShow.isNotBlank()) {
            reviewerStats?.apply {
                text = textToShow
                setOnClickListener {
                    goToCredibility()
                }
            }
        }
        reviewerStats?.showWithCondition(textToShow.isNotBlank())
    }

    fun setStatsString(
        userStatsString: String
    ) {
        if (!shouldShowCredibility()) {
            reviewerStats?.hide()
            return
        }
        if (userStatsString.isNotBlank()) {
            reviewerStats?.apply {
                text = userStatsString
                setOnClickListener {
                    goToCredibility()
                }
            }
        }
        reviewerStats?.showWithCondition(userStatsString.isNotBlank())
    }

    fun setReviewerImage(imageUrl: String) {
        profilePicture?.apply {
            if (isProductReview && imageUrl.isNotEmpty()) {
                urlSrc = imageUrl
                show()
                if (!isAnonymous) {
                    setOnClickListener {
                        goToCredibility()
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

    fun setListeners(reviewBasicInfoListener: ReviewBasicInfoListener, threeDotsListener: ReviewBasicInfoThreeDotsListener?) {
        this.listener = reviewBasicInfoListener
        if (shouldShowCredibility()) {
            setOnClickListener { goToCredibility() }
        } else {
            setOnClickListener {  }
        }
        icThreeDots?.setOnClickListener { threeDotsListener?.onThreeDotsClicked() }
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
        listener?.onUserNameClicked(feedbackId, userId, reviewerStats?.text.toString(), tvReviewerLabel?.text.toString())
    }

    fun shouldShowCredibility(): Boolean {
        return isProductReview && !isAnonymous
    }

    fun showThreeDots() {
        icThreeDots?.show()
    }

    fun hideThreeDots() {
        icThreeDots?.gone()
    }

    fun hideRating() {
        ratingStars?.gone()
    }

    fun hideCreateTime() {
        timeStamp?.gone()
    }

    fun hideVariant() {
        variant?.gone()
    }
}
