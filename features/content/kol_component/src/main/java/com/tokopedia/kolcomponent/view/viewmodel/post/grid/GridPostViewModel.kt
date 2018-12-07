package com.tokopedia.kolcomponent.view.viewmodel.post.grid

/**
 * @author by milhamj on 07/12/18.
 */
data class GridPostViewModel(
        val itemList: MutableList<String> = ArrayList(),
        val actionText: String = "",
        val actionLink: String = "",
        val totalItems: Int = 0
)