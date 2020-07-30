package com.tokopedia.liveness.data.model

object MockUploadImageResponse {
    val successRegister = """
        {
            "data": {
                "is_success_register": true,
                "list_retake": null,
                "list_message": null,
                "apps": {
                    "title": "",
                    "subtitle": "",
                    "button": ""
                }
            }
        }
    """.trimIndent()


    val failedRegister = """
        {
            "data": {
                "is_success_register": false,
                "list_retake": [
                  1,2
                ],
                "list_message": [
                  "Pastikan foto KTP bisa dikenali sebagai orang yang sama",
                  "Scan wajah di tempat terang",
                  "Pastikan foto KTP tidak terpotong,tidak gelap, dan tidak buram"
                ],
                "apps": {
                    "title": "Foto KTP dan Verifikasi Wajah tidak dapat diproses, silakan coba lagi.",
                    "subtitle": "Agar verifikasi berhasil, scan wajah kamu di tempat terang, ya.",
                    "button": "Coba lagi"
                }
            }
        }
    """.trimIndent()

    val badRequest = """
        {
            "header": {
                "process_time": 0.003458706,
                "messages": [
                    "Testing"
                ],
                "error_code": "30003"
            },
            "data": null
        }
    """.trimIndent()
}