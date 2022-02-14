package com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.common.listener.SingleTapListener
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel

@SuppressLint("ClickableViewAccessibility")
abstract class BaseOrderExtensionRequestInfoViewHolder<T : OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>(
    itemView: View?
) : AbstractViewHolder<T>(itemView) {

    init {
        setupSingleTapListener()
    }

    protected var element: T? = null

    @CallSuper
    override fun bind(element: T?) {
        this.element = element
    }

    @CallSuper
    override fun bind(element: T?, payloads: MutableList<Any>) {
        this.element = element
    }

    private fun setupSingleTapListener() {
        return itemView?.let {
            SingleTapListener(it.context, ::onTap).attachListener(it)
        }
    }

    private fun tryHideKeyboard() {
        if (element?.hideKeyboardOnClick == true) {
            itemView?.hideKeyboard()
        }
    }

    protected open fun onTap(event: MotionEvent?): Boolean {
        tryHideKeyboard()
        return false
    }

    open fun onViewAttachedFromWindow() {}
    open fun onViewDetachedFromWindow() {}
}