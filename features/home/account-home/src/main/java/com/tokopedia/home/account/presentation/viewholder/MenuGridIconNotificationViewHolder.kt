package com.tokopedia.home.account.presentation.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.adapter.MenuGridIconNotificationAdapter
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.util.MenuGridSpacingDecoration
import com.tokopedia.home.account.presentation.viewmodel.MenuGridIconNotificationViewModel

/**
 * Created by fwidjaja on 15/07/20.
 */
class MenuGridIconNotificationViewHolder(itemView: View, private val listener: AccountItemListener) : AbstractViewHolder<MenuGridIconNotificationViewModel>(itemView) {
    private val textTitle: TextView = itemView.findViewById(R.id.text_title)
    private val textLink: TextView = itemView.findViewById(R.id.text_link)
    private val recyclerCategory: RecyclerView = itemView.findViewById(R.id.recycler_category)
    private val adapter: MenuGridIconNotificationAdapter = MenuGridIconNotificationAdapter(listener)

    override fun bind(element: MenuGridIconNotificationViewModel) {
        if (!TextUtils.isEmpty(element.title)) {
            textTitle.text = element.title
        }
        if (!TextUtils.isEmpty(element.linkText)) {
            textLink.text = element.linkText
            textLink.setOnClickListener { listener.onMenuGridBackgroundLinkClicked(element) }
        }
        adapter.setNewData(element.items)
    }

    companion object {
        @LayoutRes
        var LAYOUT: Int = R.layout.item_menu_grid_with_bg
    }

    init {
        recyclerCategory.adapter = adapter
        recyclerCategory.layoutManager = GridLayoutManager(itemView.context, 3, LinearLayoutManager.VERTICAL, false)
        recyclerCategory.addItemDecoration(MenuGridSpacingDecoration(3, 0, 2, false))
    }
}