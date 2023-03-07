package com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.unifycomponents.Toaster

object ToasterHelper {
    fun showToaster(view: View?, title: String, type: Int) {
        view?.let {
            Toaster.build(
                it,
                title,
                Snackbar.LENGTH_LONG,
                type,
                it.resources.getString(R.string.stfs_toaster_ok)
            ).apply {
                anchorView = view
                show()
            }
        }
    }
}