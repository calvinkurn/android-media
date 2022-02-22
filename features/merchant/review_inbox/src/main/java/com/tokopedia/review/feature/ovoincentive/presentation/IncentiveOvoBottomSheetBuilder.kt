package com.tokopedia.review.feature.ovoincentive.presentation

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.feature.ovoincentive.analytics.IncentiveOvoTracking
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.TncBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.IncentiveOvoAdapter
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.IncentiveOvoTncBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx

object IncentiveOvoBottomSheetBuilder {

    private const val ADD_RATING_URL = "https://ecs7.tokopedia.net/android/others/ic_add_rating_incentive_tnc.png"
    private const val ADD_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/ic_add_photo_incentive_tnc.png"
    private const val ADD_REVIEW_URL = "https://ecs7.tokopedia.net/android/others/ic_add_review_incentive_tnc.png"

    fun getTermsAndConditionsBottomSheet(context: Context, productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain, hasIncentive: Boolean, hasOngoingChallenge: Boolean, incentiveOvoListener: IncentiveOvoListener, category: String, trackerData: TncBottomSheetTrackerData? = null): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.incentive_ovo_tnc_bottom_sheet, null)
        bottomSheetUnify.apply {
            setChild(view)
            setupTermsAndConditionView(view, productRevIncentiveOvoDomain, hasIncentive, hasOngoingChallenge, bottomSheetUnify, incentiveOvoListener, category, trackerData)
            setOnDismissListener {
                trackerData?.let {
                    IncentiveOvoTracking.eventDismissTncBottomSheet(productRevIncentiveOvoDomain.productrevIncentiveOvo?.description ?: ".", it.reputationId, it.orderId, it.productId, it.userId)
                    return@setOnDismissListener
                }
                ReviewTracking.onClickDismissIncentiveOvoBottomSheetTracker(category)
            }
            setShowListener {
                bottomSheetWrapper.setPadding(0, 16.toPx(), 0, 0)
                bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) {
                    }

                    override fun onStateChanged(p0: View, p1: Int) {
                        if (p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }
        }
        return bottomSheetUnify
    }

    private fun setupTermsAndConditionView(view: View, productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain, hasIncentive: Boolean, hasOngoingChallenge: Boolean, bottomSheet: BottomSheetUnify, incentiveOvoListener: IncentiveOvoListener, category: String, trackerData: TncBottomSheetTrackerData?) {
        bottomSheet.apply {
            val binding = IncentiveOvoTncBottomSheetBinding.bind(view)
            binding.apply {
                tgIncentiveOvoTitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.title
                tgIncentiveOvoSubtitle.text = HtmlLinkHelper(root.context, productRevIncentiveOvoDomain.productrevIncentiveOvo?.subtitle
                        ?: "").spannedString
                incentiveOvoBtnContinueReview.apply {
                    setOnClickListener {
                        dismiss()
                        trackerData?.let {
                            IncentiveOvoTracking.eventClickContinueTncBottomSheet(productRevIncentiveOvoDomain.productrevIncentiveOvo?.title ?: "", it.reputationId, it.orderId, it.productId, it.userId)
                            return@setOnClickListener
                        }
                        ReviewTracking.onClickContinueIncentiveOvoBottomSheetTracker(category)
                    }
                    text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.ctaText
                }
                tvIncentiveOvoAddRating.text = if (hasIncentive) {
                    view.context.getString(R.string.review_create_rating_condition_incentive)
                } else if (hasOngoingChallenge) {
                    view.context.getString(R.string.review_create_rating_condition_challenge)
                } else {
                    view.context.getString(R.string.review_create_rating_condition_incentive)
                }
                incentiveOvoAddRating.loadImage(ADD_RATING_URL)
                incentiveOvoAddImage.loadImage(ADD_IMAGE_URL)
                incentiveOvoAddReview.loadImage(ADD_REVIEW_URL)
                tgIncentiveOvoDescription.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.description
                val adapterIncentiveOvo = IncentiveOvoAdapter(productRevIncentiveOvoDomain.productrevIncentiveOvo?.numberedList
                    ?: emptyList(), incentiveOvoListener)
                rvIncentiveOvoExplain.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = adapterIncentiveOvo
                }
            }
            showKnob = true
            showCloseIcon = false
            showHeader = false
            isHideable = true
            isDragable = true
        }
    }

}