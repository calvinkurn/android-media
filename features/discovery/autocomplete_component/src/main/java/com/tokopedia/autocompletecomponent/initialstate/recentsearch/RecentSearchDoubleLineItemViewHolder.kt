package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteDoubleLineItemBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateLayoutStrategyFactory
import com.tokopedia.autocompletecomponent.initialstate.InitialStateLayoutStrategy
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.media.loader.loadImageWithError
import com.tokopedia.utils.view.binding.viewBinding

class RecentSearchDoubleLineItemViewHolder(
    itemView: View,
    private val listener: RecentSearchListener,
    private val isReimagine: Boolean
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_autocomplete_double_line_item
    }

    private var binding: LayoutAutocompleteDoubleLineItemBinding? by viewBinding()

    private val layoutStrategy: InitialStateLayoutStrategy = InitialStateLayoutStrategyFactory.create(isReimagine)

    fun bind(item: BaseItemInitialStateSearch) {
        bindIconImage(item)
        bindIconTitle(item)
        bindIconSubtitle(item)
        bindSubtitle(item)
        bindTitle(item)
        bindLabel(item)
        bindRemoveButton(item)
        bindListener(item)
    }

    private fun bindIconImage(item: BaseItemInitialStateSearch) {
        binding?.iconImage?.showWithAction(item.imageUrl.isNotEmpty()) {
            it.loadImageCircle(item.imageUrl)
        }
    }

    private fun bindIconTitle(item: BaseItemInitialStateSearch) {
        val iconTitle = binding?.iconTitle ?: return
        val autoCompleteIconTitleReimagine = binding?.autoCompleteIconTitleReimagine ?: return
        layoutStrategy.bindIconTitle(iconTitle, autoCompleteIconTitleReimagine, item)
    }

    private fun bindIconSubtitle(item: BaseItemInitialStateSearch) {
        binding?.iconSubtitle?.shouldShowOrHideWithAction(item.iconSubtitle.isNotEmpty()) {
            it.loadImage(item.iconSubtitle)
        }
    }

    private fun bindSubtitle(item: BaseItemInitialStateSearch) {
        binding?.doubleLineSubtitle?.setTextAndCheckShow(MethodChecker.fromHtml(item.subtitle).toString())
    }

    private fun bindTitle(item: BaseItemInitialStateSearch) {
        binding?.doubleLineTitle?.setTextAndCheckShow(MethodChecker.fromHtml(item.title).toString())

        setTitleMaxLine(item)
    }

    private fun setTitleMaxLine(item: BaseItemInitialStateSearch) {
        if (item.subtitle.isEmpty()) {
            binding?.doubleLineTitle?.maxLines = 2
        }
    }

    private fun bindLabel(item: BaseItemInitialStateSearch) {
        binding?.doubleLineLabel?.setTextAndCheckShow(item.label)
        if(binding?.doubleLineLabel?.text?.isNotEmpty() == true) {
            binding?.doubleLineLabel?.setLabelType(0)
        }
    }

    private fun bindRemoveButton(item: BaseItemInitialStateSearch) {
        binding?.actionShortcutButton?.shouldShowWithAction(item.shortcutImage.isNotEmpty()) {
            binding?.actionShortcutButton?.loadImageWithError(item.shortcutImage, R.drawable.autocomplete_ic_remove)
        }
    }

    private fun bindListener(item: BaseItemInitialStateSearch) {
        binding?.actionShortcutButton?.setOnClickListener { _ ->
            listener.onDeleteRecentSearchItem(item)
        }
        binding?.autocompleteDoubleLineItem?.setOnClickListener { _ ->
            listener.onRecentSearchItemClicked(item)
        }
    }

    private fun <T : View> T?.showWithAction(shouldShow: Boolean, action: (T) -> Unit) {
        if (this == null) return

        if (shouldShow) {
            action(this)
        }
    }

    private fun <T : View> T?.shouldShowOrHideWithAction(shouldShow: Boolean, action: (T) -> Unit) {
        if (this == null) return

        if (shouldShow) {
            visibility = View.VISIBLE
            action(this)
        }else {
            visibility = View.GONE
        }
    }
}
