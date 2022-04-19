package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.unifycomponents.Toaster

abstract class BaseToasterViewHolder<T : Visitable<BuyerOrderDetailTypeFactory>>(itemView: View?) : AbstractViewHolder<T>(itemView) {
    protected fun showToaster(message: String) {
        itemView?.parent?.parent?.parent?.let {
            if (it is View) {
                Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        }
    }
}