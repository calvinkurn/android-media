package com.tokopedia.entertainment.pdp.data


import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventContentByIdEntity(
        @SuppressLint("Invalid Data Type")
        @SerializedName("event_content_by_id")
        @Expose
        val eventContentById : EventContentById = EventContentById()
)

data class EventContentById(
        @SerializedName("data")
        @Expose
        val data : ContentById = ContentById()
)

data class ContentById(
        @SerializedName("meta_description")
        @Expose
        val metaDescription: String = "",
        @SerializedName("meta_follow")
        @Expose
        val metaFollow: Int = 0,
        @SerializedName("meta_index")
        @Expose
        val metaIndex: Int = 0,
        @SerializedName("meta_title")
        @Expose
        val metaTitle: String = "",
        @SerializedName("section_data")
        @Expose
        val sectionData: List<SectionData> = emptyList(),
        @SerializedName("title")
        @Expose
        val title: String = ""
)

data class Content(
        @SerializedName("content_section_id")
        @Expose
        val contentSectionId: String = "",
        @SerializedName("content_type_id")
        @Expose
        val contentTypeId: String = "0",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("priority")
        @Expose
        val priority: Int = 0,
        @SerializedName("value_accordion")
        @Expose
        val valueAccordion: List<ValueAccordion> = emptyList(),
        @SerializedName("value_bullet_list")
        @Expose
        val valueBulletList: List<ValueBullet> = emptyList(),
        @SerializedName("value_carousel")
        @Expose
        val valueCarousel: List<Any> = emptyList(),
        @SerializedName("value_header_image")
        @Expose
        val valueHeaderImage: ValueHeaderImage,
        @SerializedName("value_image")
        @Expose
        val valueImage: ValueImage = ValueImage(),
        @SerializedName("value_image_bullet")
        @Expose
        val valueImageBullet: List<Any> = emptyList(),
        @SerializedName("value_label_bullet")
        @Expose
        val valueLabelBullet: List<Any> = emptyList(),
        @SerializedName("value_text")
        @Expose
        val valueText: String = ""
)

data class SectionData(
        @SerializedName("content")
        @Expose
        val content: List<Content> = emptyList(),
        @SerializedName("priority")
        @Expose
        val priority: Int = 0,
        @SerializedName("section")
        @Expose
        val section: String = ""
)

data class ValueAccordion(
        @SerializedName("content")
        @Expose
        val content: String = "",
        @SerializedName("title")
        @Expose
        val title: String = ""
)

data class ValueBullet(
        @SerializedName("bullet")
        @Expose
        val bullet: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("title")
        @Expose
        val title: String = ""
)

data class ValueHeaderImage(
        @SerializedName("columns")
        @Expose
        val columns: List<Any> = emptyList(),
        @SerializedName("footer_text")
        @Expose
        val footerText: String = "",
        @SerializedName("header_text")
        @Expose
        val headerText: String = ""
)

data class ValueImage(
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String = "",
        @SerializedName("url")
        @Expose
        val url: String = ""
)