package com.tokopedia.salam.umrah.homepage.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity

/**
 * @author by firman on 23/10/19
 */
interface UmrahHomepageFactory : AdapterTypeFactory {
    fun type(dataModel: UmrahSearchParameterEntity): Int
    fun type(dataModel: UmrahHomepageCategoryEntity): Int
    fun type(dataModel: UmrahHomepageCategoryFeaturedEntity): Int
    fun type(dataModel: UmrahHomepageMyUmrahEntity):Int
}