package com.tokopedia.tradein.raw

const val GQL_VALIDATE_IMEI: String = """query validateImei(${"$"}params : ValidateImeiInput!){
    validateImei(params: ${"$"}params) {
        IsValid
        Message
    }
}
"""