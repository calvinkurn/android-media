package com.tokopedia.salam.umrah.travel.data


/**
 * @author by Firman on 3/2/20
 */

data class UmrahGalleriesInput(
        val page : Int = 1,
        val limit : Int = 20,
        val entityName : List<String> = arrayListOf(),
        val entitySlugName : String = ""
)