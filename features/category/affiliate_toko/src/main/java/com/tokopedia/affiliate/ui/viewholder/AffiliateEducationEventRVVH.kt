package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateEducationEventRVVH(
    itemView: View
) : AbstractViewHolder<AffiliateEducationEventRVUiModel>(itemView) {

    private var eventAdapter: AffiliateAdapter? = null

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_event_list
    }

    override fun bind(element: AffiliateEducationEventRVUiModel?) {
        eventAdapter =
            AffiliateAdapter(AffiliateAdapterFactory())
        val rvEvent = itemView.findViewById<RecyclerView>(R.id.rv_edu_event)
        val rvLayoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        rvEvent?.apply {
            layoutManager = rvLayoutManager
            adapter = eventAdapter
        }
        eventAdapter?.addMoreData(
            element?.eventList?.map { AffiliateEducationEventUiModel(it) }
        )
    }
}
