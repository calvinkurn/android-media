package com.tokopedia.talk

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * @author by Steven
 */
interface ProductTalkChildThreadTypeFactory{

    fun type(loadMoreModel: LoadProductTalkViewModel):Int

    fun type(viewModel: ProductTalkItemViewModel):Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}