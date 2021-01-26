package com.tokopedia.media.common.data

/*
* Client hints: ECT
* The first client hints that will be used for adaptive is Effective Connection Types (ECT).
* Beside ECT, there are also other hints that we may take to consideration: Downlink and RTT.
* But for the first phase, ECT is giving us enough information regarding the client’s
* network capabilities. See this article to read the details.
* */
const val HEADER_ECT = "ect"

/*
* determine object classification in every network capability segmentation
* only supported for 2 segmentation are slow for 2g / 3g, and fast for 4g and wifi.
* */
const val LOW_QUALITY = "3g"
const val HIGH_QUALITY = "4g"

// the key of connection type
const val HIGH_QUALITY_SETTINGS = 1 // 4g / wifi
const val LOW_QUALITY_SETTINGS = 2 // 2g / 3g