package com.tokopedia.recommendation_widget_common.widget.foryou.state.viewholder

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.baselist.R as baselistR
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.EmptyStateModel

class EmptyStateViewHolder(view: View) : AbstractViewHolder<EmptyStateModel>(view) {

    private val title = view.findViewById<TextView>(baselistR.id.text_view_empty_title_text)
    private val description = view.findViewById<TextView>(baselistR.id.text_view_empty_content_text)
    private val buttonRetry = view.findViewById<Button>(baselistR.id.button_add_promo)

    override fun bind(element: EmptyStateModel) {
        buttonRetry.gone()
        title.setText(R.string.recom_card_empty_title)
        description.setText(R.string.recom_card_empty_description)
    }
}
