package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_AKTIF
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsItemModel
import com.tokopedia.topads.dashboard.view.sheet.TopadsSelectActionSheet
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by Pika on 2/6/20.
 */

private const val CLICK_IMG_MENU = "click - edit ads tanpa group"
private const val CLICK_NON_AKTIFKAN = "click - nonaktifkan single ads"
private const val CLICK_UHBAH = "click - ubah iklan single ads"
private const val CLICK_HAPUS = "click - hapus iklan single ads"

class NonGroupItemsItemViewHolder(
    val view: View,
    var selectMode: ((select: Boolean) -> Unit),
    var actionDelete: ((pos: Int) -> Unit),
    var actionStatusChange: ((pos: Int, status: Int) -> Unit),
    var editDone: ((groupId: String, adPriceBid: Int) -> Unit),
) : NonGroupItemsViewHolder<NonGroupItemsItemModel>(view) {

    private val productImg: ImageUnify? = view.findViewById(R.id.product_img)
    private val productName: Typography? = view.findViewById(R.id.product_name)
    private val imgMenu: UnifyImageButton? = view.findViewById(R.id.img_menu)
    private val checkBox: CheckboxUnify? = view.findViewById(R.id.check_box)
    private val label: Label? = view.findViewById(R.id.label)
    private val tampilCount: Typography? = view.findViewById(R.id.tampil_count)
    private val pengeluaranCount: Typography? = view.findViewById(R.id.pengeluaran_count)
    private val klikCount: Typography? = view.findViewById(R.id.klik_count)
    private val pendapatanCount: Typography? = view.findViewById(R.id.pendapatan_count)
    private val persentaseKlikCount: Typography? = view.findViewById(R.id.persentase_klik_count)
    private val produkTerjualCount: Typography? = view.findViewById(R.id.produk_terjual_count)
    private val progressLayout: ConstraintLayout? = view.findViewById(R.id.progress_layout)
    private val itemCard: ConstraintLayout? = view.findViewById(R.id.item_card)
    private val cardView: CardUnify? = view.findViewById(R.id.card_view)
    private val progressBar: ProgressBarUnify? = view.findViewById(R.id.progress_bar)
    private val progressStatus1: Typography? = view.findViewById(R.id.progress_status1)
    private val progressStatus2: Typography? = view.findViewById(R.id.progress_status2)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_non_group_card
    }

    private val sheet: TopadsSelectActionSheet? by lazy(LazyThreadSafetyMode.NONE) {
        TopadsSelectActionSheet.newInstance()
    }

    override fun bind(
        item: NonGroupItemsItemModel, selectedMode: Boolean,
        fromSearch: Boolean, statsData: MutableList<WithoutGroupDataItem>,
    ) {
        imgMenu?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_topads_menu))
        item.let {
            when (it.data.adStatusDesc) {

                ACTIVE -> {
                    label?.setLabelType(Label.GENERAL_LIGHT_GREEN)
                }
                TIDAK_AKTIF -> {
                    label?.setLabelType(Label.GENERAL_LIGHT_ORANGE)
                }
                TIDAK_TAMPIL -> {
                    label?.setLabelType(Label.GENERAL_LIGHT_GREY)
                }
            }
            if (selectedMode) {
                imgMenu?.visibility = View.GONE
                checkBox?.visibility = View.VISIBLE
                label?.visibility = View.INVISIBLE
            } else {
                cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                ))
                imgMenu?.visibility = View.VISIBLE
                checkBox?.visibility = View.GONE
                label?.visibility = View.VISIBLE
            }

            label?.text = it.data.adStatusDesc
            productImg?.setImageUrl(it.data.productImageUri)
            productName?.text = it.data.productName
            setProgressBar(it.data)
            checkBox?.isChecked = item.isChecked

            if (statsData.isNotEmpty() && adapterPosition < statsData.size && adapterPosition != RecyclerView.NO_POSITION) {
                tampilCount?.text = statsData[adapterPosition].statTotalImpression
                klikCount?.text = statsData[adapterPosition].statTotalClick
                persentaseKlikCount?.text = statsData[adapterPosition].statTotalCtr
                pengeluaranCount?.text = statsData[adapterPosition].statTotalSpent
                produkTerjualCount?.text = statsData[adapterPosition].statTotalConversion
            }
            pendapatanCount?.text = it.data.statTotalGrossProfit

            if (checkBox?.isChecked == false) {
                cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            } else {
                cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_BN400_20))
            }
        }

        itemCard?.setOnClickListener {
            if (selectedMode) {
                checkBox?.isChecked = checkBox?.isChecked == false
                item.isChecked = checkBox?.isChecked == true
                if (item.isChecked)
                    cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_BN400_20))
                else
                    cardView?.setBackgroundColor(ContextCompat.getColor(view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            }
        }

        imgMenu?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupEvent(CLICK_IMG_MENU, "")
            sheet?.show(((view.context as FragmentActivity).supportFragmentManager),
                item.data.adStatus,
                item.data.productName,
                item.data.groupId)
            sheet?.onEditAction = {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupEvent(CLICK_UHBAH, "")
                editDone.invoke(item.data.adId, item.data.adPriceBid)
            }
            sheet?.onDeleteClick = {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupEvent(CLICK_HAPUS, "")
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionDelete(adapterPosition)
            }
            sheet?.changeStatus = {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupEvent(
                    CLICK_NON_AKTIFKAN, "")
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionStatusChange(adapterPosition, it)
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

    private fun setProgressBar(data: WithoutGroupDataItem) {
        if (data.adPriceDailyBar.isNotEmpty()) {
            progressLayout?.visibility = View.VISIBLE
            progressBar?.progressBarColorType = ProgressBarUnify.COLOR_GREEN
            try {
                progressBar?.setValue(Utils.convertMoneyToValue(data.adPriceDailySpentFmt),
                    true)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            progressStatus1?.text = data.adPriceDailySpentFmt
            progressStatus2?.text =
                String.format(view.context.resources.getString(com.tokopedia.topads.common.R.string.topads_dash_group_item_progress_status),
                    data.adPriceDaily)
        } else {
            progressLayout?.visibility = View.GONE
        }
    }
}
