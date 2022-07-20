package com.tokopedia.play_common.viewcomponent

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by jegul on 31/07/20
 */
abstract class ViewComponent(
    view: View
) : IViewComponent {

    constructor(container: ViewGroup, @IdRes rootId: Int) : this(container.findViewById<View>(rootId))

    override val id: Int = view.id

    override val rootView: View = view

    protected fun <V: View> findViewById(@IdRes id: Int): V = rootView.findViewById(id)

    protected fun getString(@StringRes stringRes: Int): String {
        return rootView.context.getString(stringRes)
    }

    protected fun getString(@StringRes stringRes: Int, vararg value: Any): String {
        return rootView.context.getString(stringRes, *value)
    }

    protected fun getColor(@ColorRes colorRes: Int): Int = MethodChecker.getColor(rootView.context, colorRes)

    protected fun getDrawable(@DrawableRes drawableRes: Int): Drawable {
        return MethodChecker.getDrawable(rootView.context, drawableRes)
    }

    protected val resources: Resources
        get() = rootView.resources

    open fun show() {
        rootView.show()
    }

    open fun hide() {
        rootView.hide()
    }

    open fun invisible() {
        rootView.invisible()
    }

    open fun isShown(): Boolean = rootView.visibility == View.VISIBLE

    open fun isHidden(): Boolean = rootView.visibility == View.GONE
}