package com.tokopedia.topads.headline.view.adapter.aditem

import android.view.View
import com.tokopedia.topads.headline.view.adapter.aditem.viewmodel.HeadLineAdItemsEmptyViewModel
import com.tokopedia.topads.headline.view.adapter.aditem.viewholder.HeadLineAdItemsViewHolder
import com.tokopedia.topads.headline.view.adapter.aditem.viewmodel.HeadLineAdItemsItemViewModel

/**
 * Created by Pika on 16/10/20.
 */

interface HeadLineAdItemsAdapterTypeFactory {

    fun type(model: HeadLineAdItemsEmptyViewModel): Int

    fun type(model: HeadLineAdItemsItemViewModel): Int

    fun holder(type: Int, view: View): HeadLineAdItemsViewHolder<*>

}