package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
            rootLayout.setOnClickListener {
                item.isSelected = !item.isSelected
                if (item.isSelected) selectView(holder, item) else unSelectView(holder, item)
            }
        }
    }

    private fun selectView(holder: RingkasanViewHolder, item: SummaryBeranda) {
        with(holder) {
            rootLayout.setBackgroundColor(
                ContextCompat.getColor(holder.rootLayout.context, selectedBackgroundColor)
            )
            bottomView.setBackgroundColor(item.selectedColor)
        }
    }

    private fun unSelectView(holder: RingkasanViewHolder, item: SummaryBeranda) {
        with(holder) {
            rootLayout.setBackgroundColor(
                ContextCompat.getColor(holder.rootLayout.context, unSelectedBackgroundColor)
            )
            bottomView.setBackgroundColor(
                ContextCompat.getColor(holder.rootLayout.context, unSelectedBackgroundColor)
            )
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
        val rootLayout: ConstraintLayout = view.findViewById(R.id.root_layout)
        val bottomView: View = view.findViewById(R.id.bottomView)
    }

    companion object {
        private val selectedBackgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_B100_44
        private val unSelectedBackgroundColor =
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        private val layout = R.layout.item_rv_ringkasan
        fun createInstance() = TopAdsBerandaSummaryRvAdapter()
    }
}