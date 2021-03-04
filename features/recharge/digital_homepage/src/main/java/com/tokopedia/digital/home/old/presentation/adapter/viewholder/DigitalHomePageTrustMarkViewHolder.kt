package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageTrustMarkModel
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemTrustMarkAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_trustmark.view.*

class DigitalHomePageTrustMarkViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageTrustMarkModel>(itemView) {

    override fun bind(element: DigitalHomePageTrustMarkModel) {
        val layoutManager = GridLayoutManager(itemView.context, TRUST_MARK_SPAN_COUNT)
        itemView.rv_digital_homepage_trust_mark.layoutManager = layoutManager
        if (element.isLoaded) {
            if (element.isSuccess
                    && element.data != null
                    && element.data.section.items.isNotEmpty()) {
                with (element.data.section) {
                    itemView.view_digital_homepage_trust_mark_shimmering.hide()
                    itemView.view_digital_homepage_trust_mark_container.show()
                    itemView.rv_digital_homepage_trust_mark.adapter = DigitalItemTrustMarkAdapter(items)
                }
            } else {
                itemView.view_digital_homepage_trust_mark_shimmering.hide()
                itemView.view_digital_homepage_trust_mark_container.hide()
            }
        } else {
            itemView.view_digital_homepage_trust_mark_shimmering.show()
            itemView.view_digital_homepage_trust_mark_container.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_trustmark
        const val TRUST_MARK_SPAN_COUNT = 3
    }
}