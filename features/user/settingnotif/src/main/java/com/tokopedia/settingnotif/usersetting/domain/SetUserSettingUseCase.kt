package com.tokopedia.settingnotif.usersetting.domain

import com.tokopedia.settingnotif.usersetting.base.BaseRequestUseCase
import com.tokopedia.settingnotif.usersetting.base.SettingRepository
import com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.Exception
import javax.inject.Inject

open class SetUserSettingUseCase @Inject constructor(
        private val repository: SettingRepository,
        private val graphQuery: String
) : UseCase<SetUserSettingResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): SetUserSettingResponse {
        if (params.paramsAllValueInString.isEmpty()) {
            throw Exception("no param found")
        }

        return BaseRequestUseCase.execute(
                query = graphQuery,
                repository = repository,
                requestParams = params
        )
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_DATA = "data"

        fun params(
                notificationType: String,
                updatedSettingIds: List<Map<String, Any>>
        ): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_TYPE, notificationType)
                putObject(PARAM_DATA, updatedSettingIds)
            }
        }
    }

}