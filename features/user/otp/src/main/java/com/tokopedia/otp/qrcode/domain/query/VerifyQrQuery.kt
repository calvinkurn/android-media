package com.tokopedia.otp.qrcode.domain.query

object VerifyQrQuery {

    private const val data = "\$data"
    private const val status = "\$status"
    private const val signature = "\$signature"

    val query: String = """
        query verify_qr($data: String!, $signature: String!, $status: String!){
            VerifyQR(data: $data, signature: $signature, status: $status, otpType:"152") {
                success
                message
                errorMessage
                Imglink
                MessageTitle
                MessageBody
                ButtonType
                Status
            }
        }
    """.trimIndent()
}