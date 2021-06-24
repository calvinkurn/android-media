package com.tokopedia.tokomart.category.presentation.viewholder

import android.view.View
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder

class CategoryChooseAddressViewHolder(
        itemView: View,
        chooseAddressListener: ChooseAddressListener
): BaseChooseAddressViewHolder(itemView, chooseAddressListener) {

    override val trackingSource: String
        get() = "tokonow category page"
}