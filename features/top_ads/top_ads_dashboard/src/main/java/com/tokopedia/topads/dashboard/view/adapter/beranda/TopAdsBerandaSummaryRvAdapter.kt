package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.beranda.SummaryBeranda
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TopAdsBerandaSummaryRvAdapter :
    RecyclerView.Adapter<TopAdsBerandaSummaryRvAdapter.RingkasanViewHolder>() {

    private val list = mutableListOf<SummaryBeranda>()
    var infoClicked: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingkasanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val holder = RingkasanViewHolder(view)
        holder.ivInformation.setOnClickListener { infoClicked?.invoke() }
        return holder
    }

    override fun onBindViewHolder(holder: RingkasanViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        with(holder) {
            title.text = item.title
            txtValue.text = item.count.toString()
            txtPercentageChange.text = item.percentCount
        }
    }

    fun addItems(items: List<SummaryBeranda>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class RingkasanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: Typography = view.findViewById(R.id.txtTitle)
        val ivInformation: ImageUnify = view.findViewById(R.id.ivInformation)
        val txtValue: Typography = view.findViewById(R.id.txtValue)
        val txtPercentageChange: Typography = view.findViewById(R.id.txtPercentageChange)
    }

    companion object {
        private val layout = R.layout.item_rv_ringkasan
        fun createInstance() = TopAdsBerandaSummaryRvAdapter()
    }
}