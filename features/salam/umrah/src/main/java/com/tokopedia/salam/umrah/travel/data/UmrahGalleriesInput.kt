package com.tokopedia.salam.umrah.travel.data


/**
 * @author by Firman on 3/2/20
 */

data class UmrahGalleriesInput(
        var page : Int = 0,
        var limit : Int = 5,
        var entityName : List<String> = listOf(),
        var entitySlugName : String = ""
)