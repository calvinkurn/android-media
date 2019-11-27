package com.tokopedia.salam.umrah.homepage.presentation.listener
/**
 * @author by firman on 28/10/19
 */
interface onItemBindListener{
    fun onBindParameterVH(isLoadFromCloud: Boolean)
    fun onBindMyUmrahVH(isLoadFromCloud: Boolean)
    fun onBindCategoryVH(isLoadFromCloud: Boolean)
    fun onBindCategoryFeaturedVH(isLoadFromCloud: Boolean)
}