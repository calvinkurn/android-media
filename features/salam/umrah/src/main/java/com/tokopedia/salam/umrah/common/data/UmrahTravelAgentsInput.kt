package com.tokopedia.salam.umrah.common.data

/**
 * @author by firman on 20/01/19
 */

data class UmrahTravelAgentsInput(
        var page : Int = 1,
        var limit : Int = 20,
        var flags : List<String> = emptyList()
)