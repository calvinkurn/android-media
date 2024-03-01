package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.delegate.OrigamiListenerDelegateImpl

class HomeOrigamiListenerDelegate constructor(
    context: Context?,
    homeCategoryListener: HomeCategoryListener
) : OrigamiListenerDelegateImpl(
    CampaignWidgetComponentCallback(context, homeCategoryListener),
    Kd4WidgetCallback(context, homeCategoryListener)
)
