package com.tokopedia.tokochat.stub.domain.response

data class ApiResponseModelStub(
    var responseCode: Int,
    var responseJsonPath: String,
    var responseEditor: ((String) -> String)? = null
)
