package com.tokopedia.media.picker.ui.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.LinearLayout
import com.tokopedia.media.R
import com.tokopedia.dialog.R as unifyR

/**
 * Need to create a custom Dialog widget instead of using LoaderDialog from Unify.
 * a dialog loader unify has a solid white color on the background, besides
 * the media picker didn't need to.
 */
@SuppressLint("UnifyComponentUsage")
class LoaderDialogWidget constructor(
    context: Context
) : Dialog(context, unifyR.style.UnifyDialogOverlapStyle) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.widget_overlay_loader_dialog)

        window?.attributes?.width = LinearLayout.LayoutParams.MATCH_PARENT
        window?.attributes?.height = LinearLayout.LayoutParams.MATCH_PARENT
    }

}