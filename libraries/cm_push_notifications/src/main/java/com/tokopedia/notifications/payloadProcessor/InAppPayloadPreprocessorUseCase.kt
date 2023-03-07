package com.tokopedia.notifications.payloadProcessor

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.SerializedCMInAppData
import com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor

class InAppPayloadPreprocessorUseCase : PayloadPreProcessor() {

    private val gsonExcludeWithoutExpose: Gson by lazy {
        GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    fun getCMInAppModel(
        map: Map<String, String>?,
        onSuccess: (CMInApp) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            if (map == null)
                onError(Exception("Payload Map is empty"))
            getCMInAPPModelInternal(map!!, onSuccess, onError)
        } catch (t: Throwable) {
            onError(t)
        }
    }

    fun getCMInAppModel(
        payloadStr: String?,
        isAmplification: Boolean,
        onSuccess: (CMInApp) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            //todo handle data byte notification
            val serializedCMInAppData = gsonExcludeWithoutExpose
                .fromJson(payloadStr, SerializedCMInAppData::class.java)
            val cmInApp = CmInAppBundleConvertor.getCmInApp(serializedCMInAppData)
            cmInApp?.let {
                cmInApp.isAmplification = isAmplification
                onSuccess(cmInApp)
            } ?: run {
                onError(Exception("Payload conversion Failed"))
            }

        } catch (t: Throwable) {
            onError(t)
        }


    }


    private fun getCMInAPPModelInternal(
        map: Map<String, String>,
        onSuccess: (CMInApp) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (isCompressedPayload(map)) {
            decompressPayloadData(map, onSuccess, onError)
        } else {
            convertBundleToPayloadData(map, onSuccess, onError)
        }
    }

    private fun decompressPayloadData(
        map: Map<String, String>,
        onSuccess: (CMInApp) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val dataStr = getDecompressPayload(map)
        val serializedCMInAppData = gsonExcludeWithoutExpose
            .fromJson(dataStr, SerializedCMInAppData::class.java)
        val cmInApp = CmInAppBundleConvertor.getCmInApp(serializedCMInAppData)
        cmInApp?.let {
            cmInApp.isAmplification = false
            onSuccess(cmInApp)
        } ?: run {
            onError(Exception("Payload conversion Failed"))
        }
    }


    private fun convertBundleToPayloadData(
        map: Map<String, String>,
        onSuccess: (CMInApp) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val cmInApp = CmInAppBundleConvertor.getCmInAppModel(gsonExcludeWithoutExpose, map = map)
        cmInApp?.let {
            onSuccess(cmInApp)
        } ?: run {
            onError(Exception("Payload conversion Failed"))
        }
    }

}