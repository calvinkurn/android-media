package com.tokopedia.media.loader.utils

internal sealed class NetworkState
internal object Fast: NetworkState()
internal object Low: NetworkState()
internal object Undefined: NetworkState()
