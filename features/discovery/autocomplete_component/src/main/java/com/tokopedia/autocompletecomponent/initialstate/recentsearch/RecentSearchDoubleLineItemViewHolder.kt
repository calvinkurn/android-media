package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteDoubleLineItemBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.renderstrategy.InitialStateLayoutStrategyFactory
import com.tokopedia.autocompletecomponent.initialstate.renderstrategy.InitialStateRenderStrategy
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class RecentSearchDoubleLineItemViewHolder(
    itemView: View,
    private val listener: RecentSearchListener,
    private val reimagineVariant: Search1InstAuto
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_autocomplete_double_line_item
    }

    private var binding: LayoutAutocompleteDoubleLineItemBinding? by viewBinding()

    private val layoutStrategy: InitialStateRenderStrategy = InitialStateLayoutStrategyFactory.create(reimagineVariant)

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
            ImageHandler.loadImageCircle2(itemView.context, it, item.imageUrl)
        }
    }

    private fun bindIconTitle(item: BaseItemInitialStateSearch) {
        val isReimagineVariantControl = reimagineVariant == Search1InstAuto.CONTROL
        val iconTitle = if (isReimagineVariantControl)
            binding?.iconTitle
        else
            binding?.autocompleteIconTitle
        layoutStrategy.bindIconTitle(iconTitle ?: return, item)
    }

    private fun bindIconSubtitle(item: BaseItemInitialStateSearch) {
        binding?.iconSubtitle?.shouldShowOrHideWithAction(item.iconSubtitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(it, item.iconSubtitle)
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
            ImageHandler.loadImage2(
                binding?.actionShortcutButton,
                item.shortcutImage,
                R.drawable.autocomplete_ic_remove
            )
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
