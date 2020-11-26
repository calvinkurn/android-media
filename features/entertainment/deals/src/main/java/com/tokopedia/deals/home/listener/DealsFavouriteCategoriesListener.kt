package com.tokopedia.deals.home.listener

import com.tokopedia.deals.home.ui.dataview.CuratedCategoryDataView

/**
 * @author by jessica on 24/06/20
 */

interface DealsFavouriteCategoriesListener {
    fun onBindFavouriteCategory(curatedCategoryDataView: CuratedCategoryDataView, position: Int)
    fun onClickFavouriteCategory(url: String, favoritePlacesDataView: CuratedCategoryDataView.CuratedCategory, position: Int)
}