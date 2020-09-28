package com.tokopedia.media.loader.utils

/*
* Client hints: ECT
* The first client hints that will be used for adaptive is Effective Connection Types (ECT).
* Beside ECT, there are also other hints that we may take to consideration: Downlink and RTT.
* But for the first phase, ECT is giving us enough information regarding the client’s
* network capabilities. See this article to read the details.
* */
const val HEADER_ECT = "ECT"

/*
* determine object classification in every network capability segmentation
* only supported for 2 segmentation are slow for 2g / 3g, and fast for 4g and wifi.
* */
const val LOW_QUALITY = "2g"
const val HIGH_QUALITY = "4g"

// default rounded
const val DEFAULT_ROUNDED = 5.0f