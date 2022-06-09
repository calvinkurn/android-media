package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.review.databinding.IncentiveOvoBottomSheetSubmittedBinding
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponse
import com.tokopedia.review.feature.ovoincentive.data.ThankYouBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class CreateReviewPostSubmitBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "CreateReviewPostSubmitBottomSheet"
    }

    private var binding by viewBinding(IncentiveOvoBottomSheetSubmittedBinding::bind)

    init {
        overlayClickDismiss = false
        showCloseIcon = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = IncentiveOvoBottomSheetSubmittedBinding.inflate(inflater).also {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupThankYouView(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        incentiveOvoListener: IncentiveOvoListener,
        trackerData: ThankYouBottomSheetTrackerData,
    ) {
        setupShowListener(
            postSubmitBottomSheetData,
            hasIncentive,
            hasOngoingChallenge,
            trackerData
        )
        setupDismissListener(incentiveOvoListener)
    }

    private fun BottomSheetUnify.setupShowListener(
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
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        trackerData: ThankYouBottomSheetTrackerData
    ) {
        binding?.run {
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
                root.context
            )
        }
    }

    private fun BottomSheetUnify.setupDismissListener(incentiveOvoListener: IncentiveOvoListener) {
        setOnDismissListener {
            incentiveOvoListener.onClickCloseThankYouBottomSheet()
        }
    }

    private fun IncentiveOvoBottomSheetSubmittedBinding.setupBottomSheetTitle(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse
    ) {
        incentiveOvoSubmittedTitle.text = postSubmitBottomSheetData.getTitle(root.context)
    }

    private fun IncentiveOvoBottomSheetSubmittedBinding.setupBottomSheetDescription(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse
    ) {
        val thankYouText = postSubmitBottomSheetData.description.orEmpty()
        incentiveOvoSubmittedSubtitle.text = thankYouText
    }

    private fun IncentiveOvoBottomSheetSubmittedBinding.setupBottomSheetImage(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse
    ) {
        val thankYouImageUrl = postSubmitBottomSheetData.imageUrl.orEmpty()
        incentiveOvoSubmittedImage.loadImage(thankYouImageUrl)
    }

    private fun IncentiveOvoBottomSheetSubmittedBinding.setupPrimaryButton(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        bottomSheetUnify: BottomSheetUnify,
        trackerData: ThankYouBottomSheetTrackerData,
    ) {
        val button = postSubmitBottomSheetData.buttonList?.firstOrNull()
        val buttonText = button?.text ?: root.context.getString(
            R.string.review_create_thank_you_default_button
        )
        val actionType = button?.type
            ?: ProductrevGetPostSubmitBottomSheetResponse.Button.TYPE_CLOSE
        incentiveOvoSendAnother.apply {
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

    private fun IncentiveOvoBottomSheetSubmittedBinding.setupSecondaryButton(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        bottomSheetUnify: BottomSheetUnify,
        trackerData: ThankYouBottomSheetTrackerData,
    ) {
        val button = if (postSubmitBottomSheetData.buttonList?.size.orZero() > 1) {
            postSubmitBottomSheetData.buttonList?.lastOrNull()
        } else null
        button?.let { button ->
            val buttonText = button.text.orEmpty()
            val actionType = button.type
                ?: ProductrevGetPostSubmitBottomSheetResponse.Button.TYPE_CLOSE
            incentiveOvoLater.apply {
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
            incentiveOvoLater.show()
        } ?: incentiveOvoLater.gone()
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

    fun init(
        postSubmitBottomSheetData: ProductrevGetPostSubmitBottomSheetResponse,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        incentiveOvoListener: IncentiveOvoListener,
        trackerData: ThankYouBottomSheetTrackerData,
    ) {
        setupThankYouView(postSubmitBottomSheetData, hasIncentive, hasOngoingChallenge, incentiveOvoListener, trackerData)
    }
}