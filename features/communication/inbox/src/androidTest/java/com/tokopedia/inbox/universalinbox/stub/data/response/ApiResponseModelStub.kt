package com.tokopedia.inbox.universalinbox.stub.data.response

data class ApiResponseModelStub(
    var responseCode: Int,
    var responseJsonPath: String,
    var responseEditor: ((String) -> String)? = null
)
