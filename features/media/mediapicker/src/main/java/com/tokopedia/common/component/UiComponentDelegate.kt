package com.tokopedia.common.component

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.common.component.observers.ImmediateLifecycleObserver
import com.tokopedia.common.component.observers.UiComponentLifecycleObserver
import com.tokopedia.common.component.utils.addSafeObserver
import com.tokopedia.common.component.utils.createUiComponent
import com.tokopedia.common.component.utils.rootCurrentView
import com.tokopedia.common.component.utils.safeLifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class UiComponentDelegate<Ui: BaseUiComponent>(
    needImmediateComponent: Boolean,
    private val lifecycleOwner: LifecycleOwner,
    private val componentCreation: (ViewGroup) -> Ui
) : ReadOnlyProperty<LifecycleOwner, Ui> {

    private var uiComponent: Ui? = null

    init {
        if (needImmediateComponent) {
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

        return safeLifecycleOwner.createUiComponent(
            componentCreation,
            owner.rootCurrentView()
        )
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
        uiComponent = null
    }

}