package com.tokopedia.pdp.fintech.domain.datamodel

import com.google.gson.annotations.SerializedName

data class WidgetDetail(
    @SerializedName("list" ) var list : ArrayList<ChipList> = arrayListOf()
)


data class ChipList (

    @SerializedName("price" ) var price : Double?             = null,
    @SerializedName("title" ) var title : String?          = null,
    @SerializedName("chips" ) var chips : ArrayList<Chips> = arrayListOf()

)


data class Chips(
    @SerializedName("gateway_id"         ) var gatewayId        : Int?     = null,
    @SerializedName("name"               ) var name             : String?  = null,
    @SerializedName("product_code"       ) var productCode      : String?  = null,
    @SerializedName("is_active"          ) var isActive         : Boolean? = null,
    @SerializedName("is_disabled"        ) var isDisabled       : Boolean? = null,
    @SerializedName("tenure"             ) var tenure           : Int?     = null,
    @SerializedName("header"             ) var header           : String?  = null,
    @SerializedName("subheader"          ) var subheader        : String?  = null,
    @SerializedName("subheader_color"    ) var subheaderColor   : String?  = null,
    @SerializedName("product_icon_light" ) var productIconLight : String?  = null,
    @SerializedName("product_icon_dark"  ) var productIconDark  : String?  = null,
    @SerializedName("cta"                ) var cta              : Cta?     = Cta()
)

data class Cta(
    @SerializedName("type"        ) var type        : Int?         = null,
    @SerializedName("web_url"     ) var webUrl      : String?      = null,
    @SerializedName("android_url" ) var androidUrl  : String?      = null,
    @SerializedName("ios_url"     ) var iosUrl      : String?      = null,
    @SerializedName("bottomsheet" ) var bottomsheet : WidgetBottomsheet? = WidgetBottomsheet()
)

data class WidgetBottomsheet(
    @SerializedName("show"                        ) var show                     : Boolean?                = null,
    @SerializedName("product_icon_light"          ) var productIconLight         : String?                 = null,
    @SerializedName("product_icon_dark"           ) var productIconDark          : String?                 = null,
    @SerializedName("title"                       ) var title                    : String?                 = null,
    @SerializedName("buttons"                     ) var buttons                  : ArrayList<BottomSheetButtons>      = arrayListOf(),
    @SerializedName("descriptions"                ) var descriptions             : ArrayList<BottomSheetDescriptions> = arrayListOf(),
    @SerializedName("product_footnote"            ) var productFootnote          : String?                 = null,
    @SerializedName("product_footnote_icon_light" ) var productFootnoteIconLight : String?                 = null,
    @SerializedName("product_footnote_icon_dark"  ) var productFootnoteIconDark  : String?                 = null,
    @SerializedName("footnote"                    ) var footnote                 : String?                 = null,
    @SerializedName("footnote_icon_light"         ) var footnoteIconLight        : String?                 = null,
    @SerializedName("footnote_icon_dark"          ) var footnoteIconDark         : String?                 = null
)

data class BottomSheetButtons(
    @SerializedName("button_text"       ) var buttonText      : String? = null,
    @SerializedName("button_text_color" ) var buttonTextColor : String? = null,
    @SerializedName("button_color"      ) var buttonColor     : String? = null,
    @SerializedName("button_url"        ) var buttonUrl       : String? = null
)

data class BottomSheetDescriptions(
    @SerializedName("line_icon_dark"  ) var lineIconDark  : String? = null,
    @SerializedName("line_icon_light" ) var lineIconLight : String? = null,
    @SerializedName("text"            ) var text          : String? = null
)