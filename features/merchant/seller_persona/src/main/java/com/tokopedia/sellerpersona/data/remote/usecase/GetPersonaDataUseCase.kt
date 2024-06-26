package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerpersona.data.local.PersonaSharedPrefInterface
import com.tokopedia.sellerpersona.data.remote.model.PersonaStatusModel
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.sellerpersona.view.model.isActive
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class GetPersonaDataUseCase @Inject constructor(
    private val getPersonaStatusUseCase: GetPersonaStatusUseCase,
    private val getPersonaListUseCase: GetPersonaListUseCase,
    private val sharedPref: PersonaSharedPrefInterface,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) {

    companion object {
        private const val ERROR_PERSONA_VALUE_IS_EMPTY = "Error : Persona status is empty"
        private const val ERROR_PERSONA_LIST_IS_EMPTY = "Error : Persona list is empty"
        private const val ERROR_PERSONA_NAME_IS_NOT_VALID =
            "Error : Persona with name `%s` is not valid"
    }

    suspend fun execute(shopId: String, page: String): PersonaDataUiModel {
        return withContext(dispatchers.io) {
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
        }
    }

    private fun getPersonaData(
        data: PersonaStatusModel, personaList: List<PersonaUiModel>
    ): PersonaDataUiModel {
        val persona = personaList.firstOrNull {
            it.value.equals(data.persona, true)
        }
        if (persona == null) {
            val message = String.format(
                ERROR_PERSONA_NAME_IS_NOT_VALID, data.persona
            )
            throw MessageErrorException(message)
        }
        val personaStatus = getPersonaStatusType(data.status)
        return PersonaDataUiModel(
            persona = data.persona,
            personaStatus = personaStatus,
            personaData = persona,
            isSwitchChecked = personaStatus.isActive(),
            isShopOwner = userSession.isShopOwner,
            isFirstVisit = sharedPref.isFirstVisit()
        )
    }

    private fun getPersonaStatusType(personaStatus: Int): PersonaStatus {
        return when (personaStatus) {
            Int.ZERO -> PersonaStatus.INACTIVE
            Int.ONE -> PersonaStatus.ACTIVE
            else -> PersonaStatus.UNDEFINED
        }
    }
}