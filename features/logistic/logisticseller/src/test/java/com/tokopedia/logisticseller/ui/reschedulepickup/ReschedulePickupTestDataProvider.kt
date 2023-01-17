package com.tokopedia.logisticseller.ui.reschedulepickup

import com.google.gson.Gson
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.data.response.GetReschedulePickupResponse
import com.tokopedia.logisticseller.domain.mapper.ReschedulePickupMapper
import java.io.IOException

object ReschedulePickupTestDataProvider {
    const val REASON_BELOW_MIN = "reason below15"
    const val REASON_MIN = "reason more t15"
    const val REASON_MAX = "reasonreasonreasonreasonreasonreasonreasonreasonreasonreasonreason reasonreasonreasonreasonreason reasonreasonreasonreasonreasonreason reasonreasonreasonreason"
    const val REASON_MORE_MAX = "reasonreasonreasonreasonreasonreasonreasonreasonreasonreasonreason reasonreasonreasonreasonreason reasonreasonreasonreasonreasonreason reasonreasonreasonreasonre"
    fun getRescheduleInfo(): GetReschedulePickupResponse.Data =
        Gson().fromJson(
            getJsonFromAsset("mpLogisticGetReschedulePickup.json"),
            GetReschedulePickupResponse.Data::class.java
        )

    fun getChosenDay(): RescheduleDayOptionModel {
        val response = getRescheduleInfo()
        val model = ReschedulePickupMapper.mapToState(response.mpLogisticGetReschedulePickup)
        return model.options.dayOptions.first()
    }

    fun getChosenTime(dayOptionModel: RescheduleDayOptionModel): RescheduleTimeOptionModel {
        return dayOptionModel.timeOptions.first()
    }

    fun getChosenReason(): RescheduleReasonOptionModel {
        val response = getRescheduleInfo()
        val model = ReschedulePickupMapper.mapToState(response.mpLogisticGetReschedulePickup)
        return model.options.reasonOptions.first()
    }

    fun getCustomReason(): RescheduleReasonOptionModel {
        val response = getRescheduleInfo()
        val model = ReschedulePickupMapper.mapToState(response.mpLogisticGetReschedulePickup)
        return model.options.reasonOptions.last()
    }

    private fun getJsonFromAsset(path: String?): String {
        var json = ""
        try {
            val inputStream = this.javaClass.classLoader!!.getResourceAsStream(path)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }
}
