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
            if(!::weaverFirebaseConditionCheck.isInitialized) {
                context?.let { initFirebaseRemoteConfig(it, remoteConfigKey) } ?: weaverInterface.execute()
            }
            executeWeaveCoRoutine(weaverInterface, weaverFirebaseConditionCheck)
        }

        fun initFirebaseRemoteConfig(context: Context, remoteConfigKey: String){
            if(!::firebaseRemoteConfig.isInitialized){
                firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
            }
            if(!::weaverFirebaseConditionCheck.isInitialized) {
                weaverFirebaseConditionCheck = WeaverFirebaseConditionCheck(remoteConfigKey, firebaseRemoteConfig)
            }
        }

        fun executeWeaveCoRoutineNow(weaverInterface: WeaveInterface) {
            getasyncWeaveProvider().executeAsync(weaverInterface)
        }

        fun getasyncWeaveProvider() : WeaveAsyncProvider{
            return CoRoutineAsyncWeave()
        }
    }
}