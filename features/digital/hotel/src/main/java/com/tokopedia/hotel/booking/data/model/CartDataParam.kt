package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by resakemal on 14/05/19
 */
class CartDataParam(@SerializedName("cartId")
                        @Expose
                        val cartId: String = "")