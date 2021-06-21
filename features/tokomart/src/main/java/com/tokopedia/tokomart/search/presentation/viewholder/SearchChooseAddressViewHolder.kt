package com.tokopedia.tokomart.search.presentation.viewholder

import android.view.View
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder

class SearchChooseAddressViewHolder(
        itemView: View,
        chooseAddressListener: ChooseAddressListener,
): BaseChooseAddressViewHolder(itemView, chooseAddressListener) {

    override val trackingSource: String
        get() = "tokonow search page"
}