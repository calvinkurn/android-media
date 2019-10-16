package com.tokopedia.salam.umrah.orderdetail.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailButtonViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyButton.Size.MEDIUM
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

        val linearLayoutParams = itemView.layoutParams as LinearLayout.LayoutParams
        linearLayoutParams.setMargins(0, 0, 0, 8)
        itemView.layoutParams = linearLayoutParams

        val button = UnifyButton(itemView.context)
        button.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        button.text = data.label
        button.buttonSize = MEDIUM
        when {
            data.buttonType == BUTTON_PRIMARY -> {
                button.buttonVariant = FILLED
                button.buttonType = MAIN
            }
            data.buttonType == BUTTON_TRANSACTION -> {
                button.buttonVariant = FILLED
                button.buttonType = TRANSACTION
            }
            data.buttonType == BUTTON_SECONDARY -> {
                button.buttonVariant = GHOST
                button.buttonType = MAIN
            }
            data.buttonType == BUTTON_DEFAULT -> {
                button.buttonVariant = GHOST
                button.buttonType = ALTERNATE
            }
        }

        (itemView as LinearLayout).addView(button)
    }

    companion object {
        const val BUTTON_PRIMARY = "primary"
        const val BUTTON_SECONDARY = "secondary"
        const val BUTTON_TRANSACTION = "transaction"
        const val BUTTON_DEFAULT = "default"
    }
}