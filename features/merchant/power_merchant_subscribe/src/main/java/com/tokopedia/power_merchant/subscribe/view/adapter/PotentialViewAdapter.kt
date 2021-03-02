package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.PotentialItemUiModel
import kotlinx.android.synthetic.main.item_pm_potential.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PotentialViewAdapter(
        private val items: List<PotentialItemUiModel>
) : RecyclerView.Adapter<PotentialViewAdapter.PotentialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PotentialViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pm_potential, parent, false)
        return PotentialViewHolder(view)
    }

    override fun onBindViewHolder(holder: PotentialViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class PotentialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PotentialItemUiModel) {
            with(itemView) {
                imgPmPotentialItem.loadImageWithoutPlaceholder(item.imgUrl)
                tvPmPotentialItemDescription.text = item.description.parseAsHtml()
            }
        }
    }
}