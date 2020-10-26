package com.tokopedia.homenav.mainnav.data.factory

import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity

interface MainNavDataFactory {

    fun buildVisitableList(userPojo: UserPojo): MainNavDataFactory

    fun addProfileSection(): MainNavDataFactory
    fun addSeparatorSection(): MainNavDataFactory
    fun addBUListSection(categoryData: List<DynamicHomeIconEntity.Category>?): MainNavDataFactory

    fun build(): List<HomeNavVisitable>
}