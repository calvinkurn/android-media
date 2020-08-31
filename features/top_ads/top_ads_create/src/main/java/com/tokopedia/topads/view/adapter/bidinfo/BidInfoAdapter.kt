package com.tokopedia.topads.view.adapter.bidinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoViewHolder

class BidInfoAdapter(private val typeFactory: BindInfoAdapterTypeFactory) : RecyclerView.Adapter<BidInfoViewHolder<BidInfoViewModel>>() {

    var items: MutableList<BidInfoViewModel> = mutableListOf()


    override fun onBindViewHolder(holder: BidInfoViewHolder<BidInfoViewModel>, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BidInfoViewHolder<BidInfoViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as BidInfoViewHolder<BidInfoViewModel>
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun isError(): Boolean {
        items.forEach {
            if (it is BidInfoItemViewModel) {
                if (it.isError) {
                    return true
                }
            }
        }
        return false
    }

}