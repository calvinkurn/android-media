package com.tokopedia.officialstore.base.diffutil

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by Lukas on 27/10/20.
 */
interface OfficialTypeFactory{
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>>
}