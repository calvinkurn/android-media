package com.tokopedia.play_common.viewcomponent

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by jegul on 04/08/20
 */
class ViewComponentNullableDelegate<VC: IViewComponent>(
        viewComponentCreator: (container: ViewGroup) -> VC
) : ReadOnlyProperty<Fragment, VC?> {

    private val nonNullDelegate = ViewComponentDelegate(viewComponentCreator)

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VC? {
        return try {
            nonNullDelegate.getValue(thisRef, property)
        } catch (e: Throwable) {
            null
        }
    }
}