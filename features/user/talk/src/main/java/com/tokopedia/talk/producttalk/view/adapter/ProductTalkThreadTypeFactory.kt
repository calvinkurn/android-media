package com.tokopedia.talk

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * @author by Steven
 */
interface ProductTalkThreadTypeFactory{


    fun type(viewModel: ProductTalkViewModel): Int

    fun type(viewModel: TalkThreadViewModel):Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}