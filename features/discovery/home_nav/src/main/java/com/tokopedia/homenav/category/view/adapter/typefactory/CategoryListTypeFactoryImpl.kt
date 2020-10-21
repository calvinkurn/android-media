package com.tokopedia.homenav.category.view.adapter.typefactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.diffutil.holder.MainNavItemViewHolder
import com.tokopedia.homenav.base.viewmodel.MainNavItemViewModel
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.AccountHeaderViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.SeparatorViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.TransactionListViewHolder

/**
 * Created by Lukas on 20/10/20.
 */
class CategoryListTypeFactoryImpl() : HomeNavTypeFactory, CategoryListTypeFactory{


    override fun type(mainNavItemViewModel: MainNavItemViewModel): Int {
        return MainNavItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<HomeNavVisitable> {
        return when (viewType) {
            MainNavItemViewHolder.LAYOUT -> MainNavItemViewHolder(view)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        } as AbstractViewHolder<HomeNavVisitable>
    }
}