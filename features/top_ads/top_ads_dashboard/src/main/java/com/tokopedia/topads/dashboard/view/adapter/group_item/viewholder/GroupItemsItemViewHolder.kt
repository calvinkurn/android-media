package com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.NOT_VALID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_AKTIF
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsItemModel
import com.tokopedia.topads.dashboard.view.sheet.TopadsSelectActionSheet
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import kotlinx.android.synthetic.main.topads_dash_item_with_group_card.view.*


/**
 * Created by Pika on 2/6/20.
 */

private const val CLICK_ATUR_IKLAN = "click - atur iklan"

class GroupItemsItemViewHolder(val view: View, var selectMode: ((select: Boolean) -> Unit),
                               var actionDelete: ((pos: Int) -> Unit),
                               var actionStatusChange: ((pos: Int, status: Int) -> Unit),
                               private var editDone: ((groupId: Int) -> Unit),
                               private var onClickItem: ((id: Int, priceSpent: String, groupName: String) -> Unit)) : GroupItemsViewHolder<GroupItemsItemModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_with_group_card
    }

    private val sheet: TopadsSelectActionSheet? by lazy(LazyThreadSafetyMode.NONE) {
        TopadsSelectActionSheet.newInstance()
    }

    override fun bind(item: GroupItemsItemModel, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableList<DataItem>, countList: MutableList<CountDataItem>) {
        item.let {

            if(item.data.strategies.isNotEmpty() && item.data.strategies[0].isNotEmpty()) {
                view.img_key.visibility = View.GONE
                view.key_count.visibility = View.GONE
            } else {
                view.img_key.visibility = View.VISIBLE
                view.key_count.visibility = View.VISIBLE
            }
            view.img.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_folder))
            view.img_total.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_total))
            view.img_key.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_key))
            view.scheduleImg.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
            view.img_menu.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_topads_menu))
            if (selectedMode) {
                view.img_menu.visibility = View.INVISIBLE
                view.check_box.visibility = View.VISIBLE
            } else {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
                view.img_menu.visibility = View.VISIBLE
                view.check_box.visibility = View.GONE
                view.check_box.isChecked = false
                it.isChecked = false
            }
            view.check_box.isChecked = it.isChecked
            if (!view.check_box.isChecked) {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
            } else {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
            }
            when (it.data.groupStatusDesc) {
                ACTIVE -> view.label.setLabelType(Label.GENERAL_DARK_GREEN)
                TIDAK_AKTIF -> view.label.setLabelType(Label.GENERAL_LIGHT_ORANGE)
                TIDAK_TAMPIL -> view.label.setLabelType(Label.GENERAL_LIGHT_GREY)
            }
            view.group_title.text = it.data.groupName
            view.label.text = it.data.groupStatusDesc
            if (countList.isNotEmpty() && adapterPosition < countList.size && adapterPosition != RecyclerView.NO_POSITION) {
                view.total_item.text = countList[adapterPosition].totalAds.toString()
                view.key_count.text = countList[adapterPosition].totalKeywords.toString()
            }
            setProgressBar(it.data)
            statsData.forEachIndexed { index, stats ->
                if (stats.groupId == it.data.groupId) {
                    view.tampil_count.text = statsData[index].statTotalImpression
                    view.klik_count.text = statsData[index].statTotalClick
                    view.persentase_klik_count.text = statsData[index].statTotalCtr
                    view.pengeluaran_count.text = statsData[index].statTotalSpent
                    view.pendapatan_count.text = statsData[index].groupTotalIncome
                    view.produk_terjual_count.text = statsData[index].statTotalConversion
                }
            }
            view.item_card?.setOnClickListener { _ ->
                if (!selectedMode) {
                    if (item.data.groupPriceDailyBar.isNotEmpty())
                        onClickItem.invoke(item.data.groupId, item.data.groupPriceDailySpentFmt, item.data.groupName)
                    else
                        onClickItem.invoke(item.data.groupId, NOT_VALID, item.data.groupName)
                } else {
                    view.check_box.isChecked = !view.check_box.isChecked
                    it.isChecked = view.check_box.isChecked
                    if (view.check_box.isChecked)
                        view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                    else
                        view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
                }
            }
            view.item_card.setOnLongClickListener {
                item.isChecked = true
                view.check_box.isChecked = true
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                selectMode.invoke(true)
                true
            }
        }

        view.img_menu.setOnClickListener {
            sheet?.show(((view.context as FragmentActivity).supportFragmentManager), item.data.groupStatus, item.data.groupName)
            sheet?.onEditAction = {
                editDone.invoke(item.data.groupId)
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_ATUR_IKLAN, "")
            }
            sheet?.onDeleteClick = {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionDelete(adapterPosition)
            }
            sheet?.changeStatus = {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionStatusChange(adapterPosition, it)
            }
        }
    }

    private fun setProgressBar(data: DataItem) {
        if (data.groupPriceDailyBar.isNotEmpty()) {
            view.progress_layout.visibility = View.VISIBLE
            view.progress_bar.progressBarColorType = ProgressBarUnify.COLOR_GREEN
            try {
                view.progress_bar.setValue(Utils.convertMoneyToValue(data.groupPriceDailySpentFmt), true)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            view.progress_status1.text = data.groupPriceDailySpentFmt
            view.progress_status2.text = String.format(view.context.resources.getString(com.tokopedia.topads.common.R.string.topads_dash_group_item_progress_status), data.groupPriceDaily)
        } else {
            view.progress_layout.visibility = View.GONE
        }
    }

}