package com.tokopedia.topads.auto.view.widget

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.View

import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.topads.auto.R

/**
 * Author errysuprayogi on 21,June,2019
 */
class ToasterAutoAds : BaseToaster() {
    companion object {

        fun make(view: View,
                 snackbarText: String,
                 @BaseToaster.Duration duration: Int): Snackbar {
            return BaseToaster.Builder(view, snackbarText, duration)
                    .setBackgroundDrawable(R.drawable.bg_toaster_autoads)
                    .setTextColor(R.color.white)
                    .setActionTextColor(R.color.white)
                    .build()
        }

        fun showClose(activity: Activity,
                      snackbarText: String, onClick: (View) -> Unit) {
            if (activity == null) {
                return
            }
            ToasterAutoAds.make(activity.findViewById(android.R.id.content),
                    snackbarText, BaseToaster.LENGTH_INDEFINITE)
                    .setAction(activity.getString(com.tokopedia.design.R.string.close)) { v ->
                        onClick(v)
                    }.show()
        }
    }
}
