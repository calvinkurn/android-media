package com.tokopedia.play.widget.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import kotlin.random.Random

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSampleActivity : BaseSimpleActivity() {

    private val rvWidgetSample by lazy { findViewById<RecyclerView>(R.id.rv_widget_sample) }

    private lateinit var adapter: PlayWidgetSampleAdapter

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
        val sampleData = getSampleData()
        val coordinatorMap = List(sampleData.size) {
            PlayWidgetCoordinator(this).apply {
                setAnalyticListener(PlayWidgetSampleAnalytic(this@PlayWidgetSampleActivity))
            }
        }.associateWith { null }

        adapter = PlayWidgetSampleAdapter(coordinatorMap)

        rvWidgetSample.adapter = adapter
        adapter.setItemsAndAnimateChanges(getSampleData())
    }

    private fun getSampleData(): List<PlayWidgetUiModel> {
        return listOf(
                PlayWidgetUiModel.Placeholder,
                PlayWidgetUiMock.getPlayWidgetSmallVideo(this),
                PlayWidgetUiMock.getPlayWidgetMedium(this)
        )
    }

    private fun getSamplePlaySmallWidget(): PlayWidgetUiModel {
        return PlayWidgetUiModel.Small(
                title = "Yuk Nonton Sekarang!",
                actionTitle = "Lihat semua",
                actionAppLink = "",
                actionWebLink = "",
                config = PlayWidgetConfigUiModel(
                        autoRefresh = false,
                        autoRefreshTimer = 0L,
                        autoPlay = true,
                        autoPlayAmount = 2,
                        maxAutoPlayCellularDuration = 2,
                        maxAutoPlayWifiDuration = 30
                ),
                items = getSampleSmallCardData(),
                useHeader = true
        )
    }

    private fun getSamplePlayMediumWidget(): PlayWidgetUiModel {
        return PlayWidgetUiModel.Medium(
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
                        autoPlayAmount = 2,
                        maxAutoPlayCellularDuration = 2,
                        maxAutoPlayWifiDuration = 30
                ),
                items = getSampleMediumCardData()
        )
    }

    private fun getSampleSmallCardData(): List<PlayWidgetSmallItemUiModel> {
        return List(5) {
            if (it == 0) getSampleSmallCardBanner()
            else {
                val channelType = when (cardItemTypeRandom.nextInt(0, 4)) {
                    0 -> PlayWidgetChannelType.Upcoming
                    1 -> PlayWidgetChannelType.Vod
                    else -> PlayWidgetChannelType.Live
                }
                getSampleSmallChannelCardBanner(channelType)
            }
        }
    }

    private fun getSampleMediumCardData(): List<PlayWidgetMediumItemUiModel> {
        return List(5) {
            if (it == 0) getSampleMediumCardBanner()
            else {
                val channelType = when (cardItemTypeRandom.nextInt(0, 4)) {
                    0 -> PlayWidgetChannelType.Upcoming
                    1 -> PlayWidgetChannelType.Vod
                    else -> PlayWidgetChannelType.Live
                }
                getSampleMediumChannelCardBanner(channelType)
            }
        }
    }

    private fun getSampleSmallCardBanner() = PlayWidgetSmallBannerUiModel(
            imageUrl = "https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg",
            appLink = "",
            webLink = ""
    )

    private fun getSampleSmallChannelCardBanner(channelType: PlayWidgetChannelType) = PlayWidgetSmallChannelUiModel(
            channelId = "123",
            title = "Google Assistant review with me",
            channelType = channelType,
            appLink = "",
            webLink = "",
            startTime = "",
            totalView = "10,0 rb",
            totalViewVisible = true,
            hasPromo = cardItemTypeRandom.nextBoolean(),
            video = getVideoUiModel(channelType)
    )

    private fun getSampleMediumCardBanner() = PlayWidgetMediumBannerUiModel(
            imageUrl = "https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg",
            appLink = "",
            webLink = "",
            partner = PlayWidgetPartnerUiModel("123", "Google")
    )

    private fun getSampleMediumChannelCardBanner(channelType: PlayWidgetChannelType) = PlayWidgetMediumChannelUiModel(
            channelId = "123",
            title = "Google Assistant review with me",
            channelType = channelType,
            appLink = "",
            webLink = "",
            startTime = "",
            totalView = "10,0 rb",
            totalViewVisible = true,
            hasPromo = cardItemTypeRandom.nextBoolean(),
            activeReminder = cardItemTypeRandom.nextBoolean(),
            partner = PlayWidgetPartnerUiModel("123", "Google"),
            video = getVideoUiModel(channelType)
    )

    private fun getVideoUiModel(channelType: PlayWidgetChannelType) = PlayWidgetVideoUiModel(
            id = "123",
            coverUrl = "https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isLive = channelType == PlayWidgetChannelType.Live
    )
}