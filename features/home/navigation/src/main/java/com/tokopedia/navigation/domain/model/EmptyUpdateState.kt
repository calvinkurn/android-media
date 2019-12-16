package com.tokopedia.navigation.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateTypeFactory

class EmptyUpdateState(
        val icon: Int,
        val title: String
): Visitable<NotificationUpdateTypeFactory> {

    override fun type(typeFactory: NotificationUpdateTypeFactory): Int {
        return typeFactory.type(this)
    }

}