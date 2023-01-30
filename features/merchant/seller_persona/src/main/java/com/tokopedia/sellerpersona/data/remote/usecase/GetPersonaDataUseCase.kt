package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerpersona.data.remote.model.GetPersonaStatusResponse
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class GetPersonaDataUseCase @Inject constructor(
    private val getPersonaStatusUseCase: GetPersonaStatusUseCase,
    private val getPersonaListUseCase: GetPersonaListUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun execute(): PersonaDataUiModel {
        return withContext(dispatchers.io) {
            try {
                val personaStatusAsync = async { getPersonaStatusUseCase.executeOnBackground() }
                val personaListAsync = async { getPersonaListUseCase.execute() }
                return@withContext getPersonaData(
                    personaStatusAsync.await(),
                    personaListAsync.await()
                )
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun getPersonaData(
        personaStatus: GetPersonaStatusResponse,
        personaList: List<PersonaUiModel>
    ): PersonaDataUiModel {
        val persona = personaList.firstOrNull {
            it.name.equals(personaStatus.data.personaStatus, true)
        }
        return PersonaDataUiModel(
            persona = personaStatus.data.persona,
            personaStatus = getPersonaStatusType(personaStatus.data.personaStatus),
            personaData = persona ?: PersonaUiModel(name = personaStatus.data.persona)
        )
    }

    private fun getPersonaStatusType(personaStatus: String): PersonaStatus {
        return when (personaStatus) {
            Int.ZERO.toString() -> PersonaStatus.INACTIVE
            Int.ONE.toString() -> PersonaStatus.ACTIVE
            else -> PersonaStatus.UNDEFINED
        }
    }
}