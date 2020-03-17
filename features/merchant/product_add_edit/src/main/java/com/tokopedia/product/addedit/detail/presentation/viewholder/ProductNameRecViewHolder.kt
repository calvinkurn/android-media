package com.tokopedia.product.addedit.detail.presentation.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import java.util.*

class ProductNameRecViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var productNameRecommendationView: AppCompatTextView? = null

    init {
        productNameRecommendationView = itemView.findViewById(R.id.product_name_rec_view)
    }

    fun bindData(productNameInput: String, productNameRecommendation: String) {
        productNameRecommendationView?.text = highlightRecommendation(productNameInput, productNameRecommendation)
    }

    private fun highlightRecommendation(productNameInput: String, productNameRecommendation: String): SpannableString {
        val highlightedRecommendation = SpannableString(productNameRecommendation)
        val matchIndex = getMatchIndex(productNameRecommendation, productNameInput)
        return if (matchIndex == -1) highlightedRecommendation
        else {
            val startIndex = matchIndex + productNameInput.length
            highlightedRecommendation.setSpan(TextAppearanceSpan(itemView.context, R.style.bold),
                    startIndex,
                    productNameRecommendation.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            highlightedRecommendation
        }
    }

    private fun getMatchIndex(productNameRecommendation: String, productNameInput: String): Int {
        return if (!TextUtils.isEmpty(productNameInput)) {
            productNameRecommendation.toLowerCase(Locale.getDefault()).indexOf(productNameInput.toLowerCase(Locale.getDefault()))
        } else -1
    }
}