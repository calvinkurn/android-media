package com.tkpd.atcvariant.view.viewholder

import android.view.View
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.VariantErrorDataModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.toPx

class AtcVariantErrorViewHolder(
    view: View,
    private val listener: AtcVariantListener
) :
    AbstractViewHolder<VariantErrorDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_error_viewholder
    }

    private val globalError: GlobalError = itemView.findViewById(R.id.global_error_pdp)

    override fun bind(element: VariantErrorDataModel) {
        globalError.setPadding(0, 0, 0, 30.toPx())
        globalError.setType(element.errorType)
        globalError.setActionClickListener {
            listener.onClickRefresh()
        }
    }
}