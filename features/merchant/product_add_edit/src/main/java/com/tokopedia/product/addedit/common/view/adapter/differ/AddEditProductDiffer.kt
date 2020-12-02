package com.tokopedia.product.addedit.common.view.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

abstract class AddEditProductDiffer: DiffUtil.Callback() {

    abstract fun create(oldList: List<Visitable<*>>, newList: List<Visitable<*>>): AddEditProductDiffer
}