package com.tokopedia.travelhomepage.homepage.presentation.listener

import com.tokopedia.travelhomepage.homepage.data.TravelLayoutSubhomepage

/**
 * @author by jessica on 2019-08-12
 */

interface OnItemBindListener {
    fun onDestinationVHBind(isFromCloud: Boolean?)

    fun onItemBindViewHolder(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean?)
}