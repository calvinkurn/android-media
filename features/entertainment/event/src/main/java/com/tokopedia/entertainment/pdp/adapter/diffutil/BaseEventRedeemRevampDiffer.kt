package com.tokopedia.entertainment.pdp.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Author firmanda on 17,Nov,2022
 */

abstract class BaseEventRedeemRevampDiffer: DiffUtil.Callback() {
    abstract fun create(oldList: List<Visitable<*>>, newList: List<Visitable<*>>): BaseEventRedeemRevampDiffer
}
