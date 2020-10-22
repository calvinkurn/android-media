package com.tokopedia.homenav.mainnav.data.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.data.pojo.MainNavPojo

interface MainNavFactory {
    fun buildVisitableList(context: Context): MainNavFactory
    fun addProfileSection(mainNavPojo: MainNavPojo?): MainNavFactory
    fun build(): List<HomeNavVisitable>
}