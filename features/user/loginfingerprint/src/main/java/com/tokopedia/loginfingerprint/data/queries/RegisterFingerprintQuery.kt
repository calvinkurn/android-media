package com.tokopedia.loginfingerprint.data.queries

object RegisterFingerprintQuery {
    coval query = """
        query register_fingerprint (${'$'}publicKey: String!, ${'$'}signature: String!, $${'$'}: String!){
            RegisterFingerprint(publicKey: ${'$'}publicKey, signature: ${'$'}signature, datetime: ${'$'}datetime) {
                success
                message
                errorMessage
            }
        }
    """
}