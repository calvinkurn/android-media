package com.tokopedia.weaver

import android.content.Context
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class Weaver {

    companion object {

        var firebaseRemoteConfig: FirebaseRemoteConfigImpl? = null

        fun <KEY_TYPE, ACS_HLPR, DEF_VAL> executeWeave(
            weaverInterface: WeaveInterface,
            weaverConditionCheckProvider: WeaverConditionCheckProvider<KEY_TYPE, ACS_HLPR, DEF_VAL>,
            weaveAsyncProvider: WeaveAsyncProvider
        ) {
            if (weaverConditionCheckProvider.checkCondition()) {
                weaveAsyncProvider.executeAsync(weaverInterface)
            } else {
                weaverInterface.execute()
            }
        }

        fun <KEY_TYPE, ACS_HLPR, DEF_VAL> executeWeaveCoRoutine(
            weaverInterface: WeaveInterface,
            weaverConditionCheckProvider: WeaverConditionCheckProvider<KEY_TYPE, ACS_HLPR, DEF_VAL>
        ) {
            try {
                executeWeave(weaverInterface, weaverConditionCheckProvider, getasyncWeaveProvider())
            } catch (ex: Exception) {
                weaverInterface.execute()
                if (ex.localizedMessage != null) {
                    val errorMap = mapOf("type" to "crashLog", "reason" to (ex.localizedMessage))
                    logError(errorMap)
                }
            }
        }

        fun executeWeaveCoRoutineWithFirebase(
            weaverInterface: WeaveInterface, remoteConfigKey: String, context: Context?,
            defaultValue: Boolean = false
        ) {
            if (firebaseRemoteConfig == null) {
                context?.let {
                    firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
                } ?: weaverInterface.execute()
            }
            val weaverFirebaseConditionCheck = WeaverFirebaseConditionCheck(
                remoteConfigKey,
                firebaseRemoteConfig,
                defaultValue
            )
            executeWeaveCoRoutine(weaverInterface, weaverFirebaseConditionCheck)
        }

        fun executeWeaveCoRoutineNow(weaverInterface: WeaveInterface) {
            getasyncWeaveProvider().executeAsync(weaverInterface)
        }

        fun getasyncWeaveProvider(): WeaveAsyncProvider {
            return CoRoutineAsyncWeave()
        }

        private fun logError(messageMap: Map<String, String>) {
            ServerLogger.log(Priority.P1, "WEAVER_CRASH", messageMap)
        }
    }
}