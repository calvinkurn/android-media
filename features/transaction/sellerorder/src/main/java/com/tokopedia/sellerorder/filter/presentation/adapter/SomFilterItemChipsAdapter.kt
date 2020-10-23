package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips.view.*

class SomFilterItemChipsAdapter(private val somFilterListener: SomFilterListener) : RecyclerView.Adapter<SomFilterItemChipsAdapter.ChipsListViewHolder>() {

    private var chipsFilterList: MutableList<SomFilterChipsUiModel>? = null
    private var isSelectMany: Boolean = false

    fun setChipsFilter(sortFilterList: List<SomFilterChipsUiModel>, isSelectMany: Boolean) {
        this.chipsFilterList = sortFilterList.toMutableList()
        this.isSelectMany = isSelectMany
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips, parent, false)
        return ChipsListViewHolder(view, somFilterListener)
    }

    override fun getItemCount(): Int {
        return chipsFilterList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChipsListViewHolder, position: Int) {
        chipsFilterList?.get(position)?.let { holder.bind(it) }
    }

    class ChipsListViewHolder(itemView: View, private val somFilterListener: SomFilterListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SomFilterChipsUiModel) {
            with(itemView) {
                chipsItem.apply {
                    centerText = true
                    chipText = data.name
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = if (data.isSelected) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                    setOnClickListener {

                    }
                }
            }
        }
    }
}