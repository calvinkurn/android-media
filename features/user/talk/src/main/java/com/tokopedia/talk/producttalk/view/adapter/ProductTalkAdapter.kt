package com.tokopedia.talk.producttalk.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.talk.ProductTalkTypeFactoryImpl

/**
 * @author by Steven
 */

class ProductTalkAdapter(adapterTypeFactory: ProductTalkTypeFactoryImpl,
                         listProductTalk: ArrayList<Visitable<*>>)
    : BaseAdapter<ProductTalkTypeFactoryImpl>(adapterTypeFactory, listProductTalk) {

    val emptyModel: EmptyModel = EmptyModel()


    fun setList(list: ArrayList<Visitable<ProductTalkThreadTypeFactory>>) {
        this.visitables.clear()
        this.visitables.addAll(list)
        notifyDataSetChanged()
    }
}


