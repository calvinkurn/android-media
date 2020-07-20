package com.tokopedia.deals.home.listener

import com.tokopedia.deals.home.ui.dataview.FavouritePlacesDataView

/**
 * @author by jessica on 24/06/20
 */

interface DealsFavouriteCategoriesListener {
    fun onClickFavouriteCategory(url: String, favoritePlacesDataView: FavouritePlacesDataView.Place, position: Int)
}