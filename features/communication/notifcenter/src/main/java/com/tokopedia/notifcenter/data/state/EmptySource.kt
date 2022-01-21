package com.tokopedia.notifcenter.data.state

sealed class EmptySource {
    object Normal: EmptySource()
    object Transaction: EmptySource()
}