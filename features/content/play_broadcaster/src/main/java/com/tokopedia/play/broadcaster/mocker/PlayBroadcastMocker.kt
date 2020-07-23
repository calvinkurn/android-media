package com.tokopedia.play.broadcaster.mocker

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import kotlin.random.Random

/**
 * Created by jegul on 20/05/20
 */
object PlayBroadcastMocker {

    const val LOCAL_RTMP_URL: String = "rtmp://192.168.0.110:1935/stream/"

    /**
     * Follower
     */
    fun getMockUnknownFollower() = List(3) {
        FollowerUiModel.Unknown(when (it) {
            0 -> R.color.play_follower_orange
            1 -> R.color.play_follower_blue
            else -> R.color.play_follower_yellow
        })
    }

    fun getMockUserFollower() = List(3) { FollowerUiModel.User("https://www.tokopedia.com") }

    /**
     * Etalase
     */
    fun getMockEtalaseList() = List(6) {
        EtalaseContentUiModel(
                id = (it + 1L).toString(),
                name = "Etalase ${it + 1}",
                productMap = mutableMapOf(),
                totalProduct = (it + 1) * 100,
                stillHasProduct = false
        )
    }

    fun getMockProductList(itemCount: Int) = List(itemCount) {
        ProductContentUiModel(
                id = 12345L + it,
                name = "Product ${it + 1}",
                imageUrl = when (it) {
                    1 -> "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/oyhemtbkghuegy9gpo0i/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                    2 -> "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/gueo3qthwrv8y5laemzs/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                    3 -> "https://static.nike.com/a/images/t_PDP_864_v1/f_auto,b_rgb:f5f5f5,q_80/rofxpoxehp6wznvzb1jk/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                    else -> "https://static.nike.com/a/images/t_PDP_864_v1/f_auto,b_rgb:f5f5f5,q_80/udglgfg9ozu3erd3fubg/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                },
                originalImageUrl = when (it) {
                    1 -> "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/oyhemtbkghuegy9gpo0i/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                    2 -> "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/gueo3qthwrv8y5laemzs/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                    3 -> "https://static.nike.com/a/images/t_PDP_864_v1/f_auto,b_rgb:f5f5f5,q_80/rofxpoxehp6wznvzb1jk/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                    else -> "https://static.nike.com/a/images/t_PDP_864_v1/f_auto,b_rgb:f5f5f5,q_80/udglgfg9ozu3erd3fubg/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                },
                isSelectedHandler = { false },
                stock = StockAvailable((it % 2) * 10),
                isSelectable = { Selectable }
        )
    }

    fun getMaxSelectedProduct() = 20

    fun getMockSearchSuggestions(keyword: String) = List(keyword.length) {
        val suggestionText = " ${keyword.substring(0, it + 1)}"
        val fullText = "$keyword$suggestionText"
        SearchSuggestionUiModel(
                queriedText = keyword,
                suggestedId = "1",
                suggestedText = fullText,
                spannedSuggestion = SpannableStringBuilder(fullText).apply {
                    setSpan(StyleSpan(Typeface.BOLD), fullText.indexOf(suggestionText), fullText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
        )
    }

    fun getMockConfigurationDraftChannel() = ConfigurationUiModel(
            streamAllowed = true,
            channelType = ChannelType.Draft,
            channelId = "10008", // 10008 prod, 10012 stag (status: draft)
            remainingTime = (30 * 60 * 1000),
            timeElapsed = "01:00",
            durationConfig = DurationConfigUiModel(
                    duration = (30 * 60 * 1000),
                    maxDurationDesc = "Siaran 30 menit",
                    pauseDuration = (1 * 60 * 1000),
                    errorMessage = "Maks. siaran 30 menit"
            ),
            productTagConfig = ProductTagConfigUiModel(
                    maxProduct = 15,
                    minProduct = 1,
                    maxProductDesc = "Maks. Produk 15",
                    errorMessage = "Oops, kamu sudah memilih 15 produk"
            ),
            coverConfig = CoverConfigUiModel(
                    maxChars = 38
            ),
            countDown = 5
    )

    private fun getMockChannel(status: PlayChannelStatus) = ChannelInfoUiModel(
            channelId = "1234",
            title = "Klarififikasi Bisa Tebak Siapa?",
            description = "Yuk gabung sekarang di Play Klarifikasi Bisa Tebak siapa?",
            coverUrl = "https://ecs7.tokopedia.net/defaultpage/banner/bannerbelanja1000.jpg",
            ingestUrl = LOCAL_RTMP_URL,
            status = status
    )

    fun getMockTotalView() = TotalViewUiModel(
            totalView = "1234"
    )

    fun getMockTotalLike() = TotalLikeUiModel(
            totalLike = "1234"
    )

    fun getLiveStreamingInfo() = LiveStreamInfoUiModel(
            "1234",
            ingestUrl = LOCAL_RTMP_URL,
            streamUrl = "rtmp://test"
    )

    fun getMockChat(): PlayChatUiModel {
        val name = listOf("Aku", "Kamu", "Dia", "Mereka").random()
        return PlayChatUiModel(
                messageId = System.currentTimeMillis().toString(),
                userId = Random.nextInt().toString(),
                name = name,
                message = listOf(":pepecry", ":pepelmao", ":lul").random(),
                isSelfMessage = false
        )
    }

    fun getMockMetric(): PlayMetricUiModel {
        val firstSentence = "${Random.nextInt(1, 10)} Penonton"
        val secondSentence = listOf("Bergabung", "Mengunjungi tokomu", "Melihat produkmu").random()
        val fullSentence = "$firstSentence $secondSentence"
        return PlayMetricUiModel(
                firstSentence = firstSentence,
                secondSentence = secondSentence,
                fullSentence = fullSentence,
                interval = 5000
        )
    }

    fun getMockShare() = ShareUiModel(
            id = "1234",
            title = "Tokopedia PLAY seru!",
            description = "Nonton siaran seru di Tokopedia PLAY!",
            imageUrl = "https://ecs7.tokopedia.net/defaultpage/banner/bannerbelanja1000.jpg",
            redirectUrl = "https://beta.tokopedia.com/play/channel/10140",
            textContent =  "\"testing\"\nYuk, nonton siaran dari MRG Audio di Tokopedia PLAY! Bakal seru banget lho!\n${'$'}{url}",
            shortenUrl = true
    )

    fun getMetricSummary(): List<TrafficMetricUiModel> = listOf(
            TrafficMetricUiModel(TrafficMetricsEnum.TotalViews, "2328"),
            TrafficMetricUiModel(TrafficMetricsEnum.VideoLikes, "1800"),
            TrafficMetricUiModel(TrafficMetricsEnum.ShopVisit, "1200"),
            TrafficMetricUiModel(TrafficMetricsEnum.ProductVisit, "1042"),
            TrafficMetricUiModel(TrafficMetricsEnum.NumberOfAtc, "320"),
            TrafficMetricUiModel(TrafficMetricsEnum.NumberOfPaidOrders, "200")
    )
}