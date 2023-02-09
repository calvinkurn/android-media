package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerpersona.data.remote.model.PersonaStatusModel
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

    companion object {
        private const val ERROR_PERSONA_VALUE_IS_EMPTY = "Error : Persona status is empty"
        private const val ERROR_PERSONA_LIST_IS_EMPTY = "Error : Persona list is empty"
    }

    suspend fun execute(shopId: String, page: String): PersonaDataUiModel {
        return withContext(dispatchers.io) {
            try {
                val personaStatusAsync = async { getPersonaStatusUseCase.execute(shopId, page) }
                val personaListAsync = async { getPersonaListUseCase.execute() }

                val personaStatus = personaStatusAsync.await()
                val personaList = personaListAsync.await()

                if (personaStatus.persona.isBlank()) {
                    throw MessageErrorException(ERROR_PERSONA_VALUE_IS_EMPTY)
                }
                if (personaList.isEmpty()) {
                    throw MessageErrorException(ERROR_PERSONA_LIST_IS_EMPTY)
                }

                return@withContext getPersonaData(personaStatus, personaList)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun getPersonaData(
        data: PersonaStatusModel,
        personaList: List<PersonaUiModel>
    ): PersonaDataUiModel {
        val persona = personaList.firstOrNull {
            it.value.equals(data.persona, true)
        }
        return PersonaDataUiModel(
            persona = data.persona,
            personaStatus = getPersonaStatusType(data.status),
            personaData = persona ?: PersonaUiModel(value = data.persona)
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