package com.tokopedia.homenav.base.diffutil

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by Lukas on 20/10/20.
 */

interface HomeNavTypeFactory{
    fun createViewHolder(view: View, viewType: Int) : AbstractViewHolder<HomeNavVisitable>
}