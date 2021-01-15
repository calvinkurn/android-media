package com.tokopedia.homenav.category.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.diffutil.holder.HomeNavGlobalErrorViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavMenuViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavTickerViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavTitleViewHolder
import com.tokopedia.homenav.base.datamodel.HomeNavGlobalErrorDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.category.view.adapter.model.CategoryListLoadingDataModel
import com.tokopedia.homenav.category.view.adapter.viewholder.CategoryListLoadingViewHolder

/**
 * Created by Lukas on 20/10/20.
 */
class CategoryListTypeFactoryImpl(
        private val listener: HomeNavListener
) : CategoryListTypeFactory{

    override fun type(visitable: HomeNavMenuDataModel): Int {
        return HomeNavMenuViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavTitleDataModel): Int {
        return HomeNavTitleViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavGlobalErrorDataModel): Int {
        return HomeNavGlobalErrorViewHolder.LAYOUT
    }

    override fun type(visitable: CategoryListLoadingDataModel): Int {
        return CategoryListLoadingViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavTickerDataModel): Int {
        return HomeNavTickerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<Visitable<*>> {
        return when (viewType) {
            HomeNavMenuViewHolder.LAYOUT -> HomeNavMenuViewHolder(view, listener)
            HomeNavTitleViewHolder.LAYOUT -> HomeNavTitleViewHolder(view)
            HomeNavGlobalErrorViewHolder.LAYOUT -> HomeNavGlobalErrorViewHolder(view, listener)
            CategoryListLoadingViewHolder.LAYOUT -> CategoryListLoadingViewHolder(view)
            HomeNavTickerViewHolder.LAYOUT -> HomeNavTickerViewHolder(view, listener)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        } as AbstractViewHolder<Visitable<*>>
    }
}