package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 12/06/2020
 */
data class GetPDPInfo(@SerializedName("media")
                      @Expose
                      val mediaList: List<Media> = arrayListOf()) {

    data class Response(@SerializedName("getPDPInfo")
                        @Expose
                        val getPdpInfo: GetPDPInfo = GetPDPInfo())

    data class Media(@SerializedName("URLOriginal")
                     @Expose
                     val urlOriginal: String = "")
}