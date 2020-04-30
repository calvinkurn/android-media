package com.tokopedia.settingnotif.usersetting.domain

import com.tokopedia.settingnotif.usersetting.base.BaseRequestUseCase
import com.tokopedia.settingnotif.usersetting.base.SettingRepository
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

open class GetUserSettingUseCase @Inject constructor(
        private val repository: SettingRepository,
        private val graphQuery: String
) : UseCase<UserNotificationResponse>() {

    override suspend fun executeOnBackground(): UserNotificationResponse {
        return BaseRequestUseCase.execute(
                query = graphQuery,
                repository = repository
        )
    }

}