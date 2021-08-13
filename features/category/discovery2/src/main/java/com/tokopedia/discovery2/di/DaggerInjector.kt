package com.tokopedia.discovery2.di

import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

internal fun AbstractViewHolder.getSubComponent ():UIWidgetComponent {
    return with(itemView.context) {
        if(this is DiscoveryActivity) {
            return this.discoveryComponent.provideSubComponent()
        }else {
            throw Exception();
        }
    }
}