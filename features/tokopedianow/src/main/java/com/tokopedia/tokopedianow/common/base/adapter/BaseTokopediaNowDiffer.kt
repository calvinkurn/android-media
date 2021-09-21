package com.tokopedia.tokopedianow.common.base.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

abstract class BaseTokopediaNowDiffer: DiffUtil.Callback() {

    abstract fun create(oldList: List<Visitable<*>>, newList: List<Visitable<*>>): BaseTokopediaNowDiffer
}