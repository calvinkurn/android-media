package com.tokopedia.home_page_banner.ext

import com.tokopedia.home_page_banner.presenter.model.BannerModel

fun List<BannerModel>.isSame(newList: List<BannerModel>): Boolean{
    if(size != newList.size) return false

    for(i in 0 until size){
        if(this[i].id != newList[i].id && this[i].url != newList[i].url){
            return false
        }
    }
    return true
}