package com.tokopedia.hotel.destination.view.adapter

/**
 * @author by jessica on 01/04/19
 */
interface RecentSearchClickListener {

    fun onDeleteRecentSearchItem(keyword: String)

    fun onDeleteAllRecentSearch()

    fun onItemClicked(applink: String, webUrl: String, shouldFinishActivity: Boolean)
}