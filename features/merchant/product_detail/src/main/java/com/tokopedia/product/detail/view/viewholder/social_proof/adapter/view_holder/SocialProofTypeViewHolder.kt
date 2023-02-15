package com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofData

/**
 * Created by yovi.putra on 15/02/23"
 * Project name: android-tokopedia-core
 **/


abstract class SocialProofTypeViewHolder(
    val view: View
) : RecyclerView.ViewHolder(view) {

    abstract fun bind(
        socialProof: SocialProofData,
        trackData: ComponentTrackDataModel?
    )
}
