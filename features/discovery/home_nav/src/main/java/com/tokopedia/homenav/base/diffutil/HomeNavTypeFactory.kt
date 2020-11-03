package com.tokopedia.homenav.base.diffutil

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.viewmodel.HomeNavGlobalErrorViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTitleViewModel

/**
 * Created by Lukas on 20/10/20.
 */

interface HomeNavTypeFactory{
    fun type(visitable: HomeNavMenuViewModel) : Int
    fun type(visitable: HomeNavTitleViewModel) : Int
    fun type(visitable: HomeNavGlobalErrorViewModel) : Int
    fun type(visitable: HomeNavTickerViewModel) : Int
    fun createViewHolder(view: View, viewType: Int) : AbstractViewHolder<HomeNavVisitable>
}