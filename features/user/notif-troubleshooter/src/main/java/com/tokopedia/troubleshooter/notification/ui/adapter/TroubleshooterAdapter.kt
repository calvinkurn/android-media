package com.tokopedia.troubleshooter.notification.ui.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory

internal open class TroubleshooterAdapter(
        factory: TroubleshooterItemFactory
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(factory)