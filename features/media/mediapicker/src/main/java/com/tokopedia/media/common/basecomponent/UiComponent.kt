package com.tokopedia.media.common.basecomponent

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

abstract class UiComponent constructor(
    private val container: ViewGroup,
    @IdRes private val componentId: Int,
) : BaseUiComponent {

    protected val context: Context by lazy {
        container.context
    }

    protected fun <V: View> findViewById(
        @IdRes id: Int
    ) = lazy(LazyThreadSafetyMode.NONE) {
        componentView().findViewById(id) as V
    }.value

    override fun componentView(): View {
        return container.findViewById(componentId)
    }

    override fun resources(): Resources {
        return componentView().resources
    }

    override fun show() {
        componentView().show()
    }

    override fun hide() {
        componentView().hide()
    }

    override fun isShown(): Boolean {
        return componentView().visibility == View.VISIBLE
    }

    override fun isHidden(): Boolean {
        return componentView().visibility == View.GONE
    }

}