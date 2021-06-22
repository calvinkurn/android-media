package com.tkpd.atcvariant.view.viewholder

import android.view.View
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.VariantShimmeringDataModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * Created by Yehezkiel on 11/05/21
 */
class AtcVariantShimmeringViewHolder(view: View) : AbstractViewHolder<VariantShimmeringDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_shimmering_viewholder
    }

    private val loader: LoaderUnify = view.findViewById(R.id.atc_loader)

    override fun bind(element: VariantShimmeringDataModel?) {
        loader.circular?.start()
    }

}