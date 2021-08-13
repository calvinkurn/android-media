package com.tokopedia.pdpsimulation.creditcard.presentation.tnc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardPdpInfoContent
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder.CreditCardPdpInfoButtonViewHolder
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder.CreditCardPdpInfoViewHolder
import kotlinx.android.synthetic.main.credit_card_pdp_meta_info_item.view.*

class CreditCardTncAdapter(val moreInfoListener: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var pdpInfoContentList: ArrayList<CreditCardPdpInfoContent> = arrayListOf()
    private var ctaRedirectionLabel: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_BUTTON -> {
                val viewHolder = CreditCardPdpInfoButtonViewHolder.getViewHolder(inflater, parent)
                viewHolder.view.setOnClickListener {
                    moreInfoListener()
                }
                return viewHolder
            }
            else -> CreditCardPdpInfoViewHolder.getViewHolder(inflater, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CreditCardPdpInfoButtonViewHolder -> holder.bindData(ctaRedirectionLabel)
            is CreditCardPdpInfoViewHolder -> {
                holder.bindData(pdpInfoContentList[position])
                if (position != RecyclerView.NO_POSITION) {
                    holder.view.rvPdpInfoContent.apply {
                        val model = pdpInfoContentList[holder.adapterPosition]
                        when (model.viewType) {
                            VIEW_TYPE_BULLET -> {
                                if (adapter != null && adapter is CreditCardPdpBulletInfoAdapter && !model.bulletList.isNullOrEmpty()) {
                                    (adapter as CreditCardPdpBulletInfoAdapter).notesList = model.bulletList
                                            ?: arrayListOf()
                                    (adapter as CreditCardPdpBulletInfoAdapter).notifyDataSetChanged()
                                } else
                                    adapter = CreditCardPdpBulletInfoAdapter(model.bulletList
                                            ?: arrayListOf())
                            }
                            VIEW_TYPE_TABLE_MIN_TRX, VIEW_TYPE_TABLE_SERVICE -> {
                                if (adapter != null && adapter is CreditCardPdpTableInfoAdapter && !model.tableData?.tableList.isNullOrEmpty()) {
                                    (adapter as CreditCardPdpTableInfoAdapter).tableList = model.tableData?.tableList
                                            ?: arrayListOf()
                                    (adapter as CreditCardPdpTableInfoAdapter).notifyDataSetChanged()
                                } else
                                    adapter = CreditCardPdpTableInfoAdapter(model.tableData?.tableList
                                            ?: arrayListOf(), model.viewType)
                            }
                        }
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                    if (position == pdpInfoContentList.size - 1) holder.view.dividerInstallment.gone()
                    else holder.view.dividerInstallment.visible()
                }
            }
        }
    }

    fun setData(pdpInfoContentList: ArrayList<CreditCardPdpInfoContent>, ctaRedirectionLabel: String?) {
        this.pdpInfoContentList = pdpInfoContentList
        this.ctaRedirectionLabel = ctaRedirectionLabel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return pdpInfoContentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == pdpInfoContentList.size) VIEW_TYPE_BUTTON
        else VIEW_TYPE_TNC
    }

    companion object {
        const val VIEW_TYPE_BULLET = 1
        const val VIEW_TYPE_TABLE_MIN_TRX = 2
        const val VIEW_TYPE_TABLE_SERVICE = 3
        const val VIEW_TYPE_BUTTON = 4
        const val VIEW_TYPE_TNC = 5

    }
}