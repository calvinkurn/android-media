package com.tokopedia.people.utils

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.content.common.util.activitycontract.ContentActivityResultContracts.OpenLogin
import com.tokopedia.people.views.uimodel.PeopleUiModel

internal class LoginToFollowHelper(
    private val registry: ActivityResultRegistry,
    owner: LifecycleOwner,
    private val onShouldFollow: (PeopleUiModel) -> Unit
) {

    private val launcherMap = mutableMapOf<String, ActivityResultLauncher<Unit>>()

    init {
        owner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event != Lifecycle.Event.ON_DESTROY) return
                    launcherMap.forEach { it.value.unregister() }
                    launcherMap.clear()
                }
            }
        )
    }

    fun launch(people: PeopleUiModel) {
        val launcher = getLauncher(people)
        launcher.launch()
    }

    private fun getLauncher(people: PeopleUiModel): ActivityResultLauncher<Unit> {
        if (!launcherMap.containsKey(people.key)) {
            val launcher = registry.register(people.key, OpenLogin()) {
                if (!it) return@register
                onShouldFollow(people)
            }
            launcherMap[people.key] = launcher
        }

        return launcherMap[people.key]!!
    }

    private val PeopleUiModel.key get() = when (this) {
        is PeopleUiModel.ShopUiModel -> "shop_$id"
        is PeopleUiModel.UserUiModel -> "user_$id"
    }
}

@Composable
internal fun rememberLoginToFollowHelper(
    onShouldFollow: (PeopleUiModel) -> Unit
): LoginToFollowHelper {
    val owner = LocalLifecycleOwner.current

    val activityResultRegistry = checkNotNull(LocalActivityResultRegistryOwner.current) {
        "No ActivityResultRegistryOwner was provided via LocalActivityResultRegistryOwner"
    }.activityResultRegistry

    return remember(activityResultRegistry, owner, onShouldFollow) {
        LoginToFollowHelper(
            registry = activityResultRegistry,
            owner = owner,
            onShouldFollow = onShouldFollow
        )
    }
}
