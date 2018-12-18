package com.tokopedia.feedcomponent.view.viewmodel.post.grid

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel

/**
 * @author by milhamj on 07/12/18.
 */
data class GridPostViewModel(
        val itemList: MutableList<GridItemViewModel> = ArrayList(),
        val actionText: String = "",
        val actionLink: String = "",
        val totalItems: Int = 0
) : BasePostViewModel