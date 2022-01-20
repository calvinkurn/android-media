package com.tokopedia.attachproduct.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder.CheckableInteractionListener

interface CheckableInteractionListenerWithPreCheckedAction : CheckableInteractionListener {

    fun shouldAllowCheckChange(position: Int, checked: Boolean): Boolean

}
