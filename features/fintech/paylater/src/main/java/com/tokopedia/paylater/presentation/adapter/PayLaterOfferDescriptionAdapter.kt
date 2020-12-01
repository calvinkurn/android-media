package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.presentation.viewholder.PayLaterOfferDescriptionViewHolder
import com.tokopedia.paylater.presentation.viewholder.PayLaterSeeMoreViewHolder
import com.tokopedia.paylater.presentation.viewholder.PayLaterUseViewHolder

class PayLaterOfferDescriptionAdapter(private val offerItemList: List<OfferDescriptionItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_FILLED_BUTTON -> PayLaterUseViewHolder.getViewHolder(inflater, parent)
            VIEW_GHOST_BUTTON -> PayLaterSeeMoreViewHolder.getViewHolder(inflater, parent)
            else -> PayLaterOfferDescriptionViewHolder.getViewHolder(inflater, parent)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PayLaterOfferDescriptionViewHolder ) {
            val descriptionData = offerItemList[position]
            holder.bindData(descriptionData)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == offerItemList.size) return VIEW_FILLED_BUTTON
        else if(position == offerItemList.size+1) return VIEW_GHOST_BUTTON
        else return VIEW_BENEFIT
    }

    override fun getItemCount(): Int {
        return offerItemList.size+2
    }

    companion object {
        const val VIEW_BENEFIT = 0
        const val VIEW_FILLED_BUTTON = 1
        const val VIEW_GHOST_BUTTON = 2
    }
}