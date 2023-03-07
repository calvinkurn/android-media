package com.tokopedia.loginregister.redefineregisteremail.stub.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.loginregister.redefineregisteremail.utils.FileUtils.createResponseFromJson
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileUpdateModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileValidateModel
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data.ValidateUserDataModel
import com.tokopedia.loginregister.test.R
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.RegisterCheckModel
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.data.register.RegisterV2Model
import java.util.*
import javax.inject.Inject

class RedefineRegisterRepositoryStub @Inject constructor() : GraphqlRepository {

    private val _queueRequest: Queue<RedefineRegisterTestState> = LinkedList()
    private var _state: RedefineRegisterTestState = RedefineRegisterTestState.GENERATE_KEY_SUCCESS

    fun setResponseQueue(vararg states: RedefineRegisterTestState) {
        _queueRequest.addAll(states)
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        _state = _queueRequest.remove()
        return getResponseInputEmail(_state)
    }

    private fun getResponseInputEmail(state: RedefineRegisterTestState): GraphqlResponse =
        when (state) {
            // page input email
            RedefineRegisterTestState.GENERATE_KEY_SUCCESS -> {
                createResponseFromJson<GenerateKeyPojo>(R.raw.generate_key_success)
            }
            RedefineRegisterTestState.GENERATE_KEY_FAILED -> {
                createResponseFromJson<GenerateKeyPojo>(R.raw.generate_key_failed)
            }
            RedefineRegisterTestState.VALIDATE_USER_DATA_SUCCESS -> {
                createResponseFromJson<ValidateUserDataModel>(R.raw.validate_user_data_success)
            }
            RedefineRegisterTestState.VALIDATE_USER_DATA_FAILED -> {
                createResponseFromJson<ValidateUserDataModel>(R.raw.validate_user_data_failed)
            }
            RedefineRegisterTestState.VALIDATE_USER_DATA_REGISTERED -> {
                createResponseFromJson<ValidateUserDataModel>(R.raw.validate_user_data_registered)
            }

            // page input phone
            RedefineRegisterTestState.REGISTER_V2_SUCCESS -> {
                createResponseFromJson<RegisterV2Model>(R.raw.register_v2_success)
            }
            RedefineRegisterTestState.REGISTER_V2_FAILED -> {
                createResponseFromJson<RegisterV2Model>(R.raw.register_v2_failed)
            }
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS -> {
                createResponseFromJson<ProfilePojo>(R.raw.get_user_info_success)
            }
            RedefineRegisterTestState.GET_USER_INFO_FAILED -> {
                createResponseFromJson<ProfilePojo>(R.raw.get_user_info_failed)
            }
            RedefineRegisterTestState.REGISTER_CHECK_EXIST -> {
                createResponseFromJson<RegisterCheckModel>(R.raw.register_check_exist)
            }
            RedefineRegisterTestState.REGISTER_CHECK_NOT_EXIST -> {
                createResponseFromJson<RegisterCheckModel>(R.raw.register_check_not_exist)
            }
            RedefineRegisterTestState.REGISTER_CHECK_FAILED -> {
                createResponseFromJson<RegisterCheckModel>(R.raw.register_check_failed)
            }
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_VALID -> {
                createResponseFromJson<UserProfileValidateModel>(R.raw.user_profile_validate_valid)
            }
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_INVALID -> {
                createResponseFromJson<UserProfileValidateModel>(R.raw.user_profile_validate_invalid)
            }
            RedefineRegisterTestState.USER_PROFILE_UPDATE_SUCCESS -> {
                createResponseFromJson<UserProfileUpdateModel>(R.raw.user_profile_update_success)
            }
            RedefineRegisterTestState.USER_PROFILE_UPDATE_FAILED -> {
                createResponseFromJson<UserProfileUpdateModel>(R.raw.user_profile_update_failed)
            }
        }
}
