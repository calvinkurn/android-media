package com.tokopedia.topupbills.telco.view.model

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalProductTelco(
        val id : String,
        val price : String,
        val priceDiscount : String,
        val description : String,
        val title : String,
        val productSelected: Boolean = false
)