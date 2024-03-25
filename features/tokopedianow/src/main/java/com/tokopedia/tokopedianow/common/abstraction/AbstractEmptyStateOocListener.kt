package com.tokopedia.tokopedianow.common.abstraction

import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder

abstract class AbstractEmptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
    override fun onGetEventCategory(): String = ""

    override fun onSwitchService() { /* nothing to do */ }
}
