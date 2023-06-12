package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.helper.widget.Layer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AFFILIATE_PROMOTE_HOME
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliatePerformaClickInterfaces
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifyprinciples.Typography

class AffiliateAdpUserDataVH(
    itemView: View,
    private val onPerformaGridClick: AffiliatePerformaClickInterfaces?
) : AbstractViewHolder<AffiliateUserPerformanceModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_list_item

        private const val SPAN_TWO = 2
        private const val SPAN_ONE = 1
    }

    private var adapter =
        AffiliateAdapter(AffiliateAdapterFactory(onPerformaGridClick = onPerformaGridClick))
    private val isAffiliatePromoteHomeEnabled =
        RemoteConfigInstance.getInstance()?.abTestPlatform?.getString(
            AFFILIATE_PROMOTE_HOME,
            ""
        ) == AFFILIATE_PROMOTE_HOME

    override fun bind(element: AffiliateUserPerformanceModel?) {
        val performRV = itemView.findViewById<RecyclerView>(R.id.performaItem_RV)
        performRV.layoutManager = GridLayoutManager(itemView.context, 2).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) {
                        SPAN_TWO
                    } else {
                        SPAN_ONE
                    }
                }
            }
        }
        performRV.adapter = adapter
        adapter.resetList()
        adapter.addMoreData(element?.data)
        itemView.findViewById<Typography>(R.id.head).apply {
            setText(R.string.link_dengan_performa)
        }

        itemView.findViewById<Layer>(R.id.link_history_group).apply {
            isVisible = isAffiliatePromoteHomeEnabled
            setOnClickListener { onPerformaGridClick?.onPromoHistoryClick() }
        }
    }
}
