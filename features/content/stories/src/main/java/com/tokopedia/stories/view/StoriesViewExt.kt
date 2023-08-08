package com.tokopedia.stories.view

import android.content.Context
import com.tokopedia.dialog.DialogUnify

/**
 * @author by astidhiyaa on 08/08/23
 */
internal fun Context.showDialog (
    title: String,
    description: String,
    primaryCTAText: String,
    secondaryCTAText: String,
    primaryAction: () -> Unit,
    secondaryAction: () -> Unit = {}
) {
    DialogUnify(context = this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
        setTitle(title)
        setDescription(description)
        setPrimaryCTAText(primaryCTAText)
        setPrimaryCTAClickListener {
            primaryAction()
            dismiss()
        }
        setSecondaryCTAText(secondaryCTAText)
        setSecondaryCTAClickListener {
            secondaryAction()
            dismiss()
        }
    }.show()
}
