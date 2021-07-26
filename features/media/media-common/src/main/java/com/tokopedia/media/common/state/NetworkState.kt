package com.tokopedia.media.common.state

sealed class NetworkState
object Fast: NetworkState()
object Low: NetworkState()
object Undefined: NetworkState()