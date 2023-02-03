package com.tokopedia.product.addedit.shipment.presentation.constant

/**
 * Created by faisalramd on 2020-03-14.
 */

class AddEditProductShipmentConstants {
    companion object{
        const val MIN_WEIGHT = 1
        const val MAX_WEIGHT_GRAM = 500000
        const val UNIT_GRAM = 0
        const val UNIT_KILOGRAM = 1
        const val DEFAULT_WEIGHT_VALUE = 500
        const val DEFAULT_WEIGHT_UNIT = UNIT_GRAM
        const val CPL_CONVENTIONAL_INDEX = 1
        const val CPL_ON_DEMAND_INDEX = 0
        const val CPL_THRESHOLD_SIZE = 2
        const val CPL_STANDARD_SHIPMENT_STATUS = 0

        const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
        const val EXTRA_CPL_ACTIVATED = "EXTRA_CPL_ACTIVATED"
        const val EXTRA_CPL_PARAM = "EXTRA_CPL_PARAM"
        const val EXTRA_SHOW_ONBOARDING_CPL = "EXTRA_SHOW_ONBOARDING_CPL"
        const val EXTRA_SHIPPER_SERVICES = "EXTRA_SHIPPER_SERVICES"
        const val ON_DEMAND_VALIDATION = "Dijemput Kurir"
        const val CONVENTIONAL_VALIDATION = "Antar ke Kantor Agen"
    }
}
