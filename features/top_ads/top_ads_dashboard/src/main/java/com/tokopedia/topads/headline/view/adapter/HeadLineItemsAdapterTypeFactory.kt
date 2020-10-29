package com.tokopedia.topads.headline.view.adapter

import android.view.View
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineItemsViewHolder
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineItemsEmptyViewModel
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineItemsItemViewModel

/**
 * Created by Pika on 16/10/20.
 */

interface HeadLineItemsAdapterTypeFactory {

    fun type(model: HeadLineItemsEmptyViewModel): Int

    fun type(model: HeadLineItemsItemViewModel): Int

    fun holder(type: Int, view: View): HeadLineItemsViewHolder<*>

}