package com.tokopedia.otp.stub.verification.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodUseCase

class GetVerificationMethodUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : GetVerificationMethodUseCase(graphqlRepository, dispatcher) {

    var response = OtpModeListPojo(
//            OtpModeListData(
//                    success = true,
//                    message = "Otp mode found",
//                    otpDigit = 4,
//                    linkType = 0,
//                    modeList = arrayListOf(
//                            ModeListData(
//                                    modeCode = 1,
//                                    modeText = "sms",
//                                    otpListText = "Melalui <b>SMS</b> ke <br>0000-0000-000",
//                                    afterOtpListText = "Kode verifikasi telah dikirimkan melalui SMS ke 0000-0000-000",
//                                    afterOtpListTextHtml = "Kode verifikasi telah dikirimkan melalui SMS ke \u003cb\u003e0000-0000-000\u003c/b\u003e.",
//                                    otpListImgUrl = "https://ecs7.tokopedia.net/img/otp/sms-green@2x.png",
//                                    usingPopUp = false,
//                                    countdown = true,
//                                    otpDigit = 4
//                            )
//                    )
//            )
    )

    override suspend fun getData(parameter: Map<String, Any>): OtpModeListPojo {
        return response
    }
}