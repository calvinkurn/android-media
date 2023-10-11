package com.tokopedia.topads.dashboard.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.STATUS_ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.STATUS_TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductItemModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 7/6/20.
 */


class ProductItemViewHolder(
    val view: View,
    var onSwitchAction: ((pos: Int, isChecked: Boolean) -> Unit),
    var onSelectMode: ((select: Boolean) -> Unit),
) : ProductViewHolder<ProductItemModel>(view) {

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
    private val itemCard: ConstraintLayout? = view.findViewById(R.id.item_card)
    private val cardView: CardUnify? = view.findViewById(R.id.card_view)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_non_group_card
    }

    override fun bind(
        item: ProductItemModel,
        selectMode: Boolean,
        statsData: MutableList<WithoutGroupDataItem>,
    ) {
        item.let {
            imgMenu?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_topads_menu))
            if (selectMode) {
                btnSwitch?.visibility = View.GONE
                checkBox?.visibility = View.VISIBLE
            } else {
                cardView?.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0
                    )
                )
                btnSwitch?.visibility = View.VISIBLE
                checkBox?.visibility = View.GONE
            }
            checkBox?.isChecked = item.isChecked
            btnSwitch?.setOnCheckedChangeListener(null)
            if (!item.isChanged)
                btnSwitch?.isChecked =
                    it.data.adStatus == STATUS_ACTIVE || it.data.adStatus == STATUS_TIDAK_TAMPIL
            else
                btnSwitch?.isChecked = item.valueChanged
            productImg?.setImageUrl(it.data.productImageUri)
            productName?.text = it.data.productName
            if (statsData.isNotEmpty() && adapterPosition < statsData.size) {
                tampilCount?.text = statsData[adapterPosition].statTotalImpression
                klikCount?.text = statsData[adapterPosition].statTotalClick
                persentaseKlikCount?.text = statsData[adapterPosition].statTotalCtr
                pengeluaranCount?.text = statsData[adapterPosition].statTotalSpent
                produkTerjualCount?.text = statsData[adapterPosition].statTotalConversion
            }
            pendapatanCount?.text = it.data.statTotalGrossProfit
            label?.visibility = View.INVISIBLE
            imgMenu?.visibility = View.INVISIBLE
            progressLayout?.visibility = View.GONE
            btnSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChanged = true
                item.valueChanged = isChecked
                if (adapterPosition != RecyclerView.NO_POSITION)
                    onSwitchAction.invoke(adapterPosition, isChecked)
            }
            if (checkBox?.isChecked == false) {
                cardView?.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0
                    )
                )
            } else {
                cardView?.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_BN400_20
                    )
                )
            }
            itemCard?.setOnClickListener {
                if (selectMode) {
                    checkBox?.isChecked = checkBox?.isChecked == false
                    item.isChecked = checkBox?.isChecked == true
                    if (checkBox?.isChecked == true)
                        cardView?.setBackgroundColor(
                            ContextCompat.getColor(
                                view.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_BN400_20
                            )
                        )
                    else
                        cardView?.setBackgroundColor(
                            ContextCompat.getColor(
                                view.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN0
                            )
                        )
                }
            }

            itemCard?.setOnLongClickListener {
                checkBox?.isChecked = true
                item.isChecked = true
                cardView?.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_BN400_20
                    )
                )
                onSelectMode.invoke(true)
                true
            }

        }
    }

}
