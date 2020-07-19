package com.tokopedia.home.account.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.viewholder.MenuGridIconNotificationItemViewHolder
import com.tokopedia.home.account.presentation.viewmodel.MenuGridIconNotificationItemViewModel
import java.util.*

/**
 * Created by fwidjaja on 15/07/20.
 */
class MenuGridIconNotificationAdapter(listener: AccountItemListener) : RecyclerView.Adapter<MenuGridIconNotificationItemViewHolder?>() {
    private val categories: MutableList<MenuGridIconNotificationItemViewModel>
    private val listener: AccountItemListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuGridIconNotificationItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_grid_item_with_bg, parent, false)
        return MenuGridIconNotificationItemViewHolder(parent.context, view, listener)
    }

    override fun onBindViewHolder(holderItem: MenuGridIconNotificationItemViewHolder, position: Int) {
        holderItem.bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setNewData(items: List<MenuGridIconNotificationItemViewModel>?) {
        if (items != null) {
            categories.clear()
            categories.addAll(items)
            notifyDataSetChanged()
        }
    }

    init {
        categories = ArrayList()
        this.listener = listener
    }
}