package com.tokopedia.homenav.base.diffutil

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.viewmodel.MainNavItemViewModel

/**
 * Created by Lukas on 20/10/20.
 */

interface HomeNavTypeFactory{

    fun type(mainNavItemViewModel: MainNavItemViewModel) : Int
    fun createViewHolder(view: View, viewType: Int) : AbstractViewHolder<HomeNavVisitable>
}