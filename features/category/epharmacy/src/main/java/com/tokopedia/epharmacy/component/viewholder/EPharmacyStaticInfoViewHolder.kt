package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyGalleryAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyImagesDecoration
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyStaticInfoDataModel
import com.tokopedia.epharmacy.utils.ERROR_MAX_MEDIA_ITEM
import com.tokopedia.epharmacy.utils.MAX_MEDIA_ITEM
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class EPharmacyStaticInfoViewHolder(private val view: View,
                                    private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyStaticInfoDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.epharmacy_static_info_view_item
    }

    override fun bind(element: EPharmacyStaticInfoDataModel) {

    }
}