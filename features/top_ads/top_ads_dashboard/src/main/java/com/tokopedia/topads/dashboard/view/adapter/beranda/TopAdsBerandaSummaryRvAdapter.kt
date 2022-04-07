package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.beranda.SummaryBeranda
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TopAdsBerandaSummaryRvAdapter :
    RecyclerView.Adapter<TopAdsBerandaSummaryRvAdapter.RingkasanViewHolder>() {

    private val selectedBackgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_B100_44
    private val unSelectedBackgroundColor =
        com.tokopedia.unifyprinciples.R.color.Unify_Static_White
    private val list = mutableListOf<SummaryBeranda>()
    var infoClicked: (() -> Unit)? = null
    var itemClicked: ((Set<SummaryBeranda>) -> Unit)? = null
    private val selectedItems = mutableSetOf<SummaryBeranda>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingkasanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_ringkasan, parent, false)
        val holder = RingkasanViewHolder(view)
        holder.ivInformation.setOnClickListener { infoClicked?.invoke() }
        return holder
    }

    override fun onBindViewHolder(holder: RingkasanViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        with(holder) {
            title.text = item.title
            txtValue.text = item.count.toString()
            txtPercentageChange.text = HtmlCompat.fromHtml(
                String.format(
                    txtPercentageChange.context.resources.getString(R.string.topads_dash_from_last_week),
                    item.percentCount
                ),HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            if (item.isSelected) selectView(holder, item) else unSelectView(holder, item)

            rootLayout.setOnClickListener {
                item.isSelected = !item.isSelected
                if (item.isSelected) {
                    selectedItems.add(item)
                    selectView(holder, item)
                } else {
                    selectedItems.remove(item)
                    unSelectView(holder, item)
                }
                itemClicked?.invoke(selectedItems)
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
        selectedItems.clear()
        list.addAll(items)

        selectedItems.add(list[0].also {
            it.isSelected = true
        })  //by default 1st item will be selected
        itemClicked?.invoke(selectedItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class RingkasanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: Typography = view.findViewById(R.id.txtTitle)
        val ivInformation: ImageUnify = view.findViewById(R.id.ivInformation)
        val txtValue: Typography = view.findViewById(R.id.txtSubTitle)
        val txtPercentageChange: Typography = view.findViewById(R.id.txtPercentageChange)
        val rootLayout: ConstraintLayout = view.findViewById(R.id.root_layout)
        val bottomView: View = view.findViewById(R.id.bottomView)
    }
}