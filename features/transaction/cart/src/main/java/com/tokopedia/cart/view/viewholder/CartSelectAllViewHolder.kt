package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartSelectAllHolderData
import com.tokopedia.purchase_platform.common.utils.rxCompoundButtonCheckDebounce
import kotlinx.android.synthetic.main.item_select_all.view.*
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

class CartSelectAllViewHolder(val view: View, val listener: ActionListener?, val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_select_all
    }

    fun bind(data: CartSelectAllHolderData) {
        itemView.checkbox_global?.let {
            compositeSubscription.add(
                    rxCompoundButtonCheckDebounce(it).subscribe(object : Subscriber<Boolean>() {
                        override fun onNext(isChecked: Boolean) {
                            if (data.isCheckUncheckDirectAction) {
                                listener?.onGlobalCheckboxCheckedChange(isChecked, data.isCheckUncheckDirectAction)
                            }
                            data.isCheckUncheckDirectAction = true
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable?) {
                        }
                    })
            )
        }

        itemView.checkbox_global.isChecked = data.isCheked
    }

}