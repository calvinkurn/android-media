package com.tokopedia.broadcast.message.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BlastMessageResponse(@SerializedName("success")
                                @Expose
                                val success: Boolean = false,

                                @SerializedName("blastId")
                                @Expose
                                val blastId: Int){

    data class Result(@SerializedName("chatSendBlastSeller")
                        @Expose val result: BlastMessageResponse){

    }
}