package com.tokopedia.chat_service.data.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class GojekInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request.Builder = chain.request().newBuilder()

        newRequest.addHeader("X-AppVersion", "4.49.0")
        newRequest.addHeader("X-AppId", "com.gojek.app.dev")
        newRequest.addHeader("Accept", "application/json")
        newRequest.addHeader("Content-Type", "application/json")
        newRequest.addHeader("X-UniqueId", "416150abb6c7fdf1")
        newRequest.addHeader("D1", "C5:90:BC:EE:EC:62:BD:5A:CC:86:5A:78:05:0D:E9:F7:37:1B:61:8A:86:94:C4:57:6D:C3:21:65:DC:59:DC:C0")
        newRequest.addHeader("X-Session-ID", "4f13e393-ec43-45ea-8d5b-a37382a22d73")
        newRequest.addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJhdWQiOlsiZ29qZWsiXSwiZGF0Ijp7ImFsbF9zZXNzaW9uX2NvdW50IjoiMCIsImNhdGVnb3J5IjoiMlciLCJjb3VudHJ5Q29kZSI6IklEIiwiZW1haWwiOiJnb3Rlc3QuemVlc2hhbmhhbmRhQGV4YW1wbGUub3JnIiwibmFtZSI6Ik9tYXIgTWFyeWFkaSIsInBob25lIjoiKzYyODEwMDAyOTgxNSIsInB1YmxpY0lkIjoiNjZEQjM1NkMyRDA1QkNGRTEzQ0MxNTFEQTg2QTM3RTYiLCJzYW1lX2RldmljZV9zZXNzaW9uX2NvdW50IjoiMCIsInNlcnZpY2VBcmVhIjoiMSIsInRpbWV6b25lIjoiQXNpYS9KYWthcnRhIn0sImV4cCI6MTY2MzA1ODA2OCwiaWF0IjoxNjYyOTY1MDIxLCJpc3MiOiJnb2lkIiwianRpIjoiMGY5OGE2MjMtYjM1YS00NWJkLTkyOTEtYjk0NGIxNDlmNjUxIiwic2NvcGVzIjpbImQ6cGFydG5lcjpyIiwiZDpwYXJ0bmVyOnciLCJkOnBhcnRuZXI6dCJdLCJzaWQiOiIzMDJjZmYzOS03YTA2LTQ0MWMtOTM2NS1kMzNhNjZkYWM3MzIiLCJzdWIiOiIxMDA3MzJjOS01ODM3LTRlOTEtYWYxMi0xMjNlZGQ2N2ViZWIiLCJ1aWQiOiI5NDIzMTc0MDAiLCJ1dHlwZSI6ImRyaXZlciJ9.JCYE8ZxBuiajF5vnObVNeVqFKZ43Vq2h04m9BGvn1J_mIhbfg4rURIyX1FEkC-Ng3lFxlFeBbgAP2-BVm0891sCxJju2WaoLY89jJJ4dxFSniHqivRVbnfa9rB-3BqZgBjI18ct2DW5SBYSA8E5KYXWEzv7s1v68_Y4wbg_-tdg")
        newRequest.addHeader("X-User-Type", "customer")
        newRequest.addHeader("X-DeviceOS", "Android,11")
        newRequest.addHeader("User-uuid", "576084")
        newRequest.addHeader("X-DeviceToken", "d9n2LfAoQxWIdNvsyrD5Us:APA91bH8IAhCrA0qZ0T1hXuVu-IItmtbht0nuzA3jLhysUST1Xbj8GR43_1pmmYC9r9YTcLD692uHKBa_yn1zr1ehuTIyRpd2K0Td3MIul8VbKoVkcsu7BjQ-wF5NSGZ3mUKE9aBNc4n")
        newRequest.addHeader("X-PhoneMake", "realme")
        newRequest.addHeader("X-PushTokenType", "FCM")
        newRequest.addHeader("X-PhoneModel", "realme,RMX2156")
        newRequest.addHeader("Accept-Language", "en-ID")
        newRequest.addHeader("X-User-Locale", "en_ID")
        newRequest.addHeader("X-Location", "30.6872676,76.7180702")
        newRequest.addHeader("X-Location-Accuracy", "3.9")
        newRequest.addHeader("Gojek-Country-Code", "ID")
        newRequest.addHeader("Gojek-Service-Area", "9000")
        newRequest.addHeader("Gojek-Timezone", "Asia/Jakarta")
        newRequest.addHeader("X-M1", "1:__h942317400-416150abb6c7fdf1,2:UNKNOWN,3:UNKNOWN,4:5951,5:|UNKNOWN|4,6:02:15:B2:00:00:00,7:,8:1080x1794,9:passive\\,gps\\,network,10:0,11:416150abb6c7fdf1,12:a34c4f9412a5575429039cd54b390a63febd2a86")
        newRequest.addHeader("Host", "integration-api.gojekapi.com")
        newRequest.addHeader("Connection", "Keep-Alive")
        newRequest.addHeader("Accept-Encoding", "gzip")
        newRequest.addHeader("User-Agent", "okhttp/3.12.13")
        newRequest.addHeader("If-Modified-Since", "Mon, 29 Aug 2022 08:16:25 GMT")

        return chain.proceed(newRequest.build())
    }
}