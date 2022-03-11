package com.tokopedia.picker.common.basecomponent

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.picker.common.basecomponent.observers.ImmediateLifecycleObserver
import com.tokopedia.picker.common.basecomponent.observers.UiComponentLifecycleObserver
import com.tokopedia.picker.common.basecomponent.utils.addSafeObserver
import com.tokopedia.picker.common.basecomponent.utils.createUiComponent
import com.tokopedia.picker.common.basecomponent.utils.rootCurrentView
import com.tokopedia.picker.common.basecomponent.utils.safeLifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class UiComponentDelegate<Ui: BaseUiComponent>(
    private val lifecycleOwner: LifecycleOwner,
    private val componentCreation: (ViewGroup) -> Ui,
    eagerComponent: Boolean,
) : ReadOnlyProperty<LifecycleOwner, Ui> {

    private var uiComponent: Ui? = null

    init {
        if (eagerComponent) {
            lifecycleOwner.addSafeObserver(buildImmediateLifecycleObserver())
        }
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): Ui {
        uiComponent?.let { return it }

        return getOrCreateComponent(thisRef)
    }

    private fun getOrCreateComponent(owner: LifecycleOwner): Ui = synchronized(this) {
        uiComponent?.let {
            return it
        }

        val safeLifecycleOwner = owner.safeLifecycleOwner().apply {
            addSafeObserver(buildUiComponentLifecycleObserver(owner))
        }.also {
            if (!it.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                error("Ui Component hasn't initialized yet")
            }
        }

        uiComponent = safeLifecycleOwner.createUiComponent(
            componentCreation,
            owner.rootCurrentView()
        )

        return uiComponent as Ui
    }

    private fun buildUiComponentLifecycleObserver(
        owner: LifecycleOwner
    ) = UiComponentLifecycleObserver(owner) {
        release()
    }

    private fun buildImmediateLifecycleObserver() = ImmediateLifecycleObserver(
        owner = lifecycleOwner,
        componentCreation = {
            getOrCreateComponent(lifecycleOwner)
        }
    )

    private fun release() = synchronized(this) {
        uiComponent?.release()
        uiComponent = null
    }

}