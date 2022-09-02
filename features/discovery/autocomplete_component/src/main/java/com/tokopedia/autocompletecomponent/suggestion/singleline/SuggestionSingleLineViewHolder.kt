package com.tokopedia.autocompletecomponent.suggestion.singleline

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteSingleLineItemBinding
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionListener
import com.tokopedia.autocompletecomponent.util.safeSetSpan
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class SuggestionSingleLineViewHolder(
        itemView: View,
        private val listener: SuggestionListener
): AbstractViewHolder<SuggestionSingleLineDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_single_line_item
    }
    private var binding: LayoutAutocompleteSingleLineItemBinding? by viewBinding()

    override fun bind(item: SuggestionSingleLineDataDataView) {
        bindIconImage(item.data)
        bindTextTitle(item.data)
        bindShortcutButton(item.data)
        bindListener(item.data)
    }

    private fun bindIconImage(item: BaseSuggestionDataView){
        binding?.iconImage?.let {
            if (item.isCircleImage()) {
                it.loadImageCircle(item.imageUrl)
            } else {
                it.loadImageRounded(
                    item.imageUrl,
                    itemView.context.resources.getDimension(R.dimen.autocomplete_product_suggestion_image_radius),
                    properties = { setErrorDrawable(R.drawable.autocomplete_ic_time) }
                )
            }
        }
    }

    private fun bindTextTitle(item: BaseSuggestionDataView){
        val binding = binding ?: return
        val highlightedTitle = SpannableString(item.title)
        if(item.isBoldAllText()){
            binding.singleLineTitle.setWeight(Typography.BOLD)
        } else {
            val startIndex = indexOfSearchQuery(item.title, item.searchTerm)
            if (startIndex == -1) {
                binding.singleLineTitle.setWeight(Typography.BOLD)
            }
            else {
                binding.singleLineTitle.setWeight(Typography.REGULAR)
                highlightedTitle.safeSetSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    startIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                highlightedTitle.safeSetSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndex + item.searchTerm.length,
                    highlightedTitle.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        binding.singleLineTitle.text = highlightedTitle
    }

    private fun indexOfSearchQuery(displayName: String, searchTerm: String): Int {
        return if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun bindShortcutButton(item: BaseSuggestionDataView){
        binding?.actionShortcutButton?.shouldShowWithAction(item.shortcutImage.isNotEmpty()) {
            ImageHandler.loadImage2(
                binding?.actionShortcutButton,
                item.shortcutImage,
                R.drawable.autocomplete_ic_copy_to_search_bar
            )
        }
    }

    private fun bindListener(item: BaseSuggestionDataView){
        binding?.autocompleteSingleLineItem?.setOnClickListener {
            listener.onItemClicked(item)
        }

        binding?.actionShortcutButton?.setOnClickListener {
            listener.copyTextToSearchView(item.title)
        }

        binding?.autocompleteSingleLineItem?.addOnImpressionListener(item, object: ViewHintListener {
            override fun onViewHint() {
                listener.onItemImpressed(item)
            }
        })
    }
}
