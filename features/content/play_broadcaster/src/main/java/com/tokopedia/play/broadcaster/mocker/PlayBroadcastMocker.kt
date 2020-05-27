package com.tokopedia.play.broadcaster.mocker

import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.model.Configuration
import com.tokopedia.play.broadcaster.view.uimodel.*

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
                id = 987L + it,
                name = "Etalase ${it + 1}",
                productList = getMockProductList((it % 4) + 1),
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
                isSelected = false,
                stock = (it % 2) * 10
        )
    }

    fun getMaxSelectedProduct() = 15

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
            ingestUrl = "rtmp://192.168.0.105/live/ByNDo1ds8",
            shareUrl = "tokopedia://play/2214",
            status = PlayChannelStatus.Active
    )
}