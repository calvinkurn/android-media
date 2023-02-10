package com.tokopedia.media.loader.utils

sealed class NetworkState
object Fast: NetworkState()
object Low: NetworkState()
object Undefined: NetworkState()
