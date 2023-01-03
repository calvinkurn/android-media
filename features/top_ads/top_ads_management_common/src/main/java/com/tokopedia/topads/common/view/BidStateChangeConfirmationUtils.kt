package com.tokopedia.topads.common.view

import android.content.Context
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

fun Context.showBidStateChangeConfirmationDialog(
    isSwitchedToAutomatic: Boolean, positiveClick: () -> Unit, negativeClick: () -> Unit,
) {
    DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
        if (isSwitchedToAutomatic) {
            setTitle(context.resources.getString(R.string.topads_common_manual_dialog_title))
            setDescription(context.resources.getString(R.string.topads_common_manual_dialog_description))
        } else {
            setTitle(context.resources.getString(R.string.topads_common_automatic_dialog_title))
            setDescription(context.resources.getString(R.string.topads_common_automatic_dialog_description))
        }
        setPrimaryCTAText(context.resources.getString(R.string.topads_common_dialog_cta_text))
        setSecondaryCTAText(context.resources.getString(R.string.topads_common_batal))
        setSecondaryCTAClickListener {
            negativeClick()
            dismiss()
        }
        setPrimaryCTAClickListener {
            positiveClick()
            dismiss()
        }
    }.show()
}

fun Context.showBidStateChangeConfirmationBottomSheet(positiveClick: () -> Unit) {
    val viewBottomSheet = View.inflate(this, R.layout.topads_autoads_advantage, null)
    val title = this.getString(com.tokopedia.topads.common.R.string.topads_autoads_advantage_title)
    val bottomSheet = BottomSheetUnify().apply {
        setChild(viewBottomSheet)
        setTitle(title)
        showCloseIcon = true
        setCloseClickListener { dismiss() }
    }

    viewBottomSheet.findViewById<UnifyButton>(R.id.auto_ads_advantage_cta).setOnClickListener{
        positiveClick()
        bottomSheet.dismiss()
    }

    viewBottomSheet.findViewById<ImageUnify>(R.id.imageUnify).setImageUrl("https://images.tokopedia.net/img/android/topads/autoads/topads_autoads_advantage_1.png")
    viewBottomSheet.findViewById<ImageUnify>(R.id.imageUnify_2).setImageUrl("https://images.tokopedia.net/img/android/topads/autoads/topads_autoads_advantage_2.png")
    viewBottomSheet.findViewById<ImageUnify>(R.id.imageUnify_3).setImageUrl("https://images.tokopedia.net/img/android/topads/autoads/topads_autoads_advantage_3.png")
    viewBottomSheet.findViewById<ImageUnify>(R.id.imageUnify_4).setImageUrl("https://images.tokopedia.net/img/android/topads/autoads/topads_autoads_advantage_4.png")

    getFragmentManager(this)?.let {
        bottomSheet.show(it,"")
    }
}
fun Context.showBidStateChangeConfirmation(
    isSwitchedToAutomatic: Boolean, positiveClick: () -> Unit, negativeClick: () -> Unit,
) {
    if(isSwitchedToAutomatic) {
        showBidStateChangeConfirmationDialog(isSwitchedToAutomatic, positiveClick, negativeClick)
    } else {
        showBidStateChangeConfirmationBottomSheet(positiveClick)
    }
}

fun getFragmentManager(context: Context?): FragmentManager? {
    return when (context) {
        is AppCompatActivity -> context.supportFragmentManager
        is ContextThemeWrapper -> getFragmentManager(context.baseContext)
        else -> null
    }
}
