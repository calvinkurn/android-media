package com.tokopedia.profilecompletion.common

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker

object ColorUtils {
    fun setBackgroundColor(context: Context?, activity: FragmentActivity?) {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                    MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        }
    }
}