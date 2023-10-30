package com.tokopedia.content.common.util.bottomsheet

import android.app.Dialog
import android.os.Build
import com.tokopedia.content.common.util.dialog.updateNavigationBarColors
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by jegul on 16/07/20
 */
class NavigationBarColorDialogCustomizer @Inject constructor() : ContentDialogCustomizer {

    override fun customize(dialog: Dialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.updateNavigationBarColors(
                    intArrayOf(unifyprinciplesR.color.Unify_NN0)
            )
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            dialog.updateNavigationBarColors(
                    intArrayOf(
                            unifyprinciplesR.color.Unify_NN0,
                            unifyprinciplesR.color.Unify_NN950_20
                    )
            )
    }
}
