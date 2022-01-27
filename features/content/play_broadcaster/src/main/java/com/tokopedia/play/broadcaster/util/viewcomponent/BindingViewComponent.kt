package com.tokopedia.play.broadcaster.util.viewcomponent

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.viewcomponent.IViewComponent

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
abstract class BindingViewComponent(
    binding: ViewBinding
) : IViewComponent {

    override val id: Int = binding.root.id

    override val rootView: View = binding.root

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