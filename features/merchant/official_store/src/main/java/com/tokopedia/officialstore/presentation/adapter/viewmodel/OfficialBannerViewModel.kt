package com.tokopedia.officialstore.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.presentation.adapter.OfficialHomeAdapterTypeFactory

class OfficialBannerViewModel : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    fun dummyData(): List<String> {
        val dummy = ArrayList<String>()
        dummy.add("https://ecs7.tokopedia.net/img/cache/1080/banner/2019/9/8/20723472/20723472_4fd488ac-e335-48f5-94c5-c4fe16f183ee.jpg")
        dummy.add("https://ecs7.tokopedia.net/img/cache/1080/banner/2019/9/8/20723472/20723472_5bf61ca5-e592-4a13-91ec-590d3421f88b.jpg")
        dummy.add("https://ecs7.tokopedia.net/img/cache/1080/banner/2019/9/8/20723472/20723472_884472ac-d6c0-4ece-b168-b95b2c96a6f6.jpg")
        dummy.add("https://ecs7.tokopedia.net/img/cache/1080/banner/2019/9/8/20723472/20723472_4fd488ac-e335-48f5-94c5-c4fe16f183ee.jpg")
        dummy.add("https://ecs7.tokopedia.net/img/cache/1080/banner/2019/9/8/20723472/20723472_5bf61ca5-e592-4a13-91ec-590d3421f88b.jpg")
        dummy.add("https://ecs7.tokopedia.net/img/cache/1080/banner/2019/9/8/20723472/20723472_884472ac-d6c0-4ece-b168-b95b2c96a6f6.jpg")
        return dummy
    }

}