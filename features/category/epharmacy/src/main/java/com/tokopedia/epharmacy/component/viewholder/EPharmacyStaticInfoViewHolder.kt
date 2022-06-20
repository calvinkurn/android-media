package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyStaticInfoDataModel

class EPharmacyStaticInfoViewHolder(private val view: View,
                                    private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyStaticInfoDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.epharmacy_static_info_view_item
    }

    override fun bind(element: EPharmacyStaticInfoDataModel) {}
}