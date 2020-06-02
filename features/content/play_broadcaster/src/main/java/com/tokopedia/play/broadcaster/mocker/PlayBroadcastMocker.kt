package com.tokopedia.play.broadcaster.mocker

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.model.Configuration
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.state.Selectable

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
        PlayEtalaseUiModel(
                id = it + 1L,
                name = "Etalase ${it + 1}",
                productMap = mutableMapOf(),
                totalProduct = (it + 1) * 100
        )
    }

    fun getMockProductList(itemCount: Int) = List(itemCount) {
        ProductUiModel(
                id = 12345L + it,
                name = "Product ${it + 1}",
                imageUrl = when (it) {
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
                suggestionText = suggestionText,
                spannedSuggestion = SpannableStringBuilder(fullText).apply {
                    setSpan(StyleSpan(Typeface.BOLD), fullText.indexOf(suggestionText), fullText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
        )
    }

    fun getMockConfiguration() = Configuration(
            isUserWhitelisted = true,
            isHaveOnGoingLive = false,
            isOfficial = false,
            channelId = "",
            maxTaggedProduct = 15,
            maxLiveStreamDuration = (60*1000)*30,
            countDownDuration = 10
    )

    fun getMockActiveChannel() = ChannelInfoUiModel(
            channelId = "1234",
            ingestUrl = "rtmp://test",
            shareUrl = "tokopedia://play/2214",
            status = PlayChannelStatus.Active
    )

    fun getMockTotalView() = TotalViewUiModel(
            totalView = "1234"
    )

    fun getMockTotalLike() = TotalLikeUiModel(
            totalLike = "1234"
    )

    fun getSummary(): SummaryUiModel {
        val infos = List(7) { SummaryUiModel.LiveInfo("", "Description $it", (it * 30).toString()) }
        val tickerContent = SummaryUiModel.TickerContent("Live Streaming Berakhir", "Waktu live streaming kamu sudah 30 menit", true)
        return SummaryUiModel(coverImage = "",
                tickerContent = tickerContent ,
                liveTitle = "Sneakers Hypebeast with Cashback 10%",
                liveDuration = "28:42", liveInfos = infos, finishRedirectUrl = "")
    }
}