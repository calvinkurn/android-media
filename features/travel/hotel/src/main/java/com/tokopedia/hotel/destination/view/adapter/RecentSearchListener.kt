package com.tokopedia.hotel.destination.view.adapter

import com.tokopedia.hotel.destination.data.model.RecentSearch

/**
 * @author by jessica on 01/04/19
 */
interface RecentSearchListener {

    fun onDeleteRecentSearchItem(uuid: String)

    fun onDeleteAllRecentSearch()

    fun onItemClicked(recentSearch: RecentSearch)

    fun isRecentSearchEmpty()
}