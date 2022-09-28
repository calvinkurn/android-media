package com.tokopedia.tokochat.data.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class GojekInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request.Builder = chain.request().newBuilder()

        newRequest.addHeader("X-AppVersion", "4.49.0")
        newRequest.addHeader("X-AppId", "com.gojek.app.staging")
        newRequest.addHeader("Accept", "application/json")
        newRequest.addHeader("Content-Type", "application/json")
        newRequest.addHeader("X-UniqueId", "c9866dcf50f6f8d1")
        newRequest.addHeader("D1", "C6:F2:C8:87:39:66:4F:F2:F8:BF:96:6D:1F:EB:70:99:80:FF:DF:C5:ED:3E:8B:02:BE:75:BB:56:3C:D9:3B:06")
        newRequest.addHeader("X-Session-ID", "4f13e393-ec43-45ea-8d5b-a37382a22d73")
        newRequest.addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJhdWQiOlsiZ29qZWsiLCJtaWR0cmFucyIsImdvdmlldCIsImdvcGF5IiwiZ29wbGF5Il0sImRhdCI6eyJhY3RpdmUiOiJ0cnVlIiwiYmxhY2tsaXN0ZWQiOiJmYWxzZSIsImNvdW50cnlfY29kZSI6Iis2MiIsImNyZWF0ZWRfYXQiOiIyMDIyLTA5LTEyVDA2OjUwOjAwWiIsImVtYWlsIjoia2VsdmluLnNhcHV0cmFAdG9rb3BlZGlhLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjoidHJ1ZSIsImdvcGF5X2FjY291bnRfaWQiOiIwMS0zODE2MmUzNmE3Zjc0Y2E3ODVhYmQwNWFhNTk1NDc4ZC0zOCIsIm5hbWUiOiJLZWx2aW4iLCJudW1iZXIiOiI4NTE1NjM2NDY5NSIsInBob25lIjoiKzYyODUxNTYzNjQ2OTUiLCJzaWduZWRfdXBfY291bnRyeSI6IklEIiwid2FsbGV0X2lkIjoiMjIyNTUwNDEwMjQwMDIyMTM1In0sImV4cCI6MTY2NjAxMjQxNiwiaWF0IjoxNjYyOTc0NTIzLCJpc3MiOiJnb2lkIiwianRpIjoiYjRlYTE2OWYtODJkMC00YjdiLWIyODItMzhiNjI2ZjQ2NzIzIiwic2NvcGVzIjpbXSwic2lkIjoiYWVlMjRiNDAtYmViMS00NmJhLTk3YTktNDkwMjcwMDhiMzVmIiwic3ViIjoiZDVkNmJhNjUtNjBjMS00Yzc4LTkyZDYtZGZkMjc0ZDQwYjdlIiwidWlkIjoiMzMwNjA1OCIsInV0eXBlIjoiY3VzdG9tZXIifQ.Y2VOUYa7FNQQO2VZ4VkyG47eutyeEWJf6BaO3oswBf5saEXmjgM1g2IVYd2diisaV3lHJi3n7HUyE9OPNm5j2Ns5BOvs7H3sFI0zcYwD1fKKGdOuYXf0WEW9EpdnC_CfqPYIMPrBfqrmMP3w0Sa7blZocAUyoQ8rV6VVd-bzGo8")
        newRequest.addHeader("X-User-Type", "customer")
        newRequest.addHeader("X-DeviceOS", "Android,11")
        newRequest.addHeader("User-uuid", "3306058")
        newRequest.addHeader("X-DeviceToken", "eB_V64XtRMamnLfDd2BEGJ:APA91bFz0TW5o3x6ycFHp48Nt8TJruTJ5JRzOX_Bdd9uJfp_28jnfzmm6Q9BYmuDjhpIYBwjvKu-5KpZQ5GW8xNznpX7_255R5IoEzgxFjyJ9DeuNPyUHFO-yjEwTW9q40KRx38veBox")
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
