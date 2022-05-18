package com.tokopedia.product_bundle.common.extension

import android.app.Activity
import androidx.core.content.ContextCompat
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.UnifyButton

internal fun Activity?.setBackgroundToWhite() {
    this?.apply {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }
}

internal fun Activity?.showUnifyDialog(title: String, message: String, buttonText: String) {
    this?.apply {
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(title)
            setDescription(message)
            setPrimaryCTAText(buttonText)
            setSecondaryCTAText(getString(R.string.action_back))
            dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.TEXT_ONLY
            setSecondaryCTAClickListener { finish() }
            setPrimaryCTAClickListener { dismiss() }
        }.show()
    }
}