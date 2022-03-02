package com.tokopedia.recharge_component.model.denom

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

object DenomConst {
    const val DENOM_STATUS_OUT_OF_STOCK = 3

    fun Typography.setStatusOutOfStockColor(status: Int, context: Context) {
        if (status == DENOM_STATUS_OUT_OF_STOCK) {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        }
    }

    fun Label.setStatusOutOfStockColor(outOfStockTitle: String) {
       setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
       text = outOfStockTitle
    }
}