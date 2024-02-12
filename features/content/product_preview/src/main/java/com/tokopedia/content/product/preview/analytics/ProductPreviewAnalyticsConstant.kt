package com.tokopedia.content.product.preview.analytics

import com.tokopedia.track.TrackApp

internal val sessionIris: String
    get() = TrackApp.getInstance().gtm.irisSessionId
