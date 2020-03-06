package com.tokopedia.travelhomepage.homepage.presentation.listener

import com.tokopedia.travelhomepage.homepage.data.TravelLayoutSubhomepage

/**
 * @author by jessica on 2019-08-12
 */

interface OnItemBindListener {
    fun onItemBindViewHolder(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, position: Int, isFromCloud: Boolean?)
}