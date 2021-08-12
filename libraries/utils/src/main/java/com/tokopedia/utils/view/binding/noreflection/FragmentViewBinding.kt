@file:JvmName("FragmentViewBinding")
@file:Suppress("RedundantVisibilityModifier", "unused")

package com.tokopedia.utils.view.binding.noreflection

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.getRootView
import com.tokopedia.utils.view.binding.internal.requireViewByIdCompat

private class FragmentViewBindingProperty<in F : Fragment, T : ViewBinding?>(
        viewBinder: (F) -> T?
) : LifecycleViewBindingProperty<F, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        try {
            return thisRef.viewLifecycleOwner
        } catch (ignored: IllegalStateException) {
            error("TkpdViewBinding: Fragment does not have view associated with it or the view has been destroyed")
        }
    }
}

@Suppress("UNCHECKED_CAST")
@JvmName("viewBindingFragment")
fun <F : Fragment, T : ViewBinding> Fragment.viewBinding(
        viewBinder: (F) -> T
): ViewBindingProperty<F, T> {
    // TODO: for DialogFragment, use DialogFragmentViewBindingProperty instead
    return FragmentViewBindingProperty(viewBinder)
}

@JvmName("viewBindingFragment")
inline fun <F : Fragment, T : ViewBinding> Fragment.viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        crossinline viewProvider: (F) -> View = Fragment::requireView
): ViewBindingProperty<F, T> {
    return viewBinding { fragment: F -> viewBindingFactory(viewProvider(fragment)) }
}

@Suppress("UNCHECKED_CAST")
@JvmName("viewBindingFragment")
inline fun <F : Fragment, T : ViewBinding> Fragment.viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        @IdRes viewBindingRootId: Int
): ViewBindingProperty<F, T> {
    return when (this) {
        is DialogFragment -> {
            viewBinding<DialogFragment, T>(viewBindingFactory) { fragment ->
                fragment.getRootView(viewBindingRootId)
            } as ViewBindingProperty<F, T>
        }
        else -> {
            viewBinding(viewBindingFactory) { fragment: F ->
                fragment.requireView().requireViewByIdCompat(viewBindingRootId)
            }
        }
    }
}