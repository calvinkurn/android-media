package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify

class MovetoGroupItemViewHolder(
    private val view: View,
    private var itemSelected: ((pos: Int) -> Unit),
) : MovetoGroupViewHolder<MovetoGroupItemModel>(view) {

    private val groupCard: ConstraintLayout = view.findViewById(R.id.groupCard)
    private val img: ImageUnify = view.findViewById(R.id.img)
    private val groupTitle: Typography = view.findViewById(R.id.group_title)
    private val radioButton: RadioButtonUnify = view.findViewById(R.id.radio_button)
    private val label: Label = view.findViewById(R.id.label)
    private val imgTotal: ImageUnify = view.findViewById(R.id.img_total)
    private val productCount: Typography = view.findViewById(R.id.productCount)
    private val imgKey: ImageUnify = view.findViewById(R.id.img_key)
    private val keyCount: Typography = view.findViewById(R.id.key_count)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_moveto_group
    }

    init {
        radioButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION)
                itemSelected.invoke(adapterPosition)
        }

        groupCard.setOnClickListener { view ->
            radioButton.isChecked = true
            if (adapterPosition != RecyclerView.NO_POSITION)
                itemSelected.invoke(adapterPosition)
        }
    }

    override fun bind(
        item: MovetoGroupItemModel,
        lastSelected: Int,
        countList: MutableList<CountDataItem>,
    ) {
        item.let {
            img.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_folder))
            groupTitle.text = it.result.groupName
            radioButton.isChecked = lastSelected == adapterPosition
            when (it.result.groupStatusDesc) {
                TopAdsDashboardConstant.ACTIVE -> label.setLabelType(Label.GENERAL_LIGHT_GREEN)
                TopAdsDashboardConstant.TIDAK_AKTIF -> label.setLabelType(Label.GENERAL_LIGHT_GREY)
                TopAdsDashboardConstant.TIDAK_TAMPIL -> label.setLabelType(Label.GENERAL_LIGHT_ORANGE)
            }
            label.text = it.result.groupStatusDesc
            if (countList.isNotEmpty() && adapterPosition < countList.size) {
                productCount.text = countList[adapterPosition].totalAds.toString()
                keyCount.text = countList[adapterPosition].totalKeywords.toString()
            }
            imgTotal.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_total))
            imgKey.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_key))
        }
    }
}
