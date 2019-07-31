package com.tokopedia.ovop2p

import android.content.Intent

interface OvoP2pRouter {
    fun gotoQrScannerPage(needResult: Boolean): Intent
}
