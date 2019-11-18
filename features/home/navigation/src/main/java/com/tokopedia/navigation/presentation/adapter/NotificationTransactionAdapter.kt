package com.tokopedia.navigation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactoryImpl

class NotificationTransactionAdapter(
        notificationFactory: NotificationTransactionFactoryImpl
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(notificationFactory)