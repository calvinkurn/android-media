package com.tokopedia.notifications.inApp

import com.tokopedia.fragmentLifecycle.FragmentLifecycleCallback
import com.tokopedia.notifications.inApp.CmEventListener.fragmentLifecycleCallbackList
import com.tokopedia.notifications.inApp.CmEventListener.pushIntentContractList


object CmEventListenerManager {


    fun unregister(pushIntentContract: PushIntentContract? = null,
                   fragmentLifecycleCallback: FragmentLifecycleCallback? = null
    ) {

        if (pushIntentContract != null) {
            pushIntentContractList.remove(pushIntentContract)
        }

        if (fragmentLifecycleCallback != null) {
            fragmentLifecycleCallbackList.remove(fragmentLifecycleCallback)
        }
    }

    fun register(pushIntentContract: PushIntentContract? = null,
                 fragmentLifecycleCallback: FragmentLifecycleCallback? = null,
                 inAppTypeList: ArrayList<String>? = null,
                 inAppPopupContract: InAppPopupContract? = null
    ) {
        if (pushIntentContract != null) {
            pushIntentContractList.add(pushIntentContract)
        }

        if (fragmentLifecycleCallback != null) {
            fragmentLifecycleCallbackList.add(fragmentLifecycleCallback)
        }

        if (inAppTypeList != null && inAppTypeList.isNotEmpty() && inAppPopupContract != null) {
            for (item in inAppTypeList) {
                CmEventListener.inAppPopupContractMap[item] = inAppPopupContract
            }
        }
    }
}