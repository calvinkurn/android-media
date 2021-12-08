package model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductArResponse(
        @SerializedName("pdpGetARData")
        @Expose
        val data: PdpGetARData = PdpGetARData()
)

data class PdpGetARData(
        @SerializedName("optionBgImage")
        @Expose
        val optionBgImage: String = "",
        @SerializedName("options")
        @Expose
        val options: List<Option> = listOf(),
        @SerializedName("provider")
        @Expose
        val provider: String = ""
)