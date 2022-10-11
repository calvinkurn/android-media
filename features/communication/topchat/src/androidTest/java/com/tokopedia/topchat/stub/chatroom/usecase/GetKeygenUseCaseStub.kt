package com.tokopedia.topchat.stub.chatroom.usecase

import android.content.res.Resources
import com.tokopedia.seamless_login_common.data.KeyPojo
import com.tokopedia.seamless_login_common.data.KeyResponse
import com.tokopedia.seamless_login_common.domain.usecase.GetKeygenUsecase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetKeygenUseCaseStub @Inject constructor(
    resources: Resources,
    private val repositoryStub: GraphqlRepositoryStub
): GetKeygenUsecase(resources, repositoryStub) {

    var response: KeyResponse = KeyResponse(KeyPojo())
        set(value) {
            repositoryStub.createMapResult(response::class.java, value)
            field = value
        }

    var errorMessage = ""
        set(value) {
            if(value.isNotEmpty()) {
                repositoryStub.createErrorMapResult(response::class.java, value)
            }
            field = value
        }
}