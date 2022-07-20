package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.mapToUnifyButtonSize
import com.tokopedia.review.common.util.mapToUnifyButtonType
import com.tokopedia.review.common.util.mapToUnifyButtonVariant
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponse
import com.tokopedia.review.feature.ovoincentive.data.ThankYouBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

object IncentiveOvoThankYouBottomSheetBuilder {

    fun getThankYouBottomSheet(
        context: Context,
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        incentiveOvoListener: IncentiveOvoListener,
        trackerData: ThankYouBottomSheetTrackerData,
    ): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val child = View.inflate(
            context,
            com.tokopedia.review.R.layout.incentive_ovo_bottom_sheet_submitted,
            null
        )
        bottomSheetUnify.setChild(child)
        setupThankYouView(
            child,
            postSubmitBottomSheetData,
            hasIncentive,
            hasOngoingChallenge,
            bottomSheetUnify,
            incentiveOvoListener,
            trackerData,
        )
        return bottomSheetUnify
    }

    private fun setupThankYouView(
        view: View,
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        bottomSheet: BottomSheetUnify,
        incentiveOvoListener: IncentiveOvoListener,
        trackerData: ThankYouBottomSheetTrackerData,
    ) {
        bottomSheet.run {
            overlayClickDismiss = false
            showCloseIcon = false
            setupShowListener(
                view,
                postSubmitBottomSheetData,
                hasIncentive,
                hasOngoingChallenge,
                trackerData
            )
            setupDismissListener(incentiveOvoListener)
        }
    }

    private fun BottomSheetUnify.setupShowListener(
        childView: View,
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        trackerData: ThankYouBottomSheetTrackerData
    ) {
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        onBottomSheetExpanded(
                            childView,
                            postSubmitBottomSheetData,
                            hasIncentive,
                            hasOngoingChallenge,
                            trackerData
                        )
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // noop
            }
        }
        setShowListener {
            bottomSheet.addBottomSheetCallback(bottomSheetCallback)
        }
    }

    private fun BottomSheetUnify.onBottomSheetExpanded(
        childView: View,
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        trackerData: ThankYouBottomSheetTrackerData
    ) {
        with(childView) {
            setupBottomSheetTitle(postSubmitBottomSheetData)
            setupBottomSheetDescription(postSubmitBottomSheetData)
            setupBottomSheetImage(postSubmitBottomSheetData)
            setupPrimaryButton(
                postSubmitBottomSheetData = postSubmitBottomSheetData,
                bottomSheetUnify = this@onBottomSheetExpanded,
                trackerData = trackerData
            )
            setupSecondaryButton(
                postSubmitBottomSheetData = postSubmitBottomSheetData,
                bottomSheetUnify = this@onBottomSheetExpanded,
                trackerData = trackerData
            )
            sendViewThankYouBottomSheetTracker(
                postSubmitBottomSheetData,
                trackerData,
                hasIncentive,
                hasOngoingChallenge,
                context
            )
        }
    }

    private fun BottomSheetUnify.setupDismissListener(incentiveOvoListener: IncentiveOvoListener) {
        setOnDismissListener {
            incentiveOvoListener.onClickCloseThankYouBottomSheet()
        }
    }

    private fun View.setupBottomSheetTitle(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse
    ) {
        val incentiveOvoSubmittedTitle: com.tokopedia.unifyprinciples.Typography? = findViewById(
            R.id.incentiveOvoSubmittedTitle
        )
        incentiveOvoSubmittedTitle?.text = postSubmitBottomSheetData.getTitle(context)
    }

    private fun View.setupBottomSheetDescription(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse
    ) {
        val incentiveOvoSubmittedSubtitle: com.tokopedia.unifyprinciples.Typography? = findViewById(
            R.id.incentiveOvoSubmittedSubtitle
        )
        val thankYouText = postSubmitBottomSheetData.description.orEmpty()
        incentiveOvoSubmittedSubtitle?.text = thankYouText
    }

    private fun View.setupBottomSheetImage(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse
    ) {
        val incentiveOvoSubmittedImage = findViewById<AppCompatImageView>(
            R.id.incentiveOvoSubmittedImage
        )
        val thankYouImageUrl = postSubmitBottomSheetData.imageUrl.orEmpty()
        incentiveOvoSubmittedImage?.loadImage(thankYouImageUrl)
    }

    private fun View.setupPrimaryButton(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        bottomSheetUnify: BottomSheetUnify,
        trackerData: ThankYouBottomSheetTrackerData,
    ) {
        val button = postSubmitBottomSheetData.buttonList?.firstOrNull()
        val incentiveOvoSendAnother: UnifyButton? = findViewById(R.id.incentiveOvoSendAnother)
        val buttonText = button?.text ?: context.getString(
            R.string.review_create_thank_you_default_button
        )
        val actionType = button?.type
            ?: ProductrevGetPostSubmitBottomSheetResponse.Button.TYPE_CLOSE
        incentiveOvoSendAnother?.apply {
            text = buttonText
            buttonType = button?.unifyType.mapToUnifyButtonType()
            buttonVariant = button?.unifyVariant.mapToUnifyButtonVariant()
            buttonSize = button?.unifySize.mapToUnifyButtonSize()
            setOnClickListener(
                createButtonClickListener(
                    actionType,
                    button?.appLink.orEmpty(),
                    bottomSheetUnify,
                    postSubmitBottomSheetData.getTitle(context),
                    buttonText,
                    trackerData
                )
            )
        }
    }

    private fun View.setupSecondaryButton(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        bottomSheetUnify: BottomSheetUnify,
        trackerData: ThankYouBottomSheetTrackerData,
    ) {
        val incentiveOvoLater: UnifyButton? = findViewById(R.id.incentiveOvoLater)
        val button = if (postSubmitBottomSheetData.buttonList?.size.orZero() > 1) {
            postSubmitBottomSheetData.buttonList?.lastOrNull()
        } else null
        button?.let { button ->
            val buttonText = button.text.orEmpty()
            val actionType = button.type
                ?: ProductrevGetPostSubmitBottomSheetResponse.Button.TYPE_CLOSE
            incentiveOvoLater?.apply {
                text = buttonText
                buttonType = button.unifyType.mapToUnifyButtonType()
                buttonVariant = button.unifyVariant.mapToUnifyButtonVariant()
                buttonSize = button.unifySize.mapToUnifyButtonSize()
                setOnClickListener(
                    createButtonClickListener(
                        actionType,
                        button.appLink.orEmpty(),
                        bottomSheetUnify,
                        postSubmitBottomSheetData.getTitle(context),
                        buttonText,
                        trackerData
                    )
                )
            }
            incentiveOvoLater?.show()
        } ?: incentiveOvoLater?.gone()
    }

    private fun sendViewThankYouBottomSheetTracker(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        trackerData: ThankYouBottomSheetTrackerData,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        context: Context
    ) {
        CreateReviewTracking.eventViewThankYouBottomSheet(
            postSubmitBottomSheetData.getTitle(context),
            trackerData.reputationId,
            trackerData.orderId,
            trackerData.feedbackId,
            trackerData.productId,
            trackerData.userId
        )
        if (!hasIncentive && hasOngoingChallenge) {
            CreateReviewTracking.eventViewPostSubmitReviewBottomSheetForOngoingChallenge(
                postSubmitBottomSheetData.getTitle(context),
                trackerData.reputationId,
                trackerData.orderId,
                trackerData.feedbackId,
                trackerData.productId,
                trackerData.userId
            )
        }
    }

    private fun ProductrevGetPostSubmitBottomSheetResponse.getTitle(context: Context): String {
        return title ?: context.getString(R.string.review_create_thank_you_title)
    }

    private fun createButtonClickListener(
        actionType: String,
        appLink: String,
        bottomSheetUnify: BottomSheetUnify,
        bottomSheetTitle: String,
        ctaText: String,
        trackerData: ThankYouBottomSheetTrackerData
    ): View.OnClickListener {
        return View.OnClickListener { view ->
            CreateReviewTracking.eventClickPostSubmitBottomSheetButton(
                bottomSheetTitle = bottomSheetTitle,
                ctaText = ctaText,
                reputationId = trackerData.reputationId,
                orderId = trackerData.orderId,
                productId = trackerData.productId,
                feedbackId = trackerData.feedbackId,
                userId = trackerData.userId
            )
            bottomSheetUnify.dismiss()
            when (actionType) {
                ProductrevGetPostSubmitBottomSheetResponse.Button.TYPE_STANDARD,
                ProductrevGetPostSubmitBottomSheetResponse.Button.TYPE_WEB_VIEW -> {
                    RouteManager.route(view.context, appLink)
                }
            }
        }
    }
}