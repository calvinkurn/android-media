package com.tokopedia.prereleaseinspector

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import java.util.*


@SuppressLint("StaticFieldLeak")
object ViewInspectorManager {

    const val TAG_OPTION_PICKER_DIALOG = "OPTION_PICKER_DIALOG"

    val viewList = ArrayList<View>()

    fun showPopupDialog(activity : Activity) {
        (activity as? AppCompatActivity)?.let {
            val viewInspectorDialog = ViewInspectorDialogFragment()
            viewInspectorDialog.setViewList(gatherView(it))
            viewInspectorDialog.show(it.supportFragmentManager, TAG_OPTION_PICKER_DIALOG)
        }
    }

    private fun gatherView(activity: Activity) : List<View> {
        viewList.clear()
        show_children(activity, activity.window.decorView)
        return viewList
    }

    private fun show_children(activity: Activity, v: View) {
        val viewgroup = v as ViewGroup
        for (i in 0 until viewgroup.childCount) {
            val v1 = viewgroup.getChildAt(i)
            viewList.add(v1)
            if (v1 is ViewGroup) {
                show_children(activity, v1)
            }
        }
    }
}
