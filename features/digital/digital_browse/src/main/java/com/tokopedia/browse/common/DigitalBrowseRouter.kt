package com.tokopedia.browse.common

import android.content.Context
import android.content.Intent

/**
 * @author by furqan on 30/08/18.
 */

interface DigitalBrowseRouter {

    fun goToWebview(context: Context, url: String)

    fun gotoSearchPage(context: Context): Intent
}
