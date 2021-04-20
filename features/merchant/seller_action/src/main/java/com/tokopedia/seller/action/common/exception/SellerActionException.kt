package com.tokopedia.seller.action.common.exception

import android.net.Uri

class SellerActionException(val sliceUri: Uri, message: String): Exception(message)