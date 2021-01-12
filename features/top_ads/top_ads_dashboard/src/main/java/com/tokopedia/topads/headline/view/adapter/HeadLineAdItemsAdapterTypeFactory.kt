package com.tokopedia.topads.headline.view.adapter

import android.view.View
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineAdItemsViewHolder
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsEmptyModel
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsItemModel

/**
 * Created by Pika on 16/10/20.
 */

interface HeadLineAdItemsAdapterTypeFactory {

    fun type(model: HeadLineAdItemsEmptyModel): Int

    fun type(model: HeadLineAdItemsItemModel): Int

    fun holder(type: Int, view: View): HeadLineAdItemsViewHolder<*>

}