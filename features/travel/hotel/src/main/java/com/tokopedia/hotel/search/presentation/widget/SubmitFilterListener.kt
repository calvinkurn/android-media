package com.tokopedia.hotel.search.presentation.widget

import com.tokopedia.hotel.search.data.model.params.ParamFilterV2

/**
 * @author by jessica on 12/08/20
 */
interface SubmitFilterListener {
    fun onSubmitFilter(selectedFilter: MutableList<ParamFilterV2>)
}