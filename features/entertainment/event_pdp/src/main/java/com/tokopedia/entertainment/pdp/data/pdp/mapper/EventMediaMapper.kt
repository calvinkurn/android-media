package com.tokopedia.entertainment.pdp.data.pdp.mapper

import com.tokopedia.entertainment.pdp.data.ProductDetailData

object EventMediaMapper{
     fun mapperMediaPDP(productDetailData: ProductDetailData): List<String>{
         if(productDetailData.media.isNullOrEmpty()) {
            return listOf(productDetailData.thumbnailApp)
         }else {
             return productDetailData.media.map {
                 it.url
             }
         }
    }
}