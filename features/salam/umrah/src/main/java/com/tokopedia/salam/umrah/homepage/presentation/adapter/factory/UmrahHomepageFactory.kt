package com.tokopedia.salam.umrah.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity

/**
 * @author by firman on 23/10/19
 */
interface UmrahHomepageFactory {
    fun type(dataModel: UmrahSearchParameterEntity): Int
    fun type(dataModel: UmrahHomepageCategoryEntity): Int
    fun type(dataModel: UmrahHomepageCategoryFeaturedEntity): Int
    fun type(dataModel: UmrahHomepageMyUmrahEntity):Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}