package com.tokopedia.play.broadcaster.mocker

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.domain.model.Configuration
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import kotlin.random.Random

/**
 * Created by jegul on 20/05/20
 */
object PlayBroadcastMocker {

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
                stock = (it % 2) * 10,
                isSelectable = { Selectable }
        )
    }

    fun getMaxSelectedProduct() = 2

    fun getMockSearchSuggestions(keyword: String) = List(keyword.length) {
        val suggestionText = " ${keyword.substring(0, it + 1)}"
        val fullText = "$keyword$suggestionText"
        SearchSuggestionUiModel(
                queriedText = keyword,
                suggestedText = fullText,
                spannedSuggestion = SpannableStringBuilder(fullText).apply {
                    setSpan(StyleSpan(Typeface.BOLD), fullText.indexOf(suggestionText), fullText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
        )
    }

    fun getMockConfiguration() = Configuration(
            isUserWhitelisted = true,
            isHaveOnGoingLive = false,
            isOfficial = false,
            channelId = "1",
            maxTaggedProduct = 15,
            maxLiveStreamDuration = (1000 * 60) * 6,
            countDownDuration = 10
    )

    fun getMockActiveChannel() = ChannelInfoUiModel(
            channelId = "1234",
            title = "Klarififikasi Bisa Tebak Siapa?",
            description = "Yuk gabung sekarang di Play Klarifikasi Bisa Tebak siapa?",
            coverUrl = "https://ecs7.tokopedia.net/defaultpage/banner/bannerbelanja1000.jpg",
            ingestUrl = "rtmp://test",
            shareUrl = "https://www.tokopedia.com/play/channels/1234",
            status = PlayChannelStatus.Active
    )

    fun getMockTotalView() = TotalViewUiModel(
            totalView = "1234"
    )

    fun getMockTotalLike() = TotalLikeUiModel(
            totalLike = "1234"
    )

    fun getLiveStreamingInfo() = LiveStreamInfoUiModel(
            "1234",
            ingestUrl = "rtmp://test",
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

    fun getSummary(): SummaryUiModel {
        val tickerContent = SummaryUiModel.TickerContent("Live Streaming Berakhir", "Waktu live streaming kamu sudah 30 menit", true)
        return SummaryUiModel(coverImage = "",
                tickerContent = tickerContent,
                liveTitle = "Sneakers Hypebeast with Cashback 10%",
                liveDuration = "28:42",
                finishRedirectUrl = "")
    }

}