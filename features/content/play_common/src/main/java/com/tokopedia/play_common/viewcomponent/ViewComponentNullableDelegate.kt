package com.tokopedia.play_common.viewcomponent

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by jegul on 04/08/20
 */
class ViewComponentNullableDelegate<VC: IViewComponent>(
        owner: LifecycleOwner,
        isEagerInit: Boolean,
        viewComponentCreator: (container: ViewGroup) -> VC
) : ReadOnlyProperty<Fragment, VC?> {

    private val nonNullDelegate = ViewComponentDelegate(owner, isEagerInit, viewComponentCreator, throwIfFailedCreation = false)

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VC? {
        return try {
            nonNullDelegate.getValue(thisRef, property)
        } catch (e: Throwable) {
            null
        }
    }
}