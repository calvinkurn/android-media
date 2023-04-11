package com.tokopedia.autocompletecomponent.initialstate.chips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutTitleChipWidgetBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.view.binding.viewBinding

class InitialStateChipWidgetTitleViewHolder(
    itemView: View,
) : AbstractViewHolder<InitialStateChipWidgetTitleDataView>(itemView) {
    private var binding: LayoutTitleChipWidgetBinding? by viewBinding()

    override fun bind(element: InitialStateChipWidgetTitleDataView) {
        val title = element.title
        val titleTextView = binding?.autoCompleteLayout?.titleTextView ?: return

        titleTextView.shouldShowWithAction(title.isNotEmpty()) {
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                titleTextView,
                title,
                getString(R.string.content_desc_titleTextView)
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_chip_widget
    }
}
