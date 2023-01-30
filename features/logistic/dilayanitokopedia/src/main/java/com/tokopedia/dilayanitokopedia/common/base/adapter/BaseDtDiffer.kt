package com.tokopedia.dilayanitokopedia.common.base.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by irpan on 12/09/22.
 */
abstract class BaseDtDiffer : DiffUtil.Callback() {

    abstract fun create(oldList: List<Visitable<*>>, newList: List<Visitable<*>>): BaseDtDiffer
}
