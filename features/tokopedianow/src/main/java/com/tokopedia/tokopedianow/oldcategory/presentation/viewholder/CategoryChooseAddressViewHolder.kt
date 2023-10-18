package com.tokopedia.tokopedianow.oldcategory.presentation.viewholder

import android.view.View
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder

class CategoryChooseAddressViewHolder(
        itemView: View,
        chooseAddressListener: ChooseAddressListener
): BaseChooseAddressViewHolder(itemView, chooseAddressListener) {

    override val trackingSource: String
        get() = "tokonow category page"
}
