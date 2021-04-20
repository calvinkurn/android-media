package com.tokopedia.salam.umrah.homepage.presentation.listener

import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.homepage.data.Products
import com.tokopedia.salam.umrah.homepage.data.UmrahBanner
import com.tokopedia.salam.umrah.homepage.data.UmrahCategories
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBottomSheetData
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageBottomSheetAdapter

/**
 * @author by firman on 28/10/19
 */
interface onItemBindListener{
    fun onBindParameterVH(isLoadFromCloud: Boolean)
    fun onBindMyUmrahVH(isLoadFromCloud: Boolean)
    fun onBindCategoryVH(isLoadFromCloud: Boolean)
    fun onBindCategoryFeaturedVH(isLoadFromCloud: Boolean)
    fun onBindBannerVH(isLoadFromCloud: Boolean)
    fun onBindPartnerVH(isLoadFromCloud: Boolean)

    fun onImpressionDanaImpian()
    fun onClickDanaImpian()

    fun onImpressionMyUmrah(headerTitle: String, myUmrahEntity: MyUmrahEntity, position: Int)
    fun onClickUmrahMyUmrah(title: String, myUmrahEntity: MyUmrahEntity, position:Int)

    fun onImpressionCategory(umrahCategories: UmrahCategories, position: Int)
    fun onClickCategory(umrahCategories: UmrahCategories, position: Int)

    fun onSearchProduct(period: String, location: String, price: String)

    fun onImpressionFeaturedCategory(headerTitle: String,  products: Products, position: Int,positionDC: Int)
    fun onClickFeaturedCategory(headerTitle: String, positionDC: Int, products: Products, position: Int)

    fun onImpressionBanner(banner: UmrahBanner, position: Int)
    fun onClickBanner(banner: UmrahBanner, position: Int)

    fun onPerformanceHomepageListener()

    fun onImpressionPartnerTravel(headerTitle: String, umrahTravelAgentsEntity: UmrahTravelAgentsEntity)
    fun onClickPartnerTravel(headerTitle: String, travelAgent: TravelAgent)
    fun onClickAllPartner()

    fun showBottomSheetSearchParam(title: String, listBottomSheet: UmrahHomepageBottomSheetData,
                                   defaultOption: Int,
                                   adapter: UmrahHomepageBottomSheetAdapter)
}