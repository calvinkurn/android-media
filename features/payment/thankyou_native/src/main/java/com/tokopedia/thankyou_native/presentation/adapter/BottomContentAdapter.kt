package com.tokopedia.thankyou_native.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory

class BottomContentAdapter(private val visitableList: ArrayList<Visitable<*>>,
                           typeFactory: BottomContentFactory
) : BaseAdapter<BottomContentFactory>(typeFactory, visitableList) {

    fun setItems(data: List<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }
}
