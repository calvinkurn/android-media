package com.tokopedia.homenav.category.view.adapter.typefactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.viewmodel.MainNavItemViewModel

/**
 * Created by Lukas on 20/10/20.
 */
class CategoryListTypeFactoryImpl : CategoryListTypeFactory{


    override fun type(mainNavItemViewModel: MainNavItemViewModel): Int {
        TODO("Not yet implemented")
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<HomeNavVisitable> {
        TODO("Not yet implemented")
    }
}