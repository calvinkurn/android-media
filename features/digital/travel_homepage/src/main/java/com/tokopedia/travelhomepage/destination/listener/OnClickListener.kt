package com.tokopedia.travelhomepage.destination.listener

/**
 * @author by jessica on 2020-01-03
 */

interface OnClickListener {

    fun clickAndRedirect(appUrl: String, webUrl: String = "")

}