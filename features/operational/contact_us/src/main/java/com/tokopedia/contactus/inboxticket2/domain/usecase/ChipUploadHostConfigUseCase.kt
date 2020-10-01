package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.data.gqlqueries.CHIP_UPLOAD_HOST_GQL
import com.tokopedia.contactus.inboxticket2.data.model.ChipUploadHostConfig
import javax.inject.Inject

class ChipUploadHostConfigUseCase @Inject constructor(private val repository: ContactUsRepository) {

    suspend fun getChipUploadHostConfig(): ChipUploadHostConfig {
        val chipUploadHostConfig = repository
                .getGQLData<ChipUploadHostConfig>(CHIP_UPLOAD_HOST_GQL,
                        ChipUploadHostConfig::class.java, mapOf())
        return chipUploadHostConfig;
    }

}