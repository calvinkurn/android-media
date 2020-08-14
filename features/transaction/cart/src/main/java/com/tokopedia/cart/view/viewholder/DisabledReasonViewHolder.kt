package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledReasonHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class DisabledReasonViewHolder(view: View, val actionListener: ActionListener?) : RecyclerView.ViewHolder(view) {

    private val textDisabledTitle by lazy {
        view.findViewById<Typography>(R.id.text_disabled_title)
    }
    private val textDisabledSubTitle by lazy {
        view.findViewById<Typography>(R.id.text_disabled_sub_title)
    }

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_reason
    }

    fun bind(data: DisabledReasonHolderData) {
        textDisabledTitle.text = data.title
        if (data.subTitle.isNotBlank()) {
            textDisabledSubTitle.text = data.subTitle
            textDisabledSubTitle.show()
        } else {
            textDisabledSubTitle.gone()
        }
    }

}