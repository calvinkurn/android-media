package com.tokopedia.catalogcommon.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

abstract class CatalogDiffer: DiffUtil.Callback() {

    abstract fun create(oldList: List<Visitable<*>>, newList: List<Visitable<*>>): CatalogDiffer
}
