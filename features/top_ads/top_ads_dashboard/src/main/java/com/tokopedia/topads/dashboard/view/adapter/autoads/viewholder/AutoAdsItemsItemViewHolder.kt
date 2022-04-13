package com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsItemModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify

/**
 * Created by Pika on 2/6/20.
 */

class AutoAdsItemsItemViewHolder(val view: View) :
    AutoAdsItemsViewHolder<AutoAdsItemsItemModel>(view) {

    private val productImg: ImageUnify? = view.findViewById(R.id.product_img)
    private val productName: Typography? = view.findViewById(R.id.product_name)
    private val imgMenu: UnifyImageButton? = view.findViewById(R.id.img_menu)
    private val checkBox: CheckboxUnify? = view.findViewById(R.id.check_box)
    private val btnSwitch: SwitchUnify? = view.findViewById(R.id.btn_switch)
    private val label: Label? = view.findViewById(R.id.label)
    private val tampilCount: Typography? = view.findViewById(R.id.tampil_count)
    private val pengeluaranCount: Typography? = view.findViewById(R.id.pengeluaran_count)
    private val klikCount: Typography? = view.findViewById(R.id.klik_count)
    private val pendapatanCount: Typography? = view.findViewById(R.id.pendapatan_count)
    private val persentaseKlikCount: Typography? = view.findViewById(R.id.persentase_klik_count)
    private val produkTerjualCount: Typography? = view.findViewById(R.id.produk_terjual_count)
    private val progressLayout: ConstraintLayout? = view.findViewById(R.id.progress_layout)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_non_group_card
    }

    override fun bind(item: AutoAdsItemsItemModel, statsData: MutableList<WithoutGroupDataItem>) {
        item.let {
            productImg?.setImageUrl(it.data.productImageUri)
            imgMenu?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_topads_menu))
            productName?.text = it.data.productName
            imgMenu?.visibility = View.GONE
            checkBox?.visibility = View.GONE
            label?.visibility = View.INVISIBLE
            btnSwitch?.visibility = View.GONE
            progressLayout?.visibility = View.GONE
            productName?.text = it.data.productName

            if (statsData.isNotEmpty() && adapterPosition < statsData.size) {
                tampilCount?.text = statsData[adapterPosition].statTotalImpression
                klikCount?.text = statsData[adapterPosition].statTotalClick
                persentaseKlikCount?.text = statsData[adapterPosition].statTotalCtr
                pengeluaranCount?.text = statsData[adapterPosition].statTotalSpent
                pendapatanCount?.text = statsData[adapterPosition].statTotalConversion
                produkTerjualCount?.text = statsData[adapterPosition].statTotalSold
            }

        }
    }
}