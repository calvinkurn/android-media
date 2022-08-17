package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import java.util.Date

object LoadingItem : DelegateAdapterItem {
    override fun id() = true
    override fun content() = this
}
