package com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.feature.ovoincentive.analytics.IncentiveOvoTracking
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.TncBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.IncentiveOvoTnCAdapter
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.layoutmanager.IncentiveOvoIllustrationLayoutManager
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.typefactory.IncentiveOvoIllustrationAdapterTypeFactory
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.viewholder.IncentiveOvoTnCViewHolder
import com.tokopedia.review.feature.ovoincentive.presentation.mapper.IncentiveOvoIllustrationMapper
import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoBottomSheetUiModel
import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoIllustrationUiModel
import com.tokopedia.review.inbox.databinding.IncentiveOvoTncBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx

class IncentiveOvoBottomSheet(context: Context): BottomSheetUnify(),
    IncentiveOvoTnCViewHolder.Listener {

    companion object {
        private const val BOTTOM_SHEET_WRAPPER_TOP_PADDING = 16
    }

    private var binding: IncentiveOvoTncBottomSheetBinding = IncentiveOvoTncBottomSheetBinding.inflate(LayoutInflater.from(context))
    private var incentiveOvoListener: IncentiveOvoListener? = null
    private val incentiveOvoIllustrationMapper by lazy(LazyThreadSafetyMode.NONE) { IncentiveOvoIllustrationMapper() }
    private val illustrationLayoutManager by lazy(LazyThreadSafetyMode.NONE) { IncentiveOvoIllustrationLayoutManager(context) }
    private val illustrationAdapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) { IncentiveOvoIllustrationAdapterTypeFactory() }
    private val illustrationAdapter by lazy(LazyThreadSafetyMode.NONE) { BaseAdapter(illustrationAdapterTypeFactory) }
    private val tncAdapter by lazy(LazyThreadSafetyMode.NONE) { IncentiveOvoTnCAdapter(emptyList(), this) }

    init {
        setChild(binding.root)
        showKnob = true
        showCloseIcon = false
        showHeader = false
        isHideable = true
        isDragable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupShowBehavior()
    }

    override fun onClickTnCLink(url: String): Boolean {
        return incentiveOvoListener?.onUrlClicked(url).orFalse()
    }

    private fun setupShowBehavior() {
        setShowListener {
            bottomSheetWrapper.setPadding(Int.ZERO, BOTTOM_SHEET_WRAPPER_TOP_PADDING.toPx(), Int.ZERO, Int.ZERO)
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                override fun onStateChanged(bottomSheet: View, state: Int) {
                    if (state == BottomSheetBehavior.STATE_COLLAPSED) dismiss()
                }
            })
        }
    }

    private fun setupIllustrationView(illustrations: List<IncentiveOvoIllustrationUiModel>) {
        with(binding.rvIncentiveOvoIllustrations) {
            if (layoutManager != illustrationLayoutManager) {
                layoutManager = illustrationLayoutManager
            }
            if (adapter != illustrationAdapter) {
                adapter = illustrationAdapter
            }
            illustrationAdapter.setElements(illustrations)
        }
    }

    private fun setupTermsAndConditionView(
        productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain,
        trackerData: TncBottomSheetTrackerData?,
        category: String,
        incentiveOvoListener: IncentiveOvoListener
    ) {
        this.incentiveOvoListener = incentiveOvoListener
        with(binding) {
            tgIncentiveOvoTitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.title.orEmpty()
            tgIncentiveOvoSubtitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.subtitle?.toSpannedHtmlLink(root.context) ?: ""
            incentiveOvoBtnContinueReview.apply {
                setOnClickListener { onContinueReview(productRevIncentiveOvoDomain, trackerData, category) }
                text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.ctaText.orEmpty()
            }
            tgIncentiveOvoDescription.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.description.orEmpty()
            if (rvIncentiveOvoExplain.adapter != tncAdapter) {
                rvIncentiveOvoExplain.adapter = tncAdapter
            }
            tncAdapter.setElements(productRevIncentiveOvoDomain.productrevIncentiveOvo?.numberedList.orEmpty())
        }
    }

    private fun setupDismissBehavior(
        productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain,
        trackerData: TncBottomSheetTrackerData?,
        category: String
    ) {
        setOnDismissListener {
            trackerData?.let {
                IncentiveOvoTracking.eventDismissTncBottomSheet(
                    message = productRevIncentiveOvoDomain.productrevIncentiveOvo?.description ?: ".",
                    reputationId = it.reputationId,
                    orderId = it.orderId,
                    productId = it.productId,
                    userId = it.userId
                )
                return@setOnDismissListener
            }
            ReviewTracking.onClickDismissIncentiveOvoBottomSheetTracker(category)
        }
    }

    private fun onContinueReview(
        productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain,
        trackerData: TncBottomSheetTrackerData?,
        category: String
    ) {
        dismiss()
        trackerData?.let {
            IncentiveOvoTracking.eventClickContinueTncBottomSheet(
                title = productRevIncentiveOvoDomain.productrevIncentiveOvo?.title ?: "",
                reputationId = it.reputationId,
                orderId = it.orderId,
                productId = it.productId,
                userId = it.userId
            )
            return
        }
        ReviewTracking.onClickContinueIncentiveOvoBottomSheetTracker(category)
    }

    private fun String.toSpannedHtmlLink(context: Context): CharSequence? {
        return HtmlLinkHelper(context = context, htmlString = this).spannedString
    }

    fun init(data: IncentiveOvoBottomSheetUiModel, incentiveOvoListener: IncentiveOvoListener) {
        setupIllustrationView(incentiveOvoIllustrationMapper.mapResponseToUiModel(data.productRevIncentiveOvoDomain))
        setupTermsAndConditionView(
            productRevIncentiveOvoDomain = data.productRevIncentiveOvoDomain,
            trackerData = data.trackerData,
            category = data.category,
            incentiveOvoListener = incentiveOvoListener
        )
        setupDismissBehavior(
            productRevIncentiveOvoDomain = data.productRevIncentiveOvoDomain,
            trackerData = data.trackerData,
            category = data.category
        )
    }
}