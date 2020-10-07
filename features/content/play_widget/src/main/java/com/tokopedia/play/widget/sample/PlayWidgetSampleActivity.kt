package com.tokopedia.play.widget.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.play.widget.PlayWidgetUiModel
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetCardItemType
import com.tokopedia.play.widget.ui.type.PlayWidgetCardSize
import com.tokopedia.play.widget.ui.type.PlayWidgetCardType
import kotlin.random.Random

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSampleActivity : BaseSimpleActivity() {

    private val rvWidgetSample by lazy { findViewById<RecyclerView>(R.id.rv_widget_sample) }

    private val adapter = PlayWidgetSampleAdapter()

    private val cardItemTypeRandom = Random(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_widget_sample)
        setupView()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun setupView() {
        rvWidgetSample.adapter = adapter
        adapter.setWidgets(getSampleData())
        adapter.notifyDataSetChanged()
    }

    private fun getSampleData(): List<PlayWidgetUiModel> {
        return listOf(
                getSamplePlayWidgetUiModel(PlayWidgetCardSize.Small),
                getSamplePlayWidgetUiModel(PlayWidgetCardSize.Medium)
        )
    }

    private fun getSamplePlayWidgetUiModel(size: PlayWidgetCardSize): PlayWidgetUiModel {
        return PlayWidgetUiModel(
                title = "Yuk Nonton Sekarang!",
                actionTitle = "Lihat semua",
                actionAppLink = "",
                actionWebLink = "",
                background = PlayWidgetBackgroundUiModel(
                        overlayImageUrl = "https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg",
                        overlayImageAppLink = "",
                        overlayImageWebLink = "",
                        gradientColors = emptyList(),
                        backgroundUrl = "https://www.publicdomainpictures.net/pictures/320000/velka/background-image.png"
                ),
                config = PlayWidgetConfigUiModel(
                        autoRefresh = false,
                        autoRefreshTimer = 0L,
                        autoPlay = true,
                        autoPlayAmount = 2000L,
                        maxAutoPlayCard = 2
                ),
                items = getSampleCardData(),
                size = size
        )
    }

    private fun getSampleCardData(): List<PlayWidgetCardUiModel> {
        return List(5) { getSamplePlayWidgetCardUiModel() }
    }

    private fun getSamplePlayWidgetCardUiModel(): PlayWidgetCardUiModel {
        return PlayWidgetCardUiModel(
                type = PlayWidgetCardType.Channel,
                card = getSamplePlayWidgetCardItemUiModel(
                        when (cardItemTypeRandom.nextInt(0, 3)) {
                            0 -> PlayWidgetCardItemType.Upcoming
                            1 -> PlayWidgetCardItemType.Vod
                            else -> PlayWidgetCardItemType.Live
                        }
                )
        )
    }

    private fun getSamplePlayWidgetCardItemUiModel(cardItemType: PlayWidgetCardItemType): PlayWidgetCardItemUiModel {
        return PlayWidgetCardItemUiModel(
                channelId = "123",
                title = "Google Assistant review with me",
                cardType = cardItemType,
                backgroundUrl = "https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg",
                appLink = "",
                webLink = "",
                startTime = "",
                totalView = "10,0 rb",
                totalViewVisible = true,
                hasPromo = true,
                activeReminder = true,
                isLive = cardItemType == PlayWidgetCardItemType.Live,
                partner = PlayWidgetPartnerUiModel("123", "Google"),
                video = PlayWidgetCardVideoUiModel(
                        id = "123",
                        coverUrl = "https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg",
                        videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                )
        )
    }
}