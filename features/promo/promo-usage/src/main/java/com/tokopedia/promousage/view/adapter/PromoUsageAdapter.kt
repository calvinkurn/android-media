package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.constant.PromoConstant
import com.tokopedia.promousage.view.model.PromoSection
import com.tokopedia.promousage.view.model.PromoTncUiModel
import com.tokopedia.promousage.view.viewholder.PromoUsageTncViewHolder

class PromoUsageAdapter(
    asyncDifferConfig: AsyncDifferConfig<PromoSection>,
    private val listener: Listener? = null
) : ListAdapter<PromoSection, RecyclerView.ViewHolder>(asyncDifferConfig) {

    private val data = mutableListOf<PromoSection>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            PromoUsageTncViewHolder.LAYOUT -> PromoUsageTncViewHolder(view)
            else -> throw RuntimeException("Type not supported!") // TODO: Temporary handling, change to more appropriate handling
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PromoUsageTncViewHolder -> holder.bind(data[position] as PromoTncUiModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].type) {
            // TODO: Update with appropriate view holder
            PromoConstant.PROMO_SECTION_RECOMMENDATION -> 0
            PromoConstant.PROMO_SECTION_PAYMENT -> 1
            PromoConstant.PROMO_SECTION_SHIPPING -> 2
            PromoConstant.PROMO_SECTION_OTHER -> 3
            PromoConstant.PROMO_SECTION_INPUT_CODE -> 4
            PromoConstant.PROMO_SECTION_TNC -> PromoUsageTncViewHolder.LAYOUT
            else -> throw RuntimeException("Type not supported!") // TODO: Temporary handling, change to more appropriate handling
        }
    }

    interface Listener {

    }
}
