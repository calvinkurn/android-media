package com.tokopedia.play_common.viewcomponent

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by jegul on 31/07/20
 */
abstract class ViewComponent(
        container: ViewGroup,
        @IdRes rootId: Int
) : IViewComponent {

    override val id: Int = rootId

    override val rootView: View = container.findViewById(rootId)

    protected fun <V: View> findViewById(@IdRes id: Int): V = rootView.findViewById(id)

    protected fun getString(@StringRes stringRes: Int): String {
        return rootView.context.getString(stringRes)
    }

    protected fun getString(@StringRes stringRes: Int, vararg value: Any): String {
        return rootView.context.getString(stringRes, *value)
    }

    protected fun getColor(@ColorRes colorRes: Int): Int = MethodChecker.getColor(rootView.context, colorRes)

    protected val resources: Resources
        get() = rootView.resources

    open fun show() {
        rootView.show()
    }

    open fun hide() {
        rootView.hide()
    }

    open fun isShown(): Boolean = rootView.visibility == View.VISIBLE

    open fun isHidden(): Boolean = rootView.visibility == View.GONE
}