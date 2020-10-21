package com.tokopedia.homenav.base.diffutil

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.viewmodel.CommonNavItemViewModel

/**
 * Created by Lukas on 20/10/20.
 */

interface HomeNavTypeFactory{

    fun type(commonNavItemViewModel: CommonNavItemViewModel) : Int
    fun createViewHolder(view: View, viewType: Int) : AbstractViewHolder<HomeNavVisitable>
}