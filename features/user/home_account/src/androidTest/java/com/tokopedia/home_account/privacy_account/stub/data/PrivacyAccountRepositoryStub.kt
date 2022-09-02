package com.tokopedia.home_account.privacy_account.stub.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.privacy_account.data.GetConsentDataModel
import com.tokopedia.home_account.privacy_account.data.LinkStatusResponse
import com.tokopedia.home_account.privacy_account.data.SetConsentDataModel
import com.tokopedia.home_account.utils.FileUtils.createResponseFromJson
import javax.inject.Inject
import com.tokopedia.home_account.test.R

class PrivacyAccountRepositoryStub @Inject constructor() : GraphqlRepository {

    private var _state: TestState = TestState.ACCOUNT_LINKED

    fun setState(state: TestState) {
        _state = state
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse =
        when (_state) {
            TestState.GET_PRIVACY_ACCOUNT_ENABLED -> createResponseFromJson<GetConsentDataModel>(R.raw.privacy_account_get_consent_enabled_success)
            TestState.GET_PRIVACY_ACCOUNT_DISABLED -> createResponseFromJson<GetConsentDataModel>(R.raw.privacy_account_get_consent_disabled_success)
            TestState.SET_CONSENT_SOCIAL_NETWORK_SUCCESS -> createResponseFromJson<SetConsentDataModel>(R.raw.privacy_account_set_consent_success)
            TestState.SET_CONSENT_SOCIAL_NETWORK_FAILED -> createResponseFromJson<SetConsentDataModel>(R.raw.privacy_account_set_consent_failed)
            TestState.ACCOUNT_LINKED -> createResponseFromJson<LinkStatusResponse>(R.raw.success_get_link_status)
            TestState.ACCOUNT_NOT_LINKED -> createResponseFromJson<LinkStatusResponse>(R.raw.success_get_link_status_not_linked)
        }
}