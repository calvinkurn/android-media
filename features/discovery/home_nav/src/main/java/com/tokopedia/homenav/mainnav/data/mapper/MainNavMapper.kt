package com.tokopedia.homenav.mainnav.data.mapper

import android.content.Context
import com.tokopedia.homenav.mainnav.data.factory.MainNavFactory
import com.tokopedia.homenav.mainnav.data.pojo.MainNavPojo
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.trackingoptimizer.TrackingQueue

class MainNavMapper(
        private val context: Context,
        private val mainNavFactory: MainNavFactory
) {

    fun mapData(data: MainNavPojo?): MainNavigationDataModel {
        val factory = mainNavFactory
                .buildVisitableList(context)
                .addProfileSection(data)

        return MainNavigationDataModel(factory.build())
    }

}