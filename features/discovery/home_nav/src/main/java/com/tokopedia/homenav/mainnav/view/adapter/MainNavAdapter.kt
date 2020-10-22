package com.tokopedia.homenav.mainnav.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavAdapter
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory

class MainNavAdapter(typeFactory: MainNavTypeFactory) : HomeNavAdapter<MainNavTypeFactory>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<HomeNavVisitable> {
        TODO("Not yet implemented")
    }
}
