package com.tokopedia.sellerorder.detail.presentation.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BOOKING_CODE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BOOKING_TYPE

/**
 * Created by fwidjaja on 2019-11-27.
 */
class SomDetailBookingCodeFragment: BaseDaggerFragment() {
    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailBookingCodeFragment {
            return SomDetailBookingCodeFragment().apply {
                arguments = Bundle().apply {
                    /*putString(PARAM_BOOKING_CODE, bundle.getString(PARAM_BOOKING_CODE))
                    putInt(PARAM_BOOKING_TYPE, bundle.getInt(PARAM_CURR_SHIPMENT_ID))
                    putString(PARAM_CURR_SHIPMENT_NAME, bundle.getString(PARAM_CURR_SHIPMENT_NAME))
                    putString(PARAM_CURR_SHIPMENT_PRODUCT_NAME, bundle.getString(PARAM_CURR_SHIPMENT_PRODUCT_NAME))*/
                }
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}
}