package com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertarget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget.VoucherTipsItemAdapterTypeFactory
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget.VoucherTipsItemTypeFactory
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.TipsItemUiModel
import kotlinx.android.synthetic.main.mvc_voucher_tips_item.view.*

class VoucherTipsAdapter(val itemList: List<TipsItemUiModel>,
                         private val onChevronIconAltered: (Int) -> Unit = {},
                         private val onItemClicked: (Int) -> Unit) : RecyclerView.Adapter<VoucherTipsAdapter.VoucherTipsViewHolder>() {

    class VoucherTipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val CHEVRON_CLOSED_ROTATION = 0f
        private const val CHEVRON_OPENED_ROTATION = 180f
    }

    private val voucherTipsItemAdapterTypeFactory by lazy {
        VoucherTipsItemAdapterTypeFactory()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherTipsViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.mvc_voucher_tips_item, parent, false)
        return VoucherTipsViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VoucherTipsViewHolder, position: Int) {
        itemList[position].let { tipsItemUiModel ->
            holder.itemView.run {
                voucherTipsTitle?.text = resources?.getString(tipsItemUiModel.titleRes).toBlankOrString()
                setAccordionState(tipsItemUiModel.isOpen)
                voucherTipsItemRecyclerView?.setAdapterItem(tipsItemUiModel.tipsItemList)
                voucherTipsView?.setOnClickListener {
                    onItemClicked(tipsItemUiModel.titleRes)
                    onChevronIconAltered(position)
                }
            }
        }
    }

    private fun View.setAccordionState(isItemOpen: Boolean): Boolean {
        if (isItemOpen) {
            voucherTipsChevron?.rotation = CHEVRON_OPENED_ROTATION
            voucherTipsItemRecyclerView?.run {
                visibility = View.VISIBLE
                return true
            }
        } else {
            voucherTipsChevron?.rotation = CHEVRON_CLOSED_ROTATION
            voucherTipsItemRecyclerView?.visibility = View.GONE
        }
        return false
    }

    private fun RecyclerView.setAdapterItem(tipsItemList: List<Visitable<VoucherTipsItemTypeFactory>>) {
        if (adapter == null) {
            val voucherTipsItemAdapter = VoucherTipsItemAdapter(voucherTipsItemAdapterTypeFactory)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = voucherTipsItemAdapter
        }

        (adapter as? VoucherTipsItemAdapter)?.setVisitables(tipsItemList)
    }

}