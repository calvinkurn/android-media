package com.tokopedia.people.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.user.session.UserSessionInterface

internal class LoginListener(
    private val owner: LifecycleOwner,
    private val userSession: UserSessionInterface
) {

    fun observe(onJustLoggedIn: () -> Unit) {
        owner.lifecycle.addObserver(
            object : LifecycleEventObserver {

                private var mIsLoggedIn = userSession.isLoggedIn

                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event != Lifecycle.Event.ON_RESUME) return
                    if (!mIsLoggedIn && userSession.isLoggedIn) onJustLoggedIn()
                    mIsLoggedIn = userSession.isLoggedIn
                }
            }
        )
    }
}

@Composable
internal fun rememberLoginListener(
    userSession: UserSessionInterface
): LoginListener {
    val owner = LocalLifecycleOwner.current

    return remember(owner, userSession) {
        LoginListener(owner, userSession)
    }
}
