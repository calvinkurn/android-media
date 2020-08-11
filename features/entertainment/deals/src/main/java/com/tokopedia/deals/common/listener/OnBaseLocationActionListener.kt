package com.tokopedia.deals.common.listener

import com.tokopedia.deals.location_picker.model.response.Location

/**
 * @author by jessica on 15/06/20
 */

interface OnBaseLocationActionListener {
    fun onBaseLocationChanged(location: Location)
}