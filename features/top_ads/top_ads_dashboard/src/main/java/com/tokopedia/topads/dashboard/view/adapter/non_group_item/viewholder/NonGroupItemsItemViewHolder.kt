package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_AKTIF
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsItemModel
import com.tokopedia.topads.dashboard.view.sheet.TopadsSelectActionSheet
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.*


/**
 * Created by Pika on 2/6/20.
 */

class NonGroupItemsItemViewHolder(val view: View,
                                  var selectMode: ((select: Boolean) -> Unit),
                                  var actionDelete: ((pos: Int) -> Unit),
                                  var actionStatusChange: ((pos: Int, status: Int) -> Unit),
                                  var editDone: ((groupId: Int, adPriceBid: Int) -> Unit)) : NonGroupItemsViewHolder<NonGroupItemsItemModel>(view) {
    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_non_group_card
    }

    private val sheet: TopadsSelectActionSheet? by lazy(LazyThreadSafetyMode.NONE) {
        TopadsSelectActionSheet.newInstance()
    }

    override fun bind(item: NonGroupItemsItemModel, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableList<WithoutGroupDataItem>) {
        view.img_menu.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_topads_menu))
        item.let {
            when (it.data.adStatusDesc) {

                ACTIVE -> {
                    view.label.setLabelType(Label.GENERAL_LIGHT_GREEN)
                }
                TIDAK_AKTIF -> {
                    view.label.setLabelType(Label.GENERAL_LIGHT_ORANGE)
                }
                TIDAK_TAMPIL -> {
                    view.label.setLabelType(Label.GENERAL_LIGHT_GREY)
                }
            }
            if (selectedMode) {
                view.img_menu.visibility = View.GONE
                view.check_box.visibility = View.VISIBLE
                view.label.visibility = View.INVISIBLE
            } else {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
                view.img_menu.visibility = View.VISIBLE
                view.check_box.visibility = View.GONE
                view.label.visibility = View.VISIBLE
            }

            view.label.text = it.data.adStatusDesc
            view.product_img.setImageUrl(it.data.productImageUri)
            view.product_name.text = it.data.productName
            setProgressBar(it.data)
            view.check_box.isChecked = item.isChecked

            if (statsData.isNotEmpty() && adapterPosition < statsData.size  && adapterPosition != RecyclerView.NO_POSITION) {
                view.tampil_count.text = statsData[adapterPosition].statTotalImpression
                view.klik_count.text = statsData[adapterPosition].statTotalClick
                view.persentase_klik_count.text = statsData[adapterPosition].statTotalCtr
                view.pengeluaran_count.text = statsData[adapterPosition].statTotalSpent
                view.produk_terjual_count.text = statsData[adapterPosition].statTotalConversion
            }
            view.pendapatan_count.text = it.data.statTotalGrossProfit

            if (!view.check_box.isChecked) {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
            } else {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
            }
        }

        view.item_card.setOnClickListener {
            if (selectedMode) {
                view.check_box.isChecked = !view.check_box.isChecked
                item.isChecked = view.check_box.isChecked
                if (item.isChecked)
                    view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                else
                    view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
            }
        }

        view.img_menu.setOnClickListener {
            sheet?.show(((view.context as FragmentActivity).supportFragmentManager),item.data.adStatus, item.data.productName, item.data.groupId)
            sheet?.onEditAction = {
                editDone.invoke(item.data.adId, item.data.adPriceBid)
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

        view.item_card.setOnLongClickListener {
            item.isChecked = true
            view.check_box.isChecked = true
            view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
            selectMode.invoke(true)
            true
        }
    }

    private fun setProgressBar(data: WithoutGroupDataItem) {
        if (data.adPriceDailyBar.isNotEmpty()) {
            view.progress_layout.visibility = View.VISIBLE
            view.progress_bar.progressBarColorType = ProgressBarUnify.COLOR_GREEN
            try {
                view.progress_bar.setValue(Utils.convertMoneyToValue(data.adPriceDailySpentFmt), true)
            }catch(e:NumberFormatException){
                e.printStackTrace()
            }
            view.progress_status1.text = data.adPriceDailySpentFmt
            view.progress_status2.text = String.format(view.context.resources.getString(com.tokopedia.topads.common.R.string.topads_dash_group_item_progress_status), data.adPriceDaily)
        } else {
            view.progress_layout.visibility = View.GONE
        }
    }
}