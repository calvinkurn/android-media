package com.tokopedia.notifications.inApp

import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleCallback

object CmEventListener {
    val pushIntentContractList = arrayListOf<PushIntentContract>()
    val fragmentLifecycleCallbackList = arrayListOf<FragmentLifecycleCallback>()
    val inAppPopupContractMap = mutableMapOf<String, InAppPopupContract>()
}