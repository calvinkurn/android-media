package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationKeywordReimagineItemBinding
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_BORDER
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_CLEAR
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class InspirationKeywordReimagineItemViewHolder(
    itemView: View,
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val type: LayoutType,
    private val searchTerm: String,
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_keyword_reimagine_item
    }
    private var binding: SearchInspirationKeywordReimagineItemBinding? by viewBinding()

    fun bind(
        inspirationKeywordDataView: InspirationKeywordDataView,
    ) {
        val binding = binding ?: return
        setContainerView()
        setImage(inspirationKeywordDataView)
        setTitleKeyword(inspirationKeywordDataView)
        binding.root.setOnClickListener {
            inspirationKeywordListener.onInspirationKeywordItemClicked(inspirationKeywordDataView)
        }
    }

    private fun setContainerView() {
        val cardView = binding?.searchInspirationKeywordReimagineCard
        val isIconKeyword = type.isIconKeyword()
        cardView?.cardType = if(isIconKeyword) TYPE_BORDER else TYPE_CLEAR
        val marginHorizontal = if (isIconKeyword) cardView?.context?.getMarginHorizontal().orZero() else 0
        val marginVertical = if (isIconKeyword) cardView?.context?.getMarginVertical().orZero() else 0
        binding?.searchInspirationKeywordReimagineContainer?.setMargin(marginHorizontal, marginVertical, marginHorizontal, marginVertical)
    }

    private fun setImage(inspirationKeywordDataView: InspirationKeywordDataView) {
        setIconKeyword(inspirationKeywordDataView)
        setImageKeyword(inspirationKeywordDataView)
    }

    private fun setIconKeyword(inspirationKeywordDataView: InspirationKeywordDataView) {
        val iconKeyword = binding?.searchInspirationKeywordReimagineIcon
        iconKeyword?.shouldShowWithAction(type.isIconKeyword()){
            iconKeyword.loadImage(inspirationKeywordDataView.imageKeyword)
        }
    }

    private fun setImageKeyword(inspirationKeywordDataView: InspirationKeywordDataView) {
        val iconKeyword = binding?.searchInspirationKeywordReimagineImage
        iconKeyword?.shouldShowWithAction(!type.isIconKeyword()){
            iconKeyword.loadImage(inspirationKeywordDataView.imageKeyword)
        }
    }

    private fun setTitleKeyword(inspirationKeywordDataView: InspirationKeywordDataView) {
        val titleTextView = binding?.searchInspirationKeywordReimagineKeyword
        titleTextView?.isSingleLine = type.isSingleLine()
        titleTextView?.text =
            if (type.isFunneling()) {
                inspirationKeywordDataView.keyword.toSpanTitle()
            } else
                inspirationKeywordDataView.keyword
    }

    private fun String.toSpanTitle() : SpannableString {
        val textKeyword = this
        val startIndex = textKeyword.indexOf(searchTerm, ignoreCase = true)
        val highlightedTitle = SpannableString(textKeyword)
        highlightedTitle.setSpan(
            getBoldStyle(itemView.context),
            startIndex,
            startIndex + searchTerm.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return highlightedTitle
    }

    private fun getBoldStyle(context: Context?): Any {
        val typeface = context?.let { getTypeface(it, "OpenSauceOneExtraBold.ttf") }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && typeface != null)
            TypefaceSpan(typeface)
        else
            StyleSpan(Typeface.BOLD)
    }

    private fun Context.getMarginVertical(): Int =
        this
            .resources
            ?.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8)
            ?: 0
    private fun Context.getMarginHorizontal(): Int =
        this
            .resources
            ?.getDimensionPixelSize(R.dimen.search_result_page_product_inspiration_keyword_vertical)
            ?: 0
}
