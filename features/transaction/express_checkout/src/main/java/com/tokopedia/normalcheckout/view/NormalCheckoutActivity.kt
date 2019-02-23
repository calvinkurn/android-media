package com.tokopedia.normalcheckout.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.expresscheckout.R
import com.tokopedia.normalcheckout.constant.ATC_AND_BUY
import com.tokopedia.normalcheckout.constant.ProductAction
import com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.EXTRA_MESSAGES_ERROR
import com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.RESULT_CODE_ERROR

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

open class NormalCheckoutActivity : BaseSimpleActivity(), NormalCheckoutListener {

    companion object {
        const val EXTRA_SHOP_ID = "shop_id"
        const val EXTRA_PRODUCT_ID = "product_id"
        const val EXTRA_NOTES = "notes"
        const val EXTRA_QUANTITY = "quantity"
        const val EXTRA_SELECTED_VARIANT_ID = "selected_variant_id"
        const val EXTRA_PRODUCT_IMAGE = "product_image"
        const val EXTRA_ACTION = "action"

        /**
         * shopID: mandatory
         * productID or (shopDomain and productName) is mandatory
         *
         */
        @JvmStatic
        fun getIntent(context: Context, shopId: String, productId: String,
                      notes: String? = "", quantity: Int? = 0,
                      selectedVariantId: ArrayList<Int>? = null,
                      @ProductAction action: Int = ATC_AND_BUY,
                      placeholderProductImage: String? = ""): Intent {
            return Intent(context, NormalCheckoutActivity::class.java).apply {
                putExtra(EXTRA_SHOP_ID, shopId)
                putExtra(EXTRA_PRODUCT_ID, productId)
                putExtra(EXTRA_NOTES, notes)
                putExtra(EXTRA_QUANTITY, quantity)
                putExtra(EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
                putExtra(EXTRA_ACTION, action)
                putExtra(EXTRA_PRODUCT_IMAGE, placeholderProductImage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        bundle?.run {
            return NormalCheckoutFragment.createInstance(getString(EXTRA_SHOP_ID),
                    getString(EXTRA_PRODUCT_ID),
                    getString(EXTRA_NOTES),
                    getInt(EXTRA_QUANTITY),
                    getStringArrayList(EXTRA_SELECTED_VARIANT_ID),
                    getInt(EXTRA_ACTION),
                    getString(EXTRA_PRODUCT_IMAGE))
        }
        return Fragment()
    }

    override fun finishWithResult(messages: String) {
        val intentResult = Intent()
        intentResult.putExtra(EXTRA_MESSAGES_ERROR, messages)
        setResult(RESULT_CODE_ERROR, intentResult)
        finish()
        overridePendingTransition(0, R.anim.push_down)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.anim.push_down)
    }
}
