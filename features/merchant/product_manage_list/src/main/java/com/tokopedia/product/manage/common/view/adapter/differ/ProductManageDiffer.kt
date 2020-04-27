package com.tokopedia.product.manage.common.view.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

abstract class ProductManageDiffer: DiffUtil.Callback() {

    abstract fun create(oldList: List<Visitable<*>>, newList: List<Visitable<*>>): ProductManageDiffer
}