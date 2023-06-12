package com.tokopedia.tokopedianow.category.presentation.callback

import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder

class TokoNowChooseAddressWidgetCallback(
    private val onClickChooseAddressWidgetTracker: () -> Unit
):  TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener {
    override fun onChooseAddressWidgetRemoved() {
        /* nothing to do */
    }

    override fun onClickChooseAddressWidgetTracker() = onClickChooseAddressWidgetTracker.invoke()
}
