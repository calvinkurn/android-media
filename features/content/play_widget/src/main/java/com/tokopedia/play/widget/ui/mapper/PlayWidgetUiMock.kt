package com.tokopedia.play.widget.ui.mapper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.tokopedia.play.widget.PlayWidgetUiModel
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.data.PlayWidgetResponse
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
        return PlayWidgetUiMapper.mapWidget(response.playWidget)
    }
}