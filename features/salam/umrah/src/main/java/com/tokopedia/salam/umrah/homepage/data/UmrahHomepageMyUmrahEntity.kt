package com.tokopedia.salam.umrah.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactory

class UmrahHomepageMyUmrahEntity(
        @SerializedName("umrahWidgetUmrahSayaList")
        @Expose
        val listMyUmrahEntity: List<MyUmrahEntity> = emptyList()
): UmrahHomepageModel(){

    companion object{
        val LAYOUT = R.layout.partial_umrah_home_page_dream_fund
    }

    override fun type(typeFactory: UmrahHomepageFactory): Int {
        return typeFactory.type(this)
    }
}