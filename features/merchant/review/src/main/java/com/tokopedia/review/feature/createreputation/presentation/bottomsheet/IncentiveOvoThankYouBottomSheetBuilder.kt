package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.ThankYouBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

object IncentiveOvoThankYouBottomSheetBuilder {

    fun getThankYouBottomSheet(
        context: Context,
        productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain?,
        incentiveOvoListener: IncentiveOvoListener,
        trackerData: ThankYouBottomSheetTrackerData,
        thankYouText: String,
        thankYouImageUrl: String
    ): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val child = View.inflate(context,  com.tokopedia.review.R.layout.incentive_ovo_bottom_sheet_submitted, null)
        bottomSheetUnify.setChild(child)
        setupThankYouView(
            child,
            productRevIncentiveOvoDomain,
            bottomSheetUnify,
            incentiveOvoListener,
            trackerData,
            thankYouText,
            thankYouImageUrl
        )
        return bottomSheetUnify
    }

    private fun setupThankYouView(
        view: View,
        productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain?,
        bottomSheet: BottomSheetUnify,
        incentiveOvoListener: IncentiveOvoListener,
        trackerData: ThankYouBottomSheetTrackerData,
        thankYouText: String,
        thankYouImageUrl: String
    ) {
        bottomSheet.run {
            view.apply {
                overlayClickDismiss = false
                showCloseIcon = false
                bottomSheet.setShowListener {
                    val incentiveOvoSubmittedImage =
                        view.findViewById<AppCompatImageView>(R.id.incentiveOvoSubmittedImage)
                    val incentiveOvoSubmittedTitle: com.tokopedia.unifyprinciples.Typography? =
                        view.findViewById(
                            R.id.incentiveOvoSubmittedTitle
                        )
                    val incentiveOvoSubmittedSubtitle: com.tokopedia.unifyprinciples.Typography? =
                        view.findViewById(
                            R.id.incentiveOvoSubmittedSubtitle
                        )
                    val incentiveOvoSendAnother: UnifyButton? =
                        view.findViewById(R.id.incentiveOvoSendAnother)
                    val incentiveOvoLater: UnifyButton? = view.findViewById(R.id.incentiveOvoLater)
                    val defaultTitle =
                        context?.getString(R.string.review_create_thank_you_title) ?: ""
                    CreateReviewTracking.eventViewThankYouBottomSheet(
                        defaultTitle,
                        trackerData.reputationId,
                        trackerData.orderId,
                        trackerData.feedbackId,
                        trackerData.productId,
                        trackerData.userId
                    )
                    incentiveOvoSubmittedImage?.loadImage(thankYouImageUrl)
                    incentiveOvoSubmittedTitle?.text = defaultTitle
                    incentiveOvoSubmittedSubtitle?.text = thankYouText
                    productRevIncentiveOvoDomain?.productrevIncentiveOvo?.let {
                        incentiveOvoSendAnother?.apply {
                            setOnClickListener {
                                dismiss()
                                incentiveOvoListener.onClickReviewAnother()
                                CreateReviewTracking.eventClickSendAnother(defaultTitle, true)
                            }
                        }
                        incentiveOvoLater?.apply {
                            setOnClickListener {
                                dismiss()
                                incentiveOvoListener.onClickCloseThankYouBottomSheet()
                                CreateReviewTracking.eventClickLater(defaultTitle, true)
                            }
                            show()
                        }
                        return@setShowListener
                    }
                    incentiveOvoSendAnother?.apply {
                        text = context.getString(R.string.review_create_thank_you_default_button)
                        setOnClickListener {
                            dismiss()
                            incentiveOvoListener.onClickCloseThankYouBottomSheet()
                            CreateReviewTracking.eventClickOk(
                                defaultTitle,
                                productRevIncentiveOvoDomain?.productrevIncentiveOvo != null
                            )
                        }
                    }
                }
                bottomSheet.setOnDismissListener {
                    incentiveOvoListener.onClickCloseThankYouBottomSheet()
                }
            }
        }
    }
}