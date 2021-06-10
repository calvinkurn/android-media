package com.tokopedia.play.broadcaster.util.bottomsheet

import android.app.Dialog
import android.os.Build
import com.tokopedia.play_common.util.extension.updateNavigationBarColors
import javax.inject.Inject

/**
 * Created by jegul on 16/07/20
 */
class NavigationBarColorDialogCustomizer @Inject constructor() : PlayBroadcastDialogCustomizer {

    override fun customize(dialog: Dialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.updateNavigationBarColors(
                    intArrayOf(com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            dialog.updateNavigationBarColors(
                    intArrayOf(
                            com.tokopedia.unifyprinciples.R.color.Unify_N0,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_20
                    )
            )
    }
}