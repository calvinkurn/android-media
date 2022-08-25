package com.tokopedia.tokofood.common.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

abstract class BaseTokoFoodDiffer: DiffUtil.Callback() {

    abstract fun create(oldList: List<Visitable<*>>, newList: List<Visitable<*>>): BaseTokoFoodDiffer
}