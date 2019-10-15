package com.tokopedia.salam.umrah.orderdetail.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailButtonViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyButton.Type.ALTERNATE
import com.tokopedia.unifycomponents.UnifyButton.Type.MAIN
import com.tokopedia.unifycomponents.UnifyButton.Type.TRANSACTION
import com.tokopedia.unifycomponents.UnifyButton.Variant.FILLED
import com.tokopedia.unifycomponents.UnifyButton.Variant.GHOST

/**
 * @author by furqan on 15/10/2019
 */
class UmrahOrderDetailButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(data: UmrahOrderDetailButtonViewModel) {
        val view = itemView as UnifyButton

        view.text = data.label
        when {
            data.buttonType == BUTTON_PRIMARY -> {
                view.buttonVariant = FILLED
                view.buttonType = MAIN
            }
            data.buttonType == BUTTON_TRANSACTION -> {
                view.buttonVariant = FILLED
                view.buttonType = TRANSACTION
            }
            data.buttonType == BUTTON_SECONDARY -> {
                view.buttonVariant = GHOST
                view.buttonType = MAIN
            }
            data.buttonType == BUTTON_DEFAULT -> {
                view.buttonVariant = GHOST
                view.buttonType = ALTERNATE
            }
        }
    }

    companion object {
        const val BUTTON_PRIMARY = "primary"
        const val BUTTON_SECONDARY = "secondary"
        const val BUTTON_TRANSACTION = "transaction"
        const val BUTTON_DEFAULT = "default"
    }
}