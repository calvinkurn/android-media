package com.tokopedia.homenav.category.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.diffutil.holder.CommonNavItemViewHolder
import com.tokopedia.homenav.base.viewmodel.CommonNavItemViewModel

/**
 * Created by Lukas on 20/10/20.
 */
class CategoryListTypeFactoryImpl() : HomeNavTypeFactory, CategoryListTypeFactory{


    override fun type(commonNavItemViewModel: CommonNavItemViewModel): Int {
        return CommonNavItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<HomeNavVisitable> {
        return when (viewType) {
            CommonNavItemViewHolder.LAYOUT -> CommonNavItemViewHolder(view)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        } as AbstractViewHolder<HomeNavVisitable>
    }
}