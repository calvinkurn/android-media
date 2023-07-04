package com.tokopedia.discovery2.di

import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

internal fun AbstractViewHolder.getSubComponent(): UIWidgetComponent {
    return uiWidgetComponent
}
