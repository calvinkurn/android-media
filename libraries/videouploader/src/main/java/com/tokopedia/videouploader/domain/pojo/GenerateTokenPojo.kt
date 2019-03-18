package com.tokopedia.videouploader.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 13/03/19.
 * Sample Pojo :
 *
 * [{
"data": {
"TopliveVideoToken": {
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNTQ3OTQxNiIsImV4cCI6MTU1MjkyNTk2NCwiaXNzIjoidG9rb3BlZGlhIn0.yXYP1kTNxVJ9d40yYdY0bYSHT38e_2QmITfx8msMGvg"
}
}
}]
 */
data class GenerateTokenPojo(
        @SerializedName("TopliveVideoToken")
        @Expose
        val topLiveVideoToken : TopliveVideoToken = TopliveVideoToken()
)

data class TopliveVideoToken(
        @SerializedName("token")
        @Expose
        val token : String = ""
)