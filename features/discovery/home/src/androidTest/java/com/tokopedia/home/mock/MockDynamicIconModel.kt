package com.tokopedia.home.mock

import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel

object MockDynamicIconModel {
    fun get(): DynamicIconSectionDataModel {
        val dynamicIconSectionDataModel =
                DynamicIconSectionDataModel(
                        listOf(
                                createDynamicIcon("https://ecs7-p.tokopedia.net/img/cache/100-square/attachment/2020/8/11/24593854/24593854_8c192e1d-9297-4aa0-877a-62cc26a43740.png"),
                                createDynamicIcon("https://ecs7-p.tokopedia.net/img/cache/100-square/attachment/2020/7/3/85531617/85531617_89f1aada-8bd2-4282-be2d-25fcaecd9f9c.png"),
                                createDynamicIcon("https://ecs7-p.tokopedia.net/img/cache/100-square/attachment/2020/7/3/85531617/85531617_fc0d80fb-1357-41f6-bced-866ea7f29bbb.png"),
                                createDynamicIcon("https://ecs7-p.tokopedia.net/img/cache/100-square/attachment/2020/7/3/85531617/85531617_fb247fec-6870-4289-8477-b34dc0fab831.png"),
                                createDynamicIcon("https://ecs7-p.tokopedia.net/img/cache/100-square/attachment/2020/7/3/85531617/85531617_0164df1a-ee10-475a-9d8d-87ab8807546f.png"),
                                createDynamicIcon("https://ecs7-p.tokopedia.net/img/cache/100-square/attachment/2020/8/1/24593854/24593854_4a0ec792-e72a-40b9-abab-3785001aa942.png"),
                                createDynamicIcon("https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/4/2/3127195/3127195_426c7c26-e0e7-47cb-ba5a-52241b625d71.png"),
                                createDynamicIcon("https://ecs7.tokopedia.net/img/cache/100-square/attachment/2020/7/2/85531617/85531617_f5606d78-2e39-453e-a362-aa59678dc24e.png"),
                                createDynamicIcon("https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/4/1/3127195/3127195_2258af2c-550d-4b7f-a8d4-f62178070ed8.png"),
                                createDynamicIcon("https://ecs7-p.tokopedia.net/img/cache/100-square/attachment/2019/4/2/3127195/3127195_0bdbfedb-8283-47bd-b56d-67c95b34fe9c.png")
                                )
                )
        return dynamicIconSectionDataModel
    }

    private fun createDynamicIcon(imgUrl: String): DynamicHomeIcon.DynamicIcon {
        return DynamicHomeIcon.DynamicIcon(
                id = "533",
                applinks = "tokopedia://discovery/bangga-buatan-indonesia",
                imageUrl=imgUrl,
                name="Buatan Indonesia",
                url="https://www.tokopedia.com/discovery/bangga-buatan-indonesia",
                bu_identifier="PG_PG_Buatan Indonesia",
                galaxyAttribution="PG",
                persona="", brandId="",
                categoryPersona="")
    }
}