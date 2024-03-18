package com.tokopedia.sessioncommon.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.register.InputRegisterGqlParam
import com.tokopedia.sessioncommon.data.register.RegisterGqlParam
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.data.register.ShopCreationRegisterGqlParam
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 30/04/19.
 */
class RegisterUseCase @Inject constructor(
    val resources: Resources,
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface
) : CoroutineUseCase<InputRegisterGqlParam, RegisterPojo>(dispatcher.io) {

    companion object {
        private val OS_TYPE_ANDROID: String = "1"
        private val REG_TYPE_PHONE: String = "phone"
        private val ACCOUNTS_TYPE_SELLER_ONBOARDING: String = "SellerOnboarding"

        fun generateParamRegisterPhone(name: String, phoneNumber: String, token: String):
            InputRegisterGqlParam {
            return InputRegisterGqlParam(
                input = RegisterGqlParam(
                    phone = phoneNumber,
                    fullname = name,
                    osType = OS_TYPE_ANDROID,
                    regType = REG_TYPE_PHONE
                )
            )
        }

        fun generateParamRegisterPhoneShopCreation(name: String, phoneNumber: String):
            InputRegisterGqlParam {
            return InputRegisterGqlParam(
                input = ShopCreationRegisterGqlParam(
                    phone = phoneNumber,
                    fullname = name,
                    accountsTypeName = ACCOUNTS_TYPE_SELLER_ONBOARDING,
                    osType = OS_TYPE_ANDROID,
                    regType = REG_TYPE_PHONE
                )
            )
        }
    }

    override fun graphqlQuery(): String {
        return """mutation register(${'$'}input: RegisterRequest!){
                    register(input:${'$'}input){
                        user_id
                        sid
                        access_token
                        refresh_token
                        token_type
                        is_active
                        action
                        enable_2fa
                        enable_skip_2fa
                        errors {
                          name
                          message
                        }
                      }
                }""".trim()
    }

    override suspend fun execute(params: InputRegisterGqlParam): RegisterPojo {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        return repository.request(graphqlQuery(), params)
    }
}
