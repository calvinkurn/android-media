package com.tokopedia.additional_check.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.additional_check.data.GetSimpleProfileResponse
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import javax.inject.Inject

class RecoverGoogleTinkUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationContext private val repository: GraphqlRepository,
    private val aeadEncryptorImpl: AeadEncryptor,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, Unit>(dispatcher.io) {

    override suspend fun execute(params: Unit) {
        val result: GetSimpleProfileResponse = repository.request(graphqlQuery(), params)
        val profile = result.data

        // delete Android Keystore and DataStore
        aeadEncryptorImpl.delete()
        UserSessionDataStoreClient.reCreate(context)

        val session = UserSession(context)
        session.name = profile.fullName
        session.email = profile.email
        session.profilePicture = profile.profilePicture
        session.phoneNumber = profile.phone
    }

    override fun graphqlQuery(): String = """
        query userProfile {
          profile {
            full_name
            profilePicture
            phone
            email
          }
        } 
        """.trimIndent()
}
