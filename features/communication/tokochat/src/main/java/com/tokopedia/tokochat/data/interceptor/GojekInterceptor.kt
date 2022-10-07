package com.tokopedia.tokochat.data.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class GojekInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request.Builder = chain.request().newBuilder()

        newRequest.addHeader("X-AppVersion", "4.53.0")
        newRequest.addHeader("X-AppId", "com.gojek.app.staging")
        newRequest.addHeader("Accept", "application/json")
        newRequest.addHeader("Content-Type", "application/json")
        newRequest.addHeader("X-UniqueId", "c9866dcf50f6f8d1")
        newRequest.addHeader("D1", "C6:F2:C8:87:39:66:4F:F2:F8:BF:96:6D:1F:EB:70:99:80:FF:DF:C5:ED:3E:8B:02:BE:75:BB:56:3C:D9:3B:06")
        newRequest.addHeader("X-Session-ID", "f537b8d7-9d92-4c3f-803b-e382a84b3882")
        newRequest.addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJhdWQiOlsiZ29qZWsiLCJtaWR0cmFucyIsImdvdmlldCIsImdvcGF5IiwiZ29wbGF5Il0sImRhdCI6eyJhY3RpdmUiOiJ0cnVlIiwiYmxhY2tsaXN0ZWQiOiJmYWxzZSIsImNvdW50cnlfY29kZSI6Iis2MiIsImNyZWF0ZWRfYXQiOiIyMDIyLTA5LTEyVDA2OjUwOjI0WiIsImVtYWlsIjoia2VsdmluLnNhcHV0cmFAdG9rb3BlZGlhLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjoidHJ1ZSIsImdvcGF5X2FjY291bnRfaWQiOiIwMS0zODE2MmUzNmE3Zjc0Y2E3ODVhYmQwNWFhNTk1NDc4ZC0zOCIsIm5hbWUiOiJLZWx2aW4iLCJudW1iZXIiOiI4NTE1NjM2NDY5NSIsInBob25lIjoiKzYyODUxNTYzNjQ2OTUiLCJzaWduZWRfdXBfY291bnRyeSI6IklEIiwid2FsbGV0X2lkIjoiMjIyNTUwNDEwMjQwMDIyMTM1In0sImV4cCI6MTY2ODIxNzUyNCwiaWF0IjoxNjY1MTQ0NDMwLCJpc3MiOiJnb2lkIiwianRpIjoiMTM5ZGY3ZDEtNWM5MS00NjU5LWFjYTktZGYyZjczMTY2NDA5Iiwic2NvcGVzIjpbXSwic2lkIjoiZmM5YTBkYjctOGI4Mi00NTAzLTgxZGMtYmNlMGM2Yjc2ZjgwIiwic3ViIjoiZDVkNmJhNjUtNjBjMS00Yzc4LTkyZDYtZGZkMjc0ZDQwYjdlIiwidWlkIjoiMzMwNjA1OCIsInV0eXBlIjoiY3VzdG9tZXIifQ.V47uCYr-mJArTBIj9oXep9p_0NGosm9p6ahhzgHITRS5srdQKmdpP42Uq_vAH9gwxs3d69BHzHVVQmgkE5QHw4swLrQB5kGylwu6Y1_toBtgchu8R3itT8Kq8M4TAexpatfaEoTpMWI2FPoELGAe8weRrnUWUTVZl5OXDW-q1Rc")
        newRequest.addHeader("X-User-Type", "customer")
        newRequest.addHeader("X-DeviceOS", "Android,11")
        newRequest.addHeader("User-uuid", "3306058")
        newRequest.addHeader("X-DeviceToken", "eB_V64XtRMamnLfDd2BEGJ:APA91bFz0TW5o3x6ycFHp48Nt8TJruTJ5JRzOX_Bdd9uJfp_28jnfzmm6Q9BYmuDjhpIYBwjvKu-5KpZQ5GW8xNznpX7_255R5IoEzgxFjyJ9DeuNPyUHFO-yjEwTW9q40KRx38veBox")
        newRequest.addHeader("X-PhoneMake", "realme")
        newRequest.addHeader("X-PushTokenType", "FCM")
        newRequest.addHeader("X-PhoneModel", "realme,RMX2156")
        newRequest.addHeader("Accept-Language", "en-ID")
        newRequest.addHeader("X-User-Locale", "en_ID")
        newRequest.addHeader("X-Location", "6.221285,106.819455")
        newRequest.addHeader("X-Location-Accuracy", "20.0")
        newRequest.addHeader("Gojek-Country-Code", "ID")
        newRequest.addHeader("Gojek-Service-Area", "1")
        newRequest.addHeader("Gojek-Timezone", "Asia/Jakarta")
        newRequest.addHeader("X-M1", "1:__80d16f6a8e7649e686760a0322485101,2:UNKNOWN,3:1662962508197-1187937468634658796,4:5951,5:|UNKNOWN|4,6:02:15:B2:00:00:00,7:,8:1080x1794,9:passive\\,gps\\,network,10:0,11:DQYk7k7RbZEIc0slR+8Sv/HJqWCMEGDIrhMjTFol1uw=,12:4a3e7b171fea240796a497890e29e85882bd7dcc,13:BXtKxJOsHH9VDlg8dim+Am+/lp8aoRmag518tneztILjaGRnHdDd1a0OpFW4M6V2NTJW2OMJEuL5I/15MCJWsH7sURZVskSoeMAxS3nm5RqzZz+bHOJwXSMBQ5b3T7RgUNEtx/CuSLz22vwTL1W3MfeTuctgAzCeS3fnydejSejW7/UWPfjrlRt/2e9XSx0AdKKql5QB7Qx+gBxuJ/NTJYnWpn1LE1zGgVOg0bVs3/i09wCIWocDZmFv9kXj0BxbC+dMAA/jXoWfdVNTnbDNkR60rVdYMQm3LrEyz92V6pffv4/+D3YQmCOm8HqWOKlsgA+H55KtjsXjoE7XWjNRaQ==,14:1665145084")
        newRequest.addHeader("Host", "integration-api.gojekapi.com")
        newRequest.addHeader("Connection", "Keep-Alive")
        newRequest.addHeader("Accept-Encoding", "gzip")
        newRequest.addHeader("User-Agent", "okhttp/3.12.13")
        newRequest.addHeader("If-Modified-Since", "Mon, 29 Aug 2022 08:16:25 GMT")

        return chain.proceed(newRequest.build())
    }
}
