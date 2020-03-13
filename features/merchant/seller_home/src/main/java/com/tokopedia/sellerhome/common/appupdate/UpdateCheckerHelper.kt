package com.tokopedia.sellerhome.common.appupdate

import android.app.Activity
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate

/**
 * Created By @ilhamsuaib on 2020-03-02
 */

object UpdateCheckerHelper {

    fun checkAppUpdate(activity: Activity) {
        val appUpdate: ApplicationUpdate = SellerRemoteConfigAppUpdate(activity)
        appUpdate.checkApplicationUpdate(object : ApplicationUpdate.OnUpdateListener {
            override fun onNeedUpdate(detail: DetailUpdate?) {
                if (detail != null && !activity.isFinishing) {
                    AppUpdateDialogBuilder(activity, detail,
                            object : AppUpdateDialogBuilder.Listener {
                                override fun onPositiveButtonClicked(detail: DetailUpdate?) {

                                }

                                override fun onNegativeButtonClicked(detail: DetailUpdate?) {

                                }
                            }).alertDialog.show()
                }
            }

            override fun onError(e: Exception?) {
                e?.printStackTrace()
            }

            override fun onNotNeedUpdate() {

            }
        })
    }
}