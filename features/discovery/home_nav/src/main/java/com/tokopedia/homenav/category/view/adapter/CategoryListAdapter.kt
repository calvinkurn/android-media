package com.tokopedia.homenav.category.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavAdapter
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.category.view.adapter.typefactory.CategoryListTypeFactory

/**
 * Created by Lukas on 20/10/20.
 */
class CategoryListAdapter (private val adapterTypeFactory: CategoryListTypeFactory): HomeNavAdapter<CategoryListTypeFactory>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<HomeNavVisitable> {
        return adapterTypeFactory.createViewHolder(onCreateViewItem(parent, viewType), viewType) as AbstractViewHolder<HomeNavVisitable>
    }

    override fun getItemViewType(position: Int): Int = currentList.getOrNull(position)?.type(adapterTypeFactory) ?: -1
}