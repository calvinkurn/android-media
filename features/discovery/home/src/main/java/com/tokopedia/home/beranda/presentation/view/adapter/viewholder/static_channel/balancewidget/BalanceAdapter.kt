package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.BalanceWidgetListener
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
): ListAdapter<BalanceDrawerItemModel, BalanceViewHolder>(diffUtil) {

    var attachedRecyclerView: RecyclerView? = null
    private var itemMap: HomeBalanceModel = HomeBalanceModel()

    companion object {
        var disableAnimation: Boolean = false
    }

    @Suppress("TooGenericExceptionCaught")
    fun setItemMap(itemMap: HomeBalanceModel) {
        this.itemMap = itemMap

        val balanceModelList = mutableListOf<BalanceDrawerItemModel>()
        try {
            itemMap.balanceDrawerItemModels.forEach {
                balanceModelList.add(it)
            }
            submitList(balanceModelList.toMutableList())
        } catch (e: Exception) {
            HomeServerLogger.logWarning(
                type = TYPE_ERROR_SUBMIT_WALLET,
                throwable = e,
                reason = e.message?:""
            )
            e.printStackTrace()
        }
    }

    fun getItemMap():  HomeBalanceModel {
        return itemMap
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.attachedRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        return BalanceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_balance_widget_new, parent, false))
    }

    override fun getItemCount(): Int {
        return itemMap.balanceDrawerItemModels.size
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.bind(
            itemMap.balanceDrawerItemModels[position],
            listener
        )
    }

    fun getGopayView() : View? {
        val viewHolder = attachedRecyclerView?.findViewHolderForAdapterPosition(0) as BalanceViewHolder
        return viewHolder.gopayViewText
    }
}