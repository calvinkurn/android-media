package com.tokopedia.play.widget.ui.mapper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.data.PlayWidgetResponse
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader


/**
 * Created by mzennis on 06/10/20.
 */
object PlayWidgetUiMock {

    @Throws(JsonSyntaxException::class, JsonIOException::class)
    fun getPlayWidgetMedium(context: Context): PlayWidgetUiModel {
        val reader: Reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.play_widget_v2_medium)))
        val gson = Gson()
        val response = gson.fromJson(reader, PlayWidgetResponse::class.java)
        return PlayWidgetMediumUiMapper(PlayWidgetConfigMapper(), PlayWidgetVideoMapper()).mapWidget(response.playWidget)
    }

    @Throws(JsonSyntaxException::class, JsonIOException::class)
    fun getPlayWidgetSmall(context: Context): PlayWidgetUiModel {
        val reader: Reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.play_widget_v2_small)))
        val gson = Gson()
        val response = gson.fromJson(reader, PlayWidgetResponse::class.java)
        return PlayWidgetSmallUiMapper(PlayWidgetConfigMapper(), PlayWidgetVideoMapper()).mapWidget(response.playWidget)
    }

    @Throws(JsonSyntaxException::class, JsonIOException::class)
    fun getPlayWidgetSmallVideo(context: Context): PlayWidgetUiModel {
        val reader: Reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.play_widget_v2_small)))
        val gson = Gson()
        val response = gson.fromJson(reader, PlayWidgetResponse::class.java)
        val model = PlayWidgetSmallUiMapper(PlayWidgetConfigMapper(), PlayWidgetVideoMapper()).mapWidget(response.playWidget) as PlayWidgetUiModel.Small
        val newModel = model.copy(
                items = model.items.mapIndexed { index, item ->
                    if (item is PlayWidgetSmallChannelUiModel) item.copy(
                            video = item.video.copy(
                                    videoUrl = if (index % 2 == 0) "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                                    else "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                            )
                    ) else item
                }
        )
        return newModel
    }
}