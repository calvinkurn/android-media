package com.tokopedia.home_component.usecase.todowidget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.util.ChannelStyleUtil.parseBorderStyle
import com.tokopedia.home_component.util.ChannelStyleUtil.parseDividerSize
import com.tokopedia.home_component.util.ChannelStyleUtil.parseImageStyle
import com.tokopedia.home_component_header.model.ChannelHeader

/**
 * Created by frenzel
 */
class HomeTodoWidgetData {
    data class HomeTodoWidget(
        @SerializedName("getHomeToDoWidget")
        @Expose
        val getHomeTodoWidget: GetHomeTodoWidget = GetHomeTodoWidget(),
    )

    data class GetHomeTodoWidget(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),
        @SerializedName("toDos")
        @Expose
        val todos: List<Todo> = listOf(),
        @SerializedName("config")
        @Expose
        val config: Config = Config(),
    )

    data class Header(
        @SerializedName("title")
        @Expose
        val title: String = "",
    )

    data class Config(
        @SerializedName("styleParam")
        @Expose
        val styleParam: String = "",
        @SerializedName("dividerType")
        @Expose
        val dividerType: Int = 0,
    )

    data class Todo(
        @SerializedName("id")
        @Expose
        val id: Long = 0L,
        @SerializedName("dataSource")
        @Expose
        val dataSource: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("dueDate")
        @Expose
        val dueDate: String = "",
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",
        @SerializedName("contextInfo")
        @Expose
        val contextInfo: String = "",
        @SerializedName("price")
        @Expose
        val price: String = "",
        @SerializedName("discountPercentage")
        @Expose
        val discountPercentage: String = "",
        @SerializedName("slashedPrice")
        @Expose
        val slashedPrice: String = "",
        @SerializedName("applink")
        @Expose
        val cardApplink: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("feParam")
        @Expose
        val feParam: String = "",
        @SerializedName("cta")
        @Expose
        val cta: Cta = Cta(),
    )

    data class Cta(
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("mode")
        @Expose
        val mode: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("applink")
        @Expose
        val applink: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
    )
}
