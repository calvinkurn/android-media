package com.tokopedia.review.feature.ovoincentive.presentation

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.inbox.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.IncentiveOvoAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.incentive_ovo_bottom_sheet_dialog.view.*

object IncentiveOvoBottomSheetBuilder {

    private const val THANK_YOU_BOTTOMSHEET_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/ovo_incentive_bottom_sheet_image.png"

    fun getTermsAndConditionsBottomSheet(context: Context, productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain, incentiveOvoListener: IncentiveOvoListener): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.incentive_ovo_bottom_sheet_dialog, null)
        bottomSheetUnify.apply {
            setChild(view)
            setupTermsAndConditionView(view, productRevIncentiveOvoDomain, bottomSheetUnify, incentiveOvoListener)
            setOnDismissListener {
                ReviewTracking.onClickDismissIncentiveOvoBottomSheetTracker("")
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

    fun getThankYouBottomSheet(context: Context, productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain?, incentiveOvoListener: IncentiveOvoListener, amount: Int): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val child = View.inflate(context, R.layout.incentive_ovo_bottom_sheet_submitted, null)
        bottomSheetUnify.setChild(child)
        bottomSheetUnify.setCloseClickListener {
            incentiveOvoListener.onClickCloseThankYouBottomSheet()
        }
        setupThankYouView(child, productRevIncentiveOvoDomain, bottomSheetUnify, incentiveOvoListener, amount)
        return bottomSheetUnify
    }

    private fun setupThankYouView(view: View, productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain?, bottomSheet: BottomSheetUnify, incentiveOvoListener: IncentiveOvoListener, amount: Int) {
        bottomSheet.run {
            val incentiveOvoSubmittedImage = view.findViewById<AppCompatImageView>(R.id.incentiveOvoSubmittedImage)
            val incentiveOvoSubmittedTitle: com.tokopedia.unifyprinciples.Typography? = view.findViewById(R.id.incentiveOvoSubmittedTitle)
            val incentiveOvoSubmittedSubtitle: com.tokopedia.unifyprinciples.Typography? = view.findViewById(R.id.incentiveOvoSubmittedSubtitle)
            val incentiveOvoSendAnother: UnifyButton? = view.findViewById(R.id.incentiveOvoSendAnother)
            val incentiveOvoLater: UnifyButton? = view.findViewById(R.id.incentiveOvoLater)
            view.apply {
                val defaultTitle = context?.getString(R.string.review_create_thank_you_title) ?: ""
                bottomSheet.setShowListener {
                    CreateReviewTracking.eventViewThankYouBottomSheet(defaultTitle, productRevIncentiveOvoDomain != null)
                }
                incentiveOvoSubmittedImage?.loadImage(THANK_YOU_BOTTOMSHEET_IMAGE_URL)
                incentiveOvoSubmittedTitle?.text = defaultTitle
                incentiveOvoSubmittedSubtitle?.text = context.getString(R.string.review_create_thank_you_subtitle, amount)
                productRevIncentiveOvoDomain?.let {
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
                    return
                }
                incentiveOvoSendAnother?.apply {
                    text = context.getString(R.string.review_create_thank_you_default_button)
                    setOnClickListener {
                        dismiss()
                        incentiveOvoListener.onClickCloseThankYouBottomSheet()
                        CreateReviewTracking.eventClickOk(defaultTitle, productRevIncentiveOvoDomain != null)
                    }
                }
                overlayClickDismiss = false
            }
        }
    }

    private fun setupTermsAndConditionView(view: View, productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain, bottomSheet: BottomSheetUnify, incentiveOvoListener: IncentiveOvoListener) {
        bottomSheet.apply {
            view.apply {
                tgIncentiveOvoTitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.title
                tgIncentiveOvoSubtitle.text = HtmlLinkHelper(context, productRevIncentiveOvoDomain.productrevIncentiveOvo?.subtitle
                        ?: "").spannedString
                incentiveOvoBtnContinueReview.apply {
                    setOnClickListener {
                        dismiss()
                        ReviewTracking.onClickContinueIncentiveOvoBottomSheetTracker(ReviewInboxTrackingConstants.PENDING_TAB)
                    }
                    text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.ctaText
                }
            }
            view.tgIncentiveOvoDescription.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.description
            val adapterIncentiveOvo = IncentiveOvoAdapter(productRevIncentiveOvoDomain.productrevIncentiveOvo?.numberedList
                    ?: emptyList(), incentiveOvoListener)
            view.rvIncentiveOvoExplain.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterIncentiveOvo
            }
            showKnob = true
            showCloseIcon = false
            showHeader = false
            isHideable = true
            isDragable = true
        }
    }

}