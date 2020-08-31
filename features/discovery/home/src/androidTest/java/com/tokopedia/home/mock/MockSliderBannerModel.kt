package com.tokopedia.home.mock

import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel

object MockSliderBannerModel {
    fun get(): HomepageBannerDataModel {
        val homepageBannerDataModel = HomepageBannerDataModel()
        homepageBannerDataModel.slides = listOf(
                createSliderBanner("https://ecs7-p.tokopedia.net/img/cache/800/VxWOnu/2020/8/12/df57e4b3-1a54-42e5-a73f-738c4621a7c4.jpg"),
                createSliderBanner("https://ecs7-p.tokopedia.net/img/cache/800/VxWOnu/2020/8/11/1c66d485-8b7b-4426-b17a-b367502db4c5.jpg"),
                createSliderBanner("https://ecs7-p.tokopedia.net/img/cache/800/VxWOnu/2020/8/12/3a83916e-190a-41b6-a61e-cda1b662a854.jpg"),
                createSliderBanner("https://ecs7-p.tokopedia.net/img/cache/800/VxWOnu/2020/8/11/08116340-0b95-4a0b-992b-9dd60ce1ed5d.jpg"),
                createSliderBanner("https://ecs7-p.tokopedia.net/img/cache/800/VxWOnu/2020/8/11/9f0d1fef-199f-4557-9987-e402aba7f99d.jpg"),
                createSliderBanner("https://ecs7-p.tokopedia.net/img/cache/800/VxWOnu/2020/8/11/98d06311-fe14-44b4-9d6b-987b2821dac2.jpg"),
                createSliderBanner("https://ecs7-p.tokopedia.net/img/cache/800/VxWOnu/2020/8/10/10e348d6-d105-4a17-b1ad-be0cf9a2e4f4.jpg"),
                createSliderBanner("https://ecs7-p.tokopedia.net/img/cache/800/VxWOnu/2020/8/11/d71e808a-534c-4a7e-b356-6dd1625b9834.jpg")
                )
        return homepageBannerDataModel
    }

    private fun createSliderBanner(imgUrl: String): BannerSlidesModel {
        return BannerSlidesModel(
                id=12873,
                galaxyAttribution="PG",
                persona="",
                brandId="",
                categoryPersona="",
                title="",
                imageUrl=imgUrl,
                redirectUrl="https://www.tokopedia.com/discovery/hobi-kemerdekaan?source=homepage.slider_banner.0.12873",
                creativeName="X_PG_HPB1_ASH Menyambut HUT Kemerdekaan Diskon Hingga 75%_All_User_12_Aug_20",
                applink="tokopedia://discovery/hobi-kemerdekaan?source=homepage.slider_banner.0.12873",
                promoCode="",
                topadsViewUrl="",
                type="general",
                categoryId="",
                campaignCode="mix00000030_ash menyambut hut kemerdekaan diskon hingga 75%_11august20-14august20",
                position=1
        )
    }
}