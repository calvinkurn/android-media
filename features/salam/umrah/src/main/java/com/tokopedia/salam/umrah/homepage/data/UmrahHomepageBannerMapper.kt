package com.tokopedia.salam.umrah.homepage.data

object UmrahHomepageBannerMapper {

    fun bannerMappertoString(data: List<UmrahBanner>): List<String>{
        return data.map { it.imageUrl }
    }
}