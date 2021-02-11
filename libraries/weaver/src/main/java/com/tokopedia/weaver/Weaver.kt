package com.tokopedia.weaver

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class Weaver{


    companion object {

        lateinit var firebaseRemoteConfig : FirebaseRemoteConfigImpl
        lateinit var weaverFirebaseConditionCheck : WeaverFirebaseConditionCheck

        fun <KEY_TYPE, ACS_HLPR> executeWeave(weaverInterface: WeaveInterface, weaverConditionCheckProvider: WeaverConditionCheckProvider<KEY_TYPE, ACS_HLPR>, weaveAsyncProvider: WeaveAsyncProvider) {
            if (weaverConditionCheckProvider.checkCondition()) {
                weaveAsyncProvider.executeAsync(weaverInterface)
            }else{
                weaverInterface.execute()
            }
        }

        fun <KEY_TYPE, ACS_HLPR> executeWeaveCoRoutine(weaverInterface: WeaveInterface, weaverConditionCheckProvider: WeaverConditionCheckProvider<KEY_TYPE, ACS_HLPR>) {
            executeWeave(weaverInterface, weaverConditionCheckProvider, getasyncWeaveProvider())
        }

        fun executeWeaveCoRoutineWithFirebase(weaverInterface: WeaveInterface, remoteConfigKey: String, context: Context?) {
            if(!::firebaseRemoteConfig.isInitialized) {
                context?.let { firebaseRemoteConfig = FirebaseRemoteConfigImpl(context) } ?: weaverInterface.execute()
            }
            weaverFirebaseConditionCheck = WeaverFirebaseConditionCheck(remoteConfigKey, firebaseRemoteConfig)
            executeWeaveCoRoutine(weaverInterface, weaverFirebaseConditionCheck)
        }

        fun executeWeaveCoRoutineNow(weaverInterface: WeaveInterface) {
            getasyncWeaveProvider().executeAsync(weaverInterface)
        }

        fun getasyncWeaveProvider() : WeaveAsyncProvider{
            return CoRoutineAsyncWeave()
        }
    }
}