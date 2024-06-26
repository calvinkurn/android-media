package com.tokopedia.tokopedianow.common.abstraction

import com.tokopedia.tokopedianow.common.viewholder.TokoNowThematicHeaderViewHolder

abstract class AbstractThematicHeaderListener: TokoNowThematicHeaderViewHolder.TokoNowHeaderListener {
    override fun onClickChooseAddressWidgetTracker() { /* do nothing */ }

    override fun onCloseTicker() { /* do nothing */ }
}
