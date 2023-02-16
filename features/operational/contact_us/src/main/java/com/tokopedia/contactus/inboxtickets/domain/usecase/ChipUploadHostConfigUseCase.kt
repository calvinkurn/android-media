package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.data.gqlqueries.CHIP_UPLOAD_HOST_GQL
import com.tokopedia.contactus.inboxtickets.data.model.ChipUploadHostConfig
import javax.inject.Inject

class ChipUploadHostConfigUseCase @Inject constructor(private val repository: ContactUsRepository) {

    suspend fun getChipUploadHostConfig(): ChipUploadHostConfig {
        return repository
            .getGQLData(
                CHIP_UPLOAD_HOST_GQL,
                ChipUploadHostConfig::class.java,
                mapOf()
            )
    }
}
