package com.tokopedia.catalog.viewholder.components

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.gallery.CatalogImageReviewAdapter
import com.tokopedia.catalog.analytics.CatalogDetailAnalytics
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.ShopReviewBasicInfoWidget
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CatalogReviewViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        const val MAX_LINES_REVIEW_DESCRIPTION = 3

        val LAYOUT = R.layout.item_catalog_review
    }

    private var isFromBottomSheet = false
    private var catalogName = ""
    private var catalogId = ""

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    private var catalogDetailListener: CatalogDetailListener? = null

    private val shopReviewWidget: ShopReviewBasicInfoWidget by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_shop_review_widget)
    }

    fun bind(
        model: CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review,
        listener: CatalogDetailListener?,
        argIsFromBottomSheet: Boolean = false,
        argCatalogName: String = "",
        argCatalogId: String = ""
    ) {
        view.setOnClickListener {
            listener?.onReviewClicked(bindingAdapterPosition, model.productUrl ?: "", isFromBottomSheet)
        }
        isFromBottomSheet = argIsFromBottomSheet
        catalogName = argCatalogName
        catalogId = argCatalogId
        catalogDetailListener = listener

        renderWidget(model)
        setReviewDescription(model.reviewText ?: "", model.reviewId ?: "")
        view.findViewById<RecyclerView>(R.id.image_review_rv_catalog)
        renderReviewImage(model, catalogDetailListener)
    }

    private fun renderWidget(model: CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review) {
        shopReviewWidget.setRating(model.rating ?: 0)
        shopReviewWidget.setReviewerName(model.reviewerName ?: "")
        shopReviewWidget.setCreateTime(model.reviewDate ?: "")
    }

    private fun setReviewDescription(description: String, reviewId: String) {
        if (description.isEmpty()) {
            view.findViewById<Typography>(R.id.txt_desc_review_catalog)?.hide()
            return
        }
        view.findViewById<Typography>(R.id.txt_desc_review_catalog)?.apply {
            if (!isFromBottomSheet) {
                maxLines = MAX_LINES_REVIEW_DESCRIPTION
                val formattingResult = reviewDescFormatter(context, description)
                text = formattingResult.first
                if (formattingResult.second) {
                    setOnClickListener {
                        CatalogDetailAnalytics.sendEvent(
                            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                            CatalogDetailAnalytics.ActionKeys.CLICK_SELENGKAPNYA_ON_REVIEW,
                            "$catalogName - $catalogId - $reviewId",
                            UserSession(context).userId,
                            catalogId
                        )
                        maxLines = Integer.MAX_VALUE
                        text = HtmlLinkHelper(context, description).spannedString
                    }
                }
            } else {
                text = description
            }

            show()
        }
    }

    private fun reviewDescFormatter(context: Context, review: String): Pair<CharSequence?, Boolean> {
        val MAX_CHAR = 140
        val ALLOW_CLICK = true

        val formattedText = HtmlLinkHelper(context, review).spannedString ?: ""
        return if (formattedText.length > MAX_CHAR) {
            val subDescription = formattedText.substring(0, MAX_CHAR)
            Pair(HtmlLinkHelper(context, subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... " + context.getString(com.tokopedia.catalog.R.string.catalog_review_expand)).spannedString, ALLOW_CLICK)
        } else {
            Pair(formattedText, !ALLOW_CLICK)
        }
    }

    private fun renderReviewImage(model: CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review, catalogDetailListener: CatalogDetailListener?) {
        val catalogReviewImages = arrayListOf<CatalogImage>()
        model.reviewImageUrl?.forEach {
            catalogReviewImages.add(CatalogImage(it, false, ""))
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.image_review_rv_catalog)
        recyclerView.show()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = CatalogImageReviewAdapter(catalogReviewImages, model.reviewId ?: "", isFromBottomSheet, catalogDetailListener)
    }
}
