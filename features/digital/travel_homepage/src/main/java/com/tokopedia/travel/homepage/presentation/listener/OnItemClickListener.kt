package com.tokopedia.travel.homepage.presentation.listener

/**
 * @author by jessica on 2019-08-12
 */

interface OnItemClickListener {


    fun onTrackEventClick(type: Int, position: Int = 0, categoryName: String = "")

    fun onItemClick(appUrl: String, webUrl: String = "")
}