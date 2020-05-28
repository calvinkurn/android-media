package com.tokopedia.product.addedit.detail.presentation.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.adapter.NameRecommendationAdapter
import java.util.*

class NameRecommendationViewHolder(itemView: View, clickListener: NameRecommendationAdapter.ProductNameItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private var nameRecommendationView: AppCompatTextView? = null

    init {
        nameRecommendationView = itemView.findViewById(R.id.name_recommendation_view)
        nameRecommendationView?.setOnClickListener {
            val nameObj = it.getTag(R.id.name)
            nameObj?.let { obj ->
                val name = obj as String
                clickListener.onNameItemClicked(name)
            }
        }
    }

    fun bindData(productNameInput: String, nameRecommendation: String) {
        nameRecommendationView?.setTag(R.id.name, nameRecommendation)
        nameRecommendationView?.text = highlightRecommendation(productNameInput, nameRecommendation)
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