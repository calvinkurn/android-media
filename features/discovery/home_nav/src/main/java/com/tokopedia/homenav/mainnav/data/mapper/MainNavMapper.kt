package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactory
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel

class MainNavMapper (private val mainNavDataFactory: MainNavDataFactory) {

    fun mapMainNavData(userPojo: UserPojo, categoryData: List<DynamicHomeIconEntity.Category>?): MainNavigationDataModel {
        val factory: MainNavDataFactory = mainNavDataFactory.buildVisitableList(userPojo)
                .addProfileSection()
                .addSeparatorSection()
                .addBUListSection(categoryData)
        return MainNavigationDataModel(factory.build())
    }
}