package com.tokopedia.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.checkout.test.R
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.purchase_platform.common.constant.CartConstant.IS_TESTING_FLOW

class InstrumentTestCheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_simple)

        val bundle = Bundle()
        bundle.putBoolean(IS_TESTING_FLOW, true)
        val shipmentFragment = ShipmentFragment.newInstance(false, "", bundle)
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, shipmentFragment, "")
                .commit()
    }

}