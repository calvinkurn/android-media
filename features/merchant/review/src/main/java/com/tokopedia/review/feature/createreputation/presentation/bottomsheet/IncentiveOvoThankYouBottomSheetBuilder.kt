package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
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
        hasPendingIncentive: Boolean,
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
            hasPendingIncentive,
            bottomSheetUnify,
            incentiveOvoListener,
            trackerData,
        )
        return bottomSheetUnify
    }

    private fun setupThankYouView(
        view: View,
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasPendingIncentive: Boolean,
        bottomSheet: BottomSheetUnify,
        incentiveOvoListener: IncentiveOvoListener,
        trackerData: ThankYouBottomSheetTrackerData,
    ) {
        bottomSheet.run {
            overlayClickDismiss = false
            showCloseIcon = false
            setupShowListener(view, postSubmitBottomSheetData, hasPendingIncentive, trackerData)
            setupDismissListener(incentiveOvoListener)
        }
    }

    private fun BottomSheetUnify.setupShowListener(
        childView: View,
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasPendingIncentive: Boolean,
        trackerData: ThankYouBottomSheetTrackerData
    ) {
        setShowListener {
            with(childView) {
                setupBottomSheetTitle(postSubmitBottomSheetData)
                setupBottomSheetDescription(postSubmitBottomSheetData)
                setupBottomSheetImage(postSubmitBottomSheetData)
                setupPrimaryButton(
                    postSubmitBottomSheetData = postSubmitBottomSheetData,
                    bottomSheetUnify = this@setupShowListener,
                    hasPendingIncentive = hasPendingIncentive
                )
                setupSecondaryButton(
                    postSubmitBottomSheetData = postSubmitBottomSheetData,
                    bottomSheetUnify = this@setupShowListener,
                    hasPendingIncentive = hasPendingIncentive
                )
                sendViewThankYouBottomSheetTracker(
                    postSubmitBottomSheetData,
                    trackerData,
                    context
                )
            }
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
        hasPendingIncentive: Boolean,
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
                    hasPendingIncentive,
                    postSubmitBottomSheetData.getTitle(context),
                    buttonText
                )
            )
        }
    }

    private fun View.setupSecondaryButton(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        bottomSheetUnify: BottomSheetUnify,
        hasPendingIncentive: Boolean,
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
                        hasPendingIncentive,
                        postSubmitBottomSheetData.getTitle(context),
                        buttonText
                    )
                )
            }
            incentiveOvoLater?.show()
        } ?: incentiveOvoLater?.gone()
    }

    private fun sendViewThankYouBottomSheetTracker(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        trackerData: ThankYouBottomSheetTrackerData,
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
    }

    private fun ProductrevGetPostSubmitBottomSheetResponse.getTitle(context: Context): String {
        return title ?: context.getString(R.string.review_create_thank_you_title)
    }

    private fun createButtonClickListener(
        actionType: String,
        appLink: String,
        bottomSheetUnify: BottomSheetUnify,
        hasPendingIncentive: Boolean,
        title: String,
        buttonText: String,
    ): View.OnClickListener {
        return View.OnClickListener { view ->
            bottomSheetUnify.dismiss()
            when (actionType) {
                ProductrevGetPostSubmitBottomSheetResponse.Button.TYPE_STANDARD,
                ProductrevGetPostSubmitBottomSheetResponse.Button.TYPE_WEB_VIEW -> {
                    CreateReviewTracking.eventClickPostSubmitBottomSheetButton(
                        title,
                        buttonText,
                        hasPendingIncentive
                    )
                    RouteManager.route(view.context, appLink)
                }
            }
        }
    }
}