package com.tokopedia.sellerhomecommon.sse

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerhomecommon.sse.model.WidgetSSEModel
import com.tokopedia.sse.OkSse
import com.tokopedia.sse.ServerSentEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

/**
 * Created by @ilhamsuaib on 10/10/22.
 */

class SellerHomeWidgetSSEImpl(
    private val dispatchers: CoroutineDispatchers
) : SellerHomeWidgetSSE {

    companion object {
        private const val URL =
            "https://sse.tokopedia.com/seller-dashboard/sse/datakeys?page=%s&datakeys=%s"
        private const val DATA_KEY_SEPARATOR = ","
        private const val HEADER_ORIGIN = "Origin"
        private const val HEADER_AUTHORIZATION = "Accounts-Authorization"
        private const val HEADER_X_DEVICE = "X-Device"
        private const val BEARER = "Bearer %s"
        private const val ANDROID_VERSION = "android-%s"
    }

    private var sse: ServerSentEvent? = null
    private var sseFlow = MutableSharedFlow<WidgetSSEModel>(extraBufferCapacity = 100)

    override fun connect(page: String, dataKeys: List<String>) {
        val dataKey = dataKeys.joinToString(DATA_KEY_SEPARATOR)
        val url = String.format(URL, page, dataKey)

        val request = Request.Builder().get().url(url)
            .addHeader("Authority", "sse.tokopedia.com")
            .addHeader(
                "Cookie",
                "_UUID_NONLOGIN_=e79c500ae23a2d7d7613f77aae8605bd; _UUID_NONLOGIN_.sig=mvMUKT7gwFmzns9AOdnncEoTUe8; DID=0dd79c74472d9d5e9874e628528422c19996417cf0de2e3c62c04e4ec2f58bf1f1e6d683009b8380acc03f3b1193b5c3; DID_JS=MGRkNzljNzQ0NzJkOWQ1ZTk4NzRlNjI4NTI4NDIyYzE5OTk2NDE3Y2YwZGUyZTNjNjJjMDRlNGVjMmY1OGJmMWYxZTZkNjgzMDA5YjgzODBhY2MwM2YzYjExOTNiNWMz47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=; __auc=f13d976817d56d7c068aba8140a; _fbp=fb.1.1637842604998.636941336; _UUID_CAS_=ce0e63fb-738d-4e5c-a72b-61e3606a3687; _hjSessionUser_714968=eyJpZCI6ImFlYzRjNWVmLWFlMjEtNTAxMC1hZjVkLTE2NzVmZmFjNmViZSIsImNyZWF0ZWQiOjE2Mzg1MzM2MjczMjAsImV4aXN0aW5nIjpmYWxzZX0=; _fbc=fb.1.1641051463550.IwAR3HXT6_15pSXA1LyfnSLN3-1HBPrBeiIMp452j7QAG9rZi3AAbVmSph43A; cto_bundle=fLbz-l9DWWlPZEp1aEZYYUFCUHB4Q2tqRjhwU1RRZDBiUGg2biUyQkxmSGFXZWlLejdWdlZlWiUyQnVWOVVxZlNQRUlzb083ZWRWMVRxU0NCQWlOazJQSkNGYnlzSlVGN05IbXBndTMzVyUyQnpabm1QSktXTWxpazljNzlFcE1veDRIV2EybUVIc09xNnp6OXFReVRsY3NGYXNSRjJ4bnclM0QlM0Q; amplitude_id_122e675cf4df83d2c6a5381c86188b7ftokopedia.com=eyJkZXZpY2VJZCI6ImFkYzU3ZTExLTU2MTYtNDk2Zi04OTA4LWM0YzQ0YzFjNjNjZlIiLCJ1c2VySWQiOm51bGwsIm9wdE91dCI6ZmFsc2UsInNlc3Npb25JZCI6MTY1ODE5ODI5Mzk1OCwibGFzdEV2ZW50VGltZSI6MTY1ODE5ODI5Mzk1OCwiZXZlbnRJZCI6MCwiaWRlbnRpZnlJZCI6MCwic2VxdWVuY2VOdW1iZXIiOjB9; _UUID_CAS_S=9de9f415-52fc-4938-8dfc-a4f787f1bd46; _jxx=03101360-f2cf-11ec-ac57-a9f8d047ef42; _jx=03101360-f2cf-11ec-ac57-a9f8d047ef42; _gcl_au=1.1.626904221.1662106652; _SID_Tokopedia_Coba_=KF4KiLVVXs6oO0tflQgASN9LLi9SOWIL0SwgT7MKAj7JYFV_WkjxLa5ONdkbT8uvuXfjdVQxN9z7nAOXCNxUUBpCU_iip46XZ5KyPd4nTVmJZYVYImGhqFLd7uSp8rOB; l_coba=1; FPF_staging=1; aus_staging=1; _gcl_aw=GCL.1662875922.Cj0KCQjw6_CYBhDjARIsABnuSzon1SeY8bT7Rci1kUJ8A2851QmJzFdA-bANYswoM2VCySOyAGsVrhMaAvTxEALw_wcB; _gcl_dc=GCL.1662875922.Cj0KCQjw6_CYBhDjARIsABnuSzon1SeY8bT7Rci1kUJ8A2851QmJzFdA-bANYswoM2VCySOyAGsVrhMaAvTxEALw_wcB; _gac_UA-126956641-6=1.1662875923.Cj0KCQjw6_CYBhDjARIsABnuSzon1SeY8bT7Rci1kUJ8A2851QmJzFdA-bANYswoM2VCySOyAGsVrhMaAvTxEALw_wcB; _gac_UA-9801603-1=1.1662875928.Cj0KCQjw6_CYBhDjARIsABnuSzon1SeY8bT7Rci1kUJ8A2851QmJzFdA-bANYswoM2VCySOyAGsVrhMaAvTxEALw_wcB; webauthn-session=74ca0a5d-f044-4ccc-a52e-4006beb9c0be; _abck=A74C25C8C576CCEAE16667BAC2ED9C7E~0~YAAQve84F3wXDFWDAQAAE+JKZAhasP5wikwFNEDfqEEp6AcmQYS8OgXYHblI9JkofYA/guh9wm4+jqvm1WjSDIF2yE0getEYUwIF/OIfZp5H27lvmokm98mH0dYPHTER5j/T3LBQYuNUCQbqERhoEjoAqIlLbHGyYfXf09n7m8A1yS5G7EHm78H4R7xDEBh7zGtWZ3qw1SUP235IfPqhHMdsTRjCWoeCB3XKbKTvNweCDMejXi6oJWiNh5LtTTDkx8WT1UT7uiFDN7Xq41+pRZr8D1kO30S5ChDMttGb5z5D61e9w/N0bH9P6PDonv8dmPa780LgpOwNQ0+QRc1vDRv2SrtZXyp1M0eCqvXd36J+KuahMVcskncDpc5qE/Ixlt9Did6E4EhnG0pfI6X3Gu4SQ6k/y9D1rl04~-1~-1~-1; _gid=GA1.2.186166231.1665466648; _SID_Tokopedia_=rmzJLN9oDNxj6oGiu2ipjCcBOM_iaoPiyNDo5ki_vpoxGadVWBTbpFKLUlT7uFO8JNG3f19vK2HpZPj_lTRri6WGWxLS5b9vmI26fwCGg822Mzswu6nTnSgbokTRnWwC; shipping_notif=0; g_yolo_production=1; __asc=14ea4f4d183c58c8032d9cea499; l=1; FPF=1; tuid=12299749; uide=wMmu1AkFYqi03TogTQwhmdIFz3ZCIkLrwCHpIMsAyJXosAi4; uidh=v8JQaDz8hySQNgqpr4/opZTz4BJtEs7Nfx6UUANjfpM=; _ga_70947XW48P=GS1.1.1665466647.209.1.1665466674.33.0.0; _ga=GA1.2.959118774.1637586544; aus=1"
            )
            .build()

        close()
        sse = OkSse().newServerSentEvent(request, object : ServerSentEvent.Listener {

            override fun onOpen(sse: ServerSentEvent, response: Response) {
                Timber.d("SSE Listener : onOpen")
            }

            override fun onMessage(
                sse: ServerSentEvent,
                id: String,
                event: String,
                message: String
            ) {
                Timber.d("SSE Listener : onMessage -> $event")
                sseFlow.tryEmit(WidgetSSEModel(event = event, message = message))
            }

            override fun onComment(sse: ServerSentEvent, comment: String) {
                Timber.d("SSE Listener : onComment")
            }

            override fun onRetryTime(sse: ServerSentEvent, milliseconds: Long): Boolean {
                Timber.d("SSE Listener : onRetryTime : $milliseconds")
                return true
            }

            override fun onRetryError(
                sse: ServerSentEvent,
                throwable: Throwable,
                response: Response?
            ): Boolean {
                Timber.d("SSE Listener : onRetryError : ${throwable.message}")
                return true
            }

            override fun onClosed(sse: ServerSentEvent) {
                Timber.d("SSE Listener : onClosed")
            }

            override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request {
                Timber.d("SSE Listener : onPreRetry")
                return request
            }
        })
    }

    override fun close() {
        sse?.close()
    }

    override fun listen(): Flow<WidgetSSEModel> {
        return sseFlow.filterNotNull().buffer().flowOn(dispatchers.io)
    }
}
