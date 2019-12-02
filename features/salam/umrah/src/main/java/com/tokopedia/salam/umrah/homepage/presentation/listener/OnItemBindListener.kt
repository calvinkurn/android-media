package com.tokopedia.salam.umrah.homepage.presentation.listener

import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.homepage.data.Products
import com.tokopedia.salam.umrah.homepage.data.UmrahCategories
import com.tokopedia.salam.umrah.homepage.data.UmrahCategoriesFeatured

/**
 * @author by firman on 28/10/19
 */
interface onItemBindListener{
    fun onBindParameterVH(isLoadFromCloud: Boolean)
    fun onBindMyUmrahVH(isLoadFromCloud: Boolean)
    fun onBindCategoryVH(isLoadFromCloud: Boolean)
    fun onBindCategoryFeaturedVH(isLoadFromCloud: Boolean)

    fun onImpressionDanaImpian()
    fun onClickDanaImpian()

    fun onImpressionMyUmrah(headerTitle: String, myUmrahEntity: MyUmrahEntity, position: Int)
    fun onClickUmrahMyUmrah(title: String, myUmrahEntity: MyUmrahEntity, position:Int)

    fun onImpressionCategory(umrahCategories: UmrahCategories, position: Int)
    fun onClickCategory(umrahCategories: UmrahCategories, position: Int)

    fun onSearchProduct(period: String, location: String, price: String)

    fun onImpressionFeaturedCategory(headerTitle: String, element: UmrahCategoriesFeatured)
    fun onClickFeaturedCategory(headerTitle: String, positionDC: Int, products: Products, position: Int)
}