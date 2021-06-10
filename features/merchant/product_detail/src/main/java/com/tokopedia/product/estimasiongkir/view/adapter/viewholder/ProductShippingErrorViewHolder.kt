package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.product.detail.R
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingErrorDataModel
import com.tokopedia.product.estimasiongkir.view.bottomsheet.ProductDetailShippingListener
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 08/03/21
 */
class ProductShippingErrorViewHolder(view: View, private val listener: ProductDetailShippingListener) : AbstractViewHolder<ProductShippingErrorDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_dynamic_global_error
    }

    private val globalError: GlobalError = itemView.findViewById(R.id.global_error_pdp)

    override fun bind(element: ProductShippingErrorDataModel) {
        globalError.setPadding(0, 0, 0, 30.toPx())
        globalError.setType(element.errorType)
        globalError.setActionClickListener {
            listener.refreshPage(itemView.height)
        }
    }
}