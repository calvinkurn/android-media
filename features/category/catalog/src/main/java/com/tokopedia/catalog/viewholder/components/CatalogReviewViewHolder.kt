package com.tokopedia.catalog.viewholder.components

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.gallery.CatalogImageReviewAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

class CatalogReviewViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {


    companion object {
        const val MAX_LINES_REVIEW_DESCRIPTION = 3
        const val GRID_LAYOUT_MANAGER_SPAN_COUNT = 5
        const val RATING_ONE = 1
        const val RATING_TWO = 2
        const val RATING_THREE = 3
        const val RATING_FOUR = 4
        const val RATING_FIVE = 5

        val LAYOUT = R.layout.item_catalog_review

    }

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    private var catalogDetailListener : CatalogDetailListener? = null

    fun bind(model: CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review, listener: CatalogDetailListener?) {
        catalogDetailListener = listener
        setReviewStars(model.rating)
        view.findViewById<Typography>(R.id.txt_user_name_catalog)?.displayTextOrHide(model.reviewerName ?: "")
        view.findViewById<Typography>(R.id.txt_date_user_catalog)?.displayTextOrHide(model.reviewDate ?: "")
        setReviewDescription(model.reviewText ?: "")
        view.findViewById<RecyclerView>(R.id.image_review_rv_catalog)
        renderReviewImage(model,catalogDetailListener)
    }

    private fun getRatingDrawable(param: Int): Int {
        return when (param) {
            RATING_ONE -> R.drawable.catalog_ic_rating_star_one
            RATING_TWO -> R.drawable.catalog_ic_rating_star_two
            RATING_THREE -> R.drawable.catalog_ic_rating_star_three
            RATING_FOUR -> R.drawable.catalog_ic_rating_star_four
            RATING_FIVE -> R.drawable.catalog_ic_rating_star_five
            else -> R.drawable.catalog_ic_rating_star_zero
        }
    }

    private fun setReviewStars(rating: Int?) {
        view.findViewById<ImageView>(R.id.rating_review_catalog)?.apply {
            if (rating == 0 || rating == null) {
                hide()
                return
            }
            setImageDrawable(MethodChecker.getDrawable(context, getRatingDrawable(rating)))
            show()
        }
    }

    private fun setReviewDescription(description : String) {
        if (description.isEmpty()) {
            view.findViewById<Typography>(R.id.txt_desc_review_catalog)?.hide()
            return
        }
        view.findViewById<Typography>(R.id.txt_desc_review_catalog)?.apply {
            maxLines = MAX_LINES_REVIEW_DESCRIPTION
            val formattingResult = reviewDescFormatter(context, description)
            text = formattingResult.first
            if(formattingResult.second) {
                setOnClickListener {
                    maxLines = Integer.MAX_VALUE
                    text = HtmlLinkHelper(context, description).spannedString
                }
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

    private fun renderReviewImage(model: CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review , catalogDetailListener: CatalogDetailListener?) {
        val catalogReviewImages = arrayListOf<CatalogImage>()
        model.reviewImageUrl?.forEach {
            catalogReviewImages.add(CatalogImage(it,false))
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.image_review_rv_catalog)
        recyclerView.show()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = CatalogImageReviewAdapter(catalogReviewImages,false, catalogDetailListener)
    }
}