package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker

/**
 * Created by jegul on 09/06/20
 */
abstract class PartialView(
        container: ViewGroup,
        @IdRes rootId: Int
) {

    val rootView = container.findViewById<View>(rootId)

    protected fun<T: View> findViewById(@IdRes id: Int): T = rootView.findViewById(id)

    protected fun getColor(@ColorRes colorRes: Int): Int = MethodChecker.getColor(rootView.context, colorRes)

    protected fun getString(@StringRes stringRes: Int, vararg value: Any): String = rootView.context.getString(stringRes, *value)
}