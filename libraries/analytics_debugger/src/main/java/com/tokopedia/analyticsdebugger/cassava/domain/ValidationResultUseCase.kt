package com.tokopedia.analyticsdebugger.cassava.domain

import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import javax.inject.Inject

/**
 * @author by furqan on 07/04/2021
 */
class ValidationResultUseCase @Inject constructor(private val repository: CassavaRepository) {

    suspend fun execute(journeyId: String,
                        validationResult: ValidationResultRequest) {
        repository.sendValidationResult(
                journeyId = journeyId,
                validationResult = validationResult
        )
    }

}