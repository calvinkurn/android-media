package com.tokopedia.product.detail.common.utils.extensions

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.viewIsVisible
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by yovi.putra on 02/08/22"
 * Project name: android-tokopedia-core
 **/

/**
 * Executes [block] with a typed version of the View's layoutParams and reassigns the
 * layoutParams with the updated version.
 *
 * @see View.getLayoutParams
 * @see View.setLayoutParams
 **/
@JvmName("updateLayoutParamsTyped")
inline fun <reified T : ViewGroup.LayoutParams> View.updateLayoutParams(
    block: T?.() -> Unit
) {
    val params = layoutParams as? T
    block(params)
    layoutParams = params
}

fun String.validDimensionRatio() = matches(Regex("[0-9]+:[0-9]+"))

fun View.addOnImpressionListener(
    holder: ImpressHolder,
    holders: MutableList<String>,
    name: String,
    useHolders: Boolean,
    onView: () -> Unit
) {
    if (useHolders) {
        addOnPdpImpressionListener(holders, name, onView)
    } else {
        addOnImpressionListener(holder, onView)
    }
}

fun View.addOnPdpImpressionListener(
    holders: MutableList<String>,
    name: String,
    onView: () -> Unit
) {
    if (!holders.contains(name)) {
        viewTreeObserver.addOnScrollChangedListener(
            object : ViewTreeObserver.OnScrollChangedListener {
                override fun onScrollChanged() {
                    if (!holders.contains(name) && viewIsVisible(this@addOnPdpImpressionListener)) {
                        onView.invoke()
                        holders.add(name)
                        viewTreeObserver.removeOnScrollChangedListener(this)
                    }
                }
            }
        )
    }
}

fun globalWidgetAddImpression(
    holders: MutableList<String>,
    name: String,
    useHolders: Boolean,
    onView: () -> Unit
) {
    if (globalWidgetDoImpression(holders = holders, name = name, useHolders = useHolders)) {
        onView()
    }
}

fun globalWidgetDoImpression(
    holders: MutableList<String>,
    name: String,
    useHolders: Boolean
): Boolean {
    if (!useHolders) return true
    if (holders.contains(name)) return false

    holders.add(name)
    return true
}
