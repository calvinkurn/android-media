package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactory
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel

class MainNavMapper (private val mainNavDataFactory: MainNavDataFactory) {

    fun mapMainNavData(userPojo: UserPojo): MainNavigationDataModel {
        val factory: MainNavDataFactory = mainNavDataFactory.buildVisitableList(userPojo)
                .addProfileSection()
                .addSeparatorSection()
        return MainNavigationDataModel(factory.build())
    }
}