package com.tokopedia.rechargeocr.util

object GqlQuery {
    val rechargeCameraRecognition = """
        query RechargeCameraRecognition(${'$'}Image: String){
          rechargeOCR(Type:OCR, Image:${'$'}Image) {
            result
          }
        }
    """.trimIndent()
}