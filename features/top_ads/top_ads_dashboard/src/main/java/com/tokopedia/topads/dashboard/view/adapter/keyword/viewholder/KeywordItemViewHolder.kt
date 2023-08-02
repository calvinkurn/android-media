package com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.STATUS_ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.STATUS_TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 7/6/20.
 */

class KeywordItemViewHolder(
    val view: View,
    private var onSwitchAction: (pos: Int, isChecked: Boolean) -> Unit,
    private var onSelectMode: (select: Boolean) -> Unit,
    var headline: Boolean = false,
) : KeywordViewHolder<KeywordItemModel>(view) {

    private val cardView: CardUnify = view.findViewById(R.id.card_view)
    private val itemCard: ConstraintLayout = view.findViewById(R.id.item_card)
    private val keyTitle: Typography = view.findViewById(R.id.key_title)
    private val btnSwitch: SwitchUnify = view.findViewById(R.id.btn_switch)
    private val checkBox: CheckboxUnify = view.findViewById(R.id.check_box)
    private val label: Label = view.findViewById(R.id.label)
    private val imgTotal: ImageUnify = view.findViewById(R.id.img_total)
    private val priceBid: Typography = view.findViewById(R.id.price_bid)
    private val perClick: Typography = view.findViewById(R.id.per_click)
    private val tampilCount: Typography = view.findViewById(R.id.tampil_count)
    private val pengeluaranCount: Typography = view.findViewById(R.id.pengeluaran_count)
    private val klikCount: Typography = view.findViewById(R.id.klik_count)
    private val pendapatanCount: Typography = view.findViewById(R.id.pendapatan_count)
    private val persentaseKlikCount: Typography = view.findViewById(R.id.persentase_klik_count)
    private val produkTerjualCount: Typography = view.findViewById(R.id.produk_terjual_count)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_keyword_card
    }

    override fun bind(item: KeywordItemModel, selectMode: Boolean, fromSearch: Boolean) {
        item.let {
            if (headline)
                perClick.text =
                    view.context.getString(com.tokopedia.topads.common.R.string.topads_common_headline_klik)
            if (selectMode) {
                btnSwitch.visibility = View.INVISIBLE
                checkBox.visibility = View.VISIBLE
            } else {
                cardView?.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0
                    )
                )
                btnSwitch.visibility = View.VISIBLE
                checkBox.visibility = View.GONE
            }
            imgTotal.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dash_rupee))
            checkBox.isChecked = item.isChecked
            keyTitle.text = it.result.keywordTag
            btnSwitch.setOnCheckedChangeListener(null)
            if (!item.isChanged)
                btnSwitch.isChecked =
                    it.result.keywordStatus == STATUS_ACTIVE || it.result.keywordStatus == STATUS_TIDAK_TAMPIL
            else
                btnSwitch.isChecked = item.changedValue
            label.setLabelType(Label.GENERAL_LIGHT_GREEN)
            label.text = it.result.keywordTypeDesc
            tampilCount.text = it.result.statTotalImpression
            klikCount.text = it.result.statTotalClick
            persentaseKlikCount.text = it.result.statTotalCtr
            pengeluaranCount.text = it.result.statTotalSpent
            pendapatanCount.text = it.result.statTotalConversion
            produkTerjualCount.text = it.result.statTotalConversion
            priceBid.text = it.result.keywordPriceBidFmt
            if (!checkBox.isChecked) {
                cardView.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0
                    )
                )
            } else {
                cardView.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_BN400_20
                    )
                )
            }
            btnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                item.changedValue = isChecked
                item.isChanged = true
                if (adapterPosition != RecyclerView.NO_POSITION)
                    onSwitchAction.invoke(adapterPosition, isChecked)
            }

            itemCard.setOnClickListener {
                if (selectMode) {
                    checkBox.isChecked = !checkBox.isChecked
                    item.isChecked = checkBox.isChecked
                    if (checkBox.isChecked)
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

            itemCard.setOnLongClickListener {
                item.isChecked = true
                checkBox.isChecked = true
                cardView.setBackgroundColor(
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
