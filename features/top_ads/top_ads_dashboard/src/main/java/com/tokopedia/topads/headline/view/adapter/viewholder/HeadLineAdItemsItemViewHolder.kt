package com.tokopedia.topads.headline.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.NOT_VALID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_AKTIF
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_DIBATASI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.view.sheet.TopadsSelectActionSheet
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsItemModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 16/10/20.
 */

class HeadLineAdItemsItemViewHolder(
    val view: View, var selectMode: ((select: Boolean) -> Unit),
    var actionDelete: ((pos: Int) -> Unit),
    var actionStatusChange: ((pos: Int, status: Int) -> Unit),
    private var editDone: ((groupId: String) -> Unit),
    private var onClickItem: ((id: String, priceSpent: String) -> Unit),
) : HeadLineAdItemsViewHolder<HeadLineAdItemsItemModel>(view) {

    private val cardView: CardUnify? = view.findViewById(R.id.card_view)
    private val itemCard: ConstraintLayout? = view.findViewById(R.id.item_card)
    private val img: ImageUnify? = view.findViewById(R.id.img)
    private val groupTitle: Typography? = view.findViewById(R.id.group_title)
    private val imgMenu: UnifyImageButton? = view.findViewById(R.id.img_menu)
    private val checkBox: CheckboxUnify? = view.findViewById(R.id.check_box)
    private val label: Label? = view.findViewById(R.id.label)
    private val imgTotal: ImageUnify? = view.findViewById(R.id.img_total)
    private val totalItem: Typography? = view.findViewById(R.id.total_item)
    private val imgKey: ImageUnify? = view.findViewById(R.id.img_key)
    private val keyCount: Typography? = view.findViewById(R.id.key_count)
    private val scheduleImg: ImageUnify? = view.findViewById(R.id.scheduleImg)
    private val scheduleDate: Typography? = view.findViewById(R.id.scheduleDate)
    private val tampilCount: Typography? = view.findViewById(R.id.tampil_count)
    private val pengeluaranCount: Typography? = view.findViewById(R.id.pengeluaran_count)
    private val klikCount: Typography? = view.findViewById(R.id.klik_count)
    private val pendapatanCount: Typography? = view.findViewById(R.id.pendapatan_count)
    private val persentaseKlikCount: Typography? = view.findViewById(R.id.persentase_klik_count)
    private val produkTerjualCount: Typography? = view.findViewById(R.id.produk_terjual_count)
    private val progressLayout: ConstraintLayout? = view.findViewById(R.id.progress_layout)
    private val progressStatus1: Typography? = view.findViewById(R.id.progress_status1)
    private val progressStatus2: Typography? = view.findViewById(R.id.progress_status2)
    private val progressBar: ProgressBarUnify? = view.findViewById(R.id.progress_bar)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_with_group_card
    }

    private val sheet: TopadsSelectActionSheet? by lazy {
        TopadsSelectActionSheet.newInstance()
    }

    override fun bind(
        item: HeadLineAdItemsItemModel, selectedMode: Boolean, fromSearch: Boolean,
        statsData: MutableList<DataItem>, countList: MutableList<CountDataItem>,
        selectedText: String,
    ) {
        item.let {

            img?.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_folder))
            imgTotal?.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_total))
            imgKey?.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dashboard_key))
            scheduleImg?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
            imgMenu?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_topads_menu))
            if (selectedMode) {
                imgMenu?.visibility = View.INVISIBLE
                checkBox?.visibility = View.VISIBLE
            } else {
                cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                ))
                imgMenu?.visibility = View.VISIBLE
                checkBox?.visibility = View.GONE
                checkBox?.isChecked = false
                it.isChecked = false
            }
            checkBox?.isChecked = it.isChecked
            if (checkBox?.isChecked == false) {
                cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            } else {
                cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_BN400_20))
            }
            when (it.data.groupStatusDesc) {
                ACTIVE -> label?.setLabelType(Label.GENERAL_DARK_GREEN)
                TIDAK_AKTIF -> label?.setLabelType(Label.GENERAL_LIGHT_GREY)
                TIDAK_TAMPIL -> label?.setLabelType(Label.GENERAL_LIGHT_GREY)
            }
            groupTitle?.text = it.data.groupName
            label?.text = it.data.groupStatusDesc
            if (countList.isNotEmpty() && adapterPosition < countList.size && adapterPosition != RecyclerView.NO_POSITION) {
                totalItem?.text = countList[adapterPosition].totalProducts.toString()
                keyCount?.text = countList[adapterPosition].totalKeywords.toString()
            }
            setProgressBar(it.data)
            statsData.forEachIndexed { index, stats ->
                if (stats.groupId == it.data.groupId) {
                    tampilCount?.text = statsData[index].statTotalImpression
                    klikCount?.text = statsData[index].statTotalClick
                    persentaseKlikCount?.text = statsData[index].statTotalCtr
                    pengeluaranCount?.text = statsData[index].statTotalSpent
                    produkTerjualCount?.text = statsData[index].statTotalConversion
                    pendapatanCount?.text = statsData[index].groupTotalIncome
                    if (it.data.groupEndDate != TIDAK_DIBATASI) {
                        scheduleImg?.visibility = View.VISIBLE
                        scheduleDate?.visibility = View.VISIBLE
                        scheduleDate?.text =
                            String.format(view.context.getString(R.string.topads_headline_group_schedule),
                                it.data.groupEndDate)
                    } else {
                        scheduleImg?.visibility = View.GONE
                        scheduleDate?.visibility = View.GONE
                    }
                }
            }
            itemCard?.setOnClickListener { _ ->
                if (!selectedMode) {
                    if (item.data.groupPriceDailyBar.isNotEmpty())
                        onClickItem.invoke(item.data.groupId, item.data.groupPriceDailySpentFmt)
                    else
                        onClickItem.invoke(item.data.groupId, NOT_VALID)
                } else {
                    checkBox?.isChecked = (checkBox?.isChecked == false)
                    it.isChecked = checkBox?.isChecked == true
                    if (checkBox?.isChecked == true)
                        cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_BN400_20))
                    else
                        cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                }
            }
            itemCard?.setOnLongClickListener {
                item.isChecked = true
                checkBox?.isChecked = true
                cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_BN400_20))
                selectMode.invoke(true)
                true
            }
        }

        imgMenu?.setOnClickListener {
            sheet?.onEditAction = {
                editDone.invoke(item.data.groupId)
            }
            sheet?.onDeleteClick = {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionDelete(adapterPosition)
            }
            sheet?.changeStatus = {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionStatusChange(adapterPosition, it)
            }
            sheet?.show(((view.context as FragmentActivity).supportFragmentManager),
                item.data.groupStatus,
                item.data.groupName,
                item.data.groupId,
                true)
        }
    }

    private fun setProgressBar(data: DataItem) {
        if (data.groupPriceDailyBar.isNotEmpty()) {
            progressLayout?.visibility = View.VISIBLE
            progressBar?.progressBarColorType = ProgressBarUnify.COLOR_GREEN
            try {
                progressBar?.setValue(Utils.convertMoneyToValue(data.groupPriceDailySpentFmt),
                    true)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            progressStatus1?.text = data.groupPriceDailySpentFmt
            progressStatus2?.text =
                String.format(view.context.resources.getString(com.tokopedia.topads.common.R.string.topads_dash_group_item_progress_status),
                    data.groupPriceDaily)
        } else {
            progressLayout?.visibility = View.GONE
        }
    }

}
