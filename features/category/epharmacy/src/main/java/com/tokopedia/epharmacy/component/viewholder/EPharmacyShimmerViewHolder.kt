package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyShimmerDataModel

class EPharmacyShimmerViewHolder(
    view: View
) : AbstractViewHolder<EPharmacyShimmerDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.epharmacy_attachment_shimmer
    }

    override fun bind(element: EPharmacyShimmerDataModel) {}
}
