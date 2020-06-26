package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_AKTIF
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsItemViewModel
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
                                  var editDone:((groupId:Int,adPriceBid:Int)->Unit)) : NonGroupItemsViewHolder<NonGroupItemsItemViewModel>(view) {
    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_non_group_card
    }

    override fun bind(item: NonGroupItemsItemViewModel, selectedMode: Boolean, fromSearch: Boolean) {
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
                view.card_view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
                view.img_menu.visibility = View.VISIBLE
                view.check_box.visibility = View.GONE
                view.label.visibility = View.VISIBLE
            }

            view.label.text = it.data.adStatusDesc
            ImageLoader.LoadImage(view.product_img, it.data.productImageUri)
            view.product_name.text = it.data.productName
            view.tampil_count.text = it.data.statTotalImpression
            view.klik_count.text = it.data.statTotalClick
            view.persentase_klik_count.text = it.data.statTotalCtr
            view.pengeluaran_count.text = it.data.statTotalSpent
            view.pendapatan_count.text = it.data.statTotalGrossProfit
            view.produk_terjual_count.text = it.data.statTotalSold
            setProgressBar(it.data)
            view.check_box.isChecked = item.isChecked

            if (!view.check_box.isChecked) {
                view.card_view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
            } else {
                view.card_view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
            }
        }

        view.item_card.setOnClickListener {
            if (selectedMode) {
                view.check_box.isChecked = !view.check_box.isChecked
                item.isChecked = view.check_box.isChecked
                if (item.isChecked)
                    view.card_view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                else
                    view.card_view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
            }
        }

        view.img_menu.setOnClickListener {
            val sheet = TopadsSelectActionSheet.newInstance(view.context, item.data.adStatus, item.data.productName)
            sheet.show()
            sheet.onEditAction = {
                editDone.invoke(item.data.adId,item.data.adPriceBid)
            }
            sheet.onDeleteClick = {
                actionDelete(adapterPosition)
            }
            sheet.changeStatus = {
                actionStatusChange(adapterPosition, it)
            }
        }

        view.item_card.setOnLongClickListener {
            item.isChecked = true
            view.check_box.isChecked = true
            view.card_view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
            selectMode.invoke(true)
            true
        }
    }

    private fun setProgressBar(data: WithoutGroupDataItem) {
        if (data.adPriceDailyBar.isNotEmpty()) {
            view.progress_layout.visibility = View.VISIBLE
            view.progress_bar.progressBarColorType = ProgressBarUnify.COLOR_GREEN
            view.progress_bar.setValue(data.adPriceDailySpentFmt.replace("Rp", "").trim().toInt(), true)
            view.progress_status1.text = data.adPriceDailySpentFmt
            view.progress_status2.text = String.format(view.context.resources.getString(R.string.topads_dash_group_item_progress_status), data.adPriceDaily)
        } else {
            view.progress_layout.visibility = View.GONE
        }
    }
}