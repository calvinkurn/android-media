package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceViewHolder
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.util.HomeServerLogger.TYPE_ERROR_SUBMIT_WALLET

/**
 * Created by yfsx on 3/1/21.
 */
class BalanceAdapter(
    val listener: HomeCategoryListener?,
    diffUtil: DiffUtil.ItemCallback<BalanceDrawerItemModel>
) : ListAdapter<BalanceDrawerItemModel, BalanceViewHolder>(diffUtil) {

    var attachedRecyclerView: RecyclerView? = null
    private var itemList: HomeBalanceModel = HomeBalanceModel()

    @Suppress("TooGenericExceptionCaught")
    fun setItemList(itemList: HomeBalanceModel) {
        this.itemList = itemList

        val balanceModelList = mutableListOf<BalanceDrawerItemModel>()
        try {
            itemList.balanceDrawerItemModels.forEach {
                balanceModelList.add(it)
            }
            submitList(balanceModelList)
        } catch (e: Exception) {
            HomeServerLogger.logWarning(
                type = TYPE_ERROR_SUBMIT_WALLET,
                throwable = e,
                reason = e.message ?: ""
            )
            e.printStackTrace()
        }
    }

    fun getItemList(): HomeBalanceModel {
        return itemList
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.attachedRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        return BalanceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_balance_widget_new, parent, false), itemList.balanceDrawerItemModels.size)
    }

    override fun getItemCount(): Int {
        return itemList.balanceDrawerItemModels.size
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.bind(
            itemList.balanceDrawerItemModels[position],
            listener
        )
    }
}
