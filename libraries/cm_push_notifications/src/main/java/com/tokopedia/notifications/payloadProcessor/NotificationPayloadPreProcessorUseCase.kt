package com.tokopedia.notifications.payloadProcessor

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.PayloadConverter
import com.tokopedia.notifications.model.AdvanceTargetingData
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.SerializedNotificationData

class NotificationPayloadPreProcessorUseCase : PayloadPreProcessor() {

    private val gsonExcludeWithoutExpose: Gson by lazy {
        GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    fun getBaseNotificationModel(
        map: Map<String, String>?,
        onSuccess: (BaseNotificationModel, AdvanceTargetingData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            if (map == null)
                onError(Exception("Payload Map is empty"))
            getBaseNotificationModelInternal(map!!, onSuccess)
        } catch (t: Throwable) {
            onError(t)
        }
    }

    fun getBaseNotificationModel(
        payloadStr: String?,
        isAmplification: Boolean,
        onSuccess: (BaseNotificationModel, AdvanceTargetingData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            //todo handle data byte notification
            val serializedNotificationData = gsonExcludeWithoutExpose
                .fromJson(payloadStr, SerializedNotificationData::class.java)
            val advanceTargetingData = AdvanceTargetingData(
                serializedNotificationData.mainAppPriority,
                serializedNotificationData.sellerAppPriority,
                serializedNotificationData.isAdvanceTargeting?.equals("true") ?: false
            )
            val baseNotificationModel = PayloadConverter.convertToBaseModel(serializedNotificationData)
            baseNotificationModel.isAmplification = isAmplification
            onSuccess(baseNotificationModel, advanceTargetingData)
        }catch (t: Throwable){
            onError(t)
        }


    }


    private fun getBaseNotificationModelInternal(
        map: Map<String, String>,
        onSuccess: (BaseNotificationModel, AdvanceTargetingData) -> Unit
    ) {
        if (isCompressedPayload(map)) {
            decompressPayloadData(map, onSuccess)
        } else {
            convertBundleToPayloadData(map, onSuccess)
        }
    }

    private fun decompressPayloadData(
        map: Map<String, String>,
        onSuccess: (BaseNotificationModel, AdvanceTargetingData) -> Unit
    ) {
        val dataStr = getDecompressPayload(map)
        val serializedNotificationData = gsonExcludeWithoutExpose
            .fromJson(dataStr, SerializedNotificationData::class.java)
        val advanceTargetingData = AdvanceTargetingData(
            serializedNotificationData.mainAppPriority,
            serializedNotificationData.sellerAppPriority,
            serializedNotificationData.isAdvanceTargeting?.equals("true") ?: false
        )
        val baseNotificationModel = PayloadConverter.convertToBaseModel(serializedNotificationData)
        baseNotificationModel.isAmplification = false
        onSuccess(baseNotificationModel, advanceTargetingData)
    }


    private fun convertBundleToPayloadData(
        map: Map<String, String>,
        onSuccess: (BaseNotificationModel, AdvanceTargetingData) -> Unit
    ) {
        val bundle = convertMapToBundle(map)
        val baseNotificationModel = PayloadConverter.convertToBaseModel(bundle)
        val advanceTargetingData = getAdvanceTargetingData(bundle)
        onSuccess(baseNotificationModel, advanceTargetingData)
    }


    private fun getAdvanceTargetingData(bundle: Bundle): AdvanceTargetingData {
        val mainAppPriority = bundle.getString(CMConstant.PayloadKeys.MAIN_APP_PRIORITY, "1")
        val sellerAppPriority = bundle.getString(CMConstant.PayloadKeys.SELLER_APP_PRIORITY, "1")
        val isAdvanceTarget =
            PayloadConverter.isBooleanTrue(bundle, CMConstant.PayloadKeys.ADVANCE_TARGET)
        return AdvanceTargetingData(mainAppPriority, sellerAppPriority, isAdvanceTarget)
    }



}

