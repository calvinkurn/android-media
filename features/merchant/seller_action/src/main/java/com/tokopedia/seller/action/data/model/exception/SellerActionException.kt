package com.tokopedia.seller.action.data.model.exception

import android.net.Uri

class SellerActionException(val sliceUri: Uri, message: String): Exception(message)