package com.tokopedia.talk.producttalk.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.talk.ProductTalkTypeFactoryImpl
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.LoadProductTalkThreadViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkTitleViewModel

/**
 * @author by Steven
 */

class ProductTalkAdapter(adapterTypeFactory: ProductTalkTypeFactoryImpl,
                         listProductTalk: ArrayList<Visitable<*>>)
    : BaseAdapter<ProductTalkTypeFactoryImpl>(adapterTypeFactory, listProductTalk) {

    var emptyModel = EmptyInboxTalkViewModel()
    var loadModel = LoadProductTalkThreadViewModel()

    fun showEmpty() {
        this.visitables.clear()
        this.visitables.add(emptyModel)
        this.notifyDataSetChanged()
    }

    fun hideEmpty() {
        this.visitables.clear()
        this.notifyDataSetChanged()
    }

    fun setList(list: ArrayList<Visitable<*>>, productTalkTitleViewModel: ProductTalkTitleViewModel) {
        this.visitables.clear()
        this.visitables.add(productTalkTitleViewModel)
        this.visitables.addAll(list)
        notifyDataSetChanged()
    }

    fun addList(listThread: ArrayList<Visitable<*>>) {
        this.visitables.addAll(listThread)
        this.notifyItemRangeInserted(visitables.size, listThread.size)

    }

    fun setLoadModel() {
        this.visitables.add(loadModel)
        this.notifyItemRangeInserted(visitables.size, 1)
    }

    fun dismissLoadModel() {
        this.visitables.remove(loadModel)
        this.notifyItemRemoved(visitables.size)
    }
}


