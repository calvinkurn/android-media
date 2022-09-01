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
        newRequest.addHeader("X-UniqueId", "fc9ca44bbbcb5dbd")
        newRequest.addHeader("D1", "8B:95:5B:94:7B:F3:45:A3:57:8D:3F:20:CF:0D:A7:24:BB:28:D3:C4:8C:0D:11:77:C8:AF:91:69:FD:7A:C3:CF")
        newRequest.addHeader("X-Session-ID", "4f13e393-ec43-45ea-8d5b-a37382a22d73")
        newRequest.addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJhdWQiOlsiZ29qZWsiLCJtaWR0cmFucyIsImdvdmlldCIsImdvcGF5IiwiZ29wbGF5Il0sImRhdCI6eyJhY3RpdmUiOiJ0cnVlIiwiYmxhY2tsaXN0ZWQiOiJmYWxzZSIsImNvdW50cnlfY29kZSI6Iis5MSIsImNyZWF0ZWRfYXQiOiIyMDE5LTA1LTMwVDA1OjQzOjAwWiIsImVtYWlsIjoidmlzaGFsLnNoYXJtYUBnb2play5jb20iLCJlbWFpbF92ZXJpZmllZCI6InRydWUiLCJnb3BheV9hY2NvdW50X2lkIjoiMDEtYjIxMGE5M2Y2YmNmNDMwN2I5YTlmMjkzNzdjNjI2ZjYtMzQiLCJuYW1lIjoidmlzaGFsIHNoYXJtYSIsIm51bWJlciI6Ijc4MzQ5NjQzMzQiLCJwaG9uZSI6Iis5MTc4MzQ5NjQzMzQiLCJzaWduZWRfdXBfY291bnRyeSI6IklEIiwid2FsbGV0X2lkIjoiMTkxNTAwMzQ0MzkwMDM0MTMwIn0sImV4cCI6MTY2NDA2NTc5MCwiaWF0IjoxNjYxMzYwMTI4LCJpc3MiOiJnb2lkIiwianRpIjoiNjEwYjk2YjEtZWEwMy00YWRlLWI4ZjgtNTVjYmZiMzAzZjdjIiwic2NvcGVzIjpbXSwic2lkIjoiN2ZlNzRiZTktMjU3OC00MDkxLTk4NTktZTQxNmU3MDdmMDM1Iiwic3ViIjoiMzU4OTBlYzAtMWI1Yy00NTQ1LWFjNmItNWZjN2Q4ZmExODcwIiwidWlkIjoiNTc2MDg0IiwidXR5cGUiOiJjdXN0b21lciJ9.gZsy7yfRQiF-fHAk5TsBtS0aw3XQshheuAv4qY7-nnw4_XYoo0A099IN3FCOljbJzt_vPfUWzAEpN9EV-FAt5KpbmssB5AaQLOOYdL2kPlh2hczXPNbxIQswo7rR9Kp6gHwRZNUN8lxE3TJAfb4QFHUkluy3ECuAPA3TIl44uA0")
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
        newRequest.addHeader("X-M1", "1:__28d4bfe1996e4c7da92099f4f0c3ad33,2:UNKNOWN,3:1658470443391-8981395925103109561,4:50472,5:mt6785|2000|8,6:UNKNOWN,7:,8:1080x2153,9:,10:0,11:prmBbONz1yfm573070HzRwVEZYCG/aAHpLFAQ5HKgX8=,12:229f1f0eba2077d19492e45046ce4833f3cfa026,13:IaRsX87HMnxvZ2ir/ms1KmbTVyNKkMXWHGof6LloFqfN3ynnzhLr9zsjCIcQYV9sGE84evpuC4WVzz1bxkPLLWh+W83NI9y2+tGhZUmFtPhYUXd5tXXZduKueMJPEB2GBXWZ5BsUxGBpEOU4ESJe4fFnrcjhXvTP3wru1Md5QyeFRbR4yvZ05/Qb7wZ9qgnXU+yRDdH/l3Meb3rUvjBwPPQB/LyQZaHtVztAjpvu6cuO9OF7+GTRd113CUtX6LUvMxnlgb7qbpEulBeBa0woTOEfvKJmLqCL88GZD7V9kk2H/2/0BwFz+gFo9/GYT9g4jeZjeTHrNJP2ffWWwdPByQ==,14:1661760985")
        newRequest.addHeader("Host", "integration-api.gojekapi.com")
        newRequest.addHeader("Connection", "Keep-Alive")
        newRequest.addHeader("Accept-Encoding", "gzip")
        newRequest.addHeader("User-Agent", "okhttp/3.12.13")
        newRequest.addHeader("If-Modified-Since", "Mon, 29 Aug 2022 08:16:25 GMT")

        return chain.proceed(newRequest.build())
    }
}