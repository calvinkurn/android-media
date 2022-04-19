package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemModel
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.topads_dash_item_moveto_group.view.*

class MovetoGroupItemViewHolder(val view: View, var itemSelected: ((pos: Int) -> Unit)) : MovetoGroupViewHolder<MovetoGroupItemModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_moveto_group
    }

    init {
        view.radio_button.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION)
                itemSelected.invoke(adapterPosition)
        }

        view.groupCard.setOnClickListener { view ->
            view.radio_button.isChecked = true
            if (adapterPosition != RecyclerView.NO_POSITION)
                itemSelected.invoke(adapterPosition)
        }
    }

    override fun bind(item: MovetoGroupItemModel, lastSelected: Int, countList: MutableList<CountDataItem>) {
        item.let {
            view.img.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_folder))
            view.group_title.text = it.result.groupName
            view.radio_button.isChecked = lastSelected == adapterPosition
            when (it.result.groupStatusDesc) {
                TopAdsDashboardConstant.ACTIVE -> view.label.setLabelType(Label.GENERAL_LIGHT_GREEN)
                TopAdsDashboardConstant.TIDAK_AKTIF -> view.label.setLabelType(Label.GENERAL_LIGHT_GREY)
                TopAdsDashboardConstant.TIDAK_TAMPIL -> view.label.setLabelType(Label.GENERAL_LIGHT_ORANGE)
            }
            view.label.text = it.result.groupStatusDesc
            if (countList.isNotEmpty() && adapterPosition < countList.size) {
                view.productCount.text = countList[adapterPosition].totalAds.toString()
                view.key_count.text = countList[adapterPosition].totalKeywords.toString()
            }
            view.img_total.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_total))
            view.img_key.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_key))
        }
    }
}