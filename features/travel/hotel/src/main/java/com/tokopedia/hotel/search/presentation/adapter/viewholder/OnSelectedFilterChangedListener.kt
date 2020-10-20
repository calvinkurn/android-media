package com.tokopedia.hotel.search.presentation.adapter.viewholder
/**
 * @author by jessica on 27/08/20
 */

interface OnSelectedFilterChangedListener{
    fun onSelectedFilterChanged(name: String, filter: List<String> = listOf())
}