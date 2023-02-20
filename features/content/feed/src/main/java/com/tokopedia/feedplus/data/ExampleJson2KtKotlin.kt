package com.example.example

import com.google.gson.annotations.SerializedName
import com.tokopedia.feedplus.data.Data

data class ExampleJson2KtKotlin(

    @SerializedName("data") var data: Data? = Data()

)
