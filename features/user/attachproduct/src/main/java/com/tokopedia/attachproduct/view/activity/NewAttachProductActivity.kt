package com.tokopedia.attachproduct.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct
import com.tokopedia.applink.RouteManager
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachproduct.R
import com.tokopedia.attachproduct.view.fragment.AttachProductFragment
import com.tokopedia.attachproduct.view.fragment.NewAttachProductFragment.Companion.newInstance
import com.tokopedia.attachproduct.view.presenter.AttachProductContract
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import java.util.*

class NewAttachProductActivity : BaseSimpleActivity(), AttachProductContract.Activity {

    private var warehouseId = "0"
    private var shopId = ""
    private var shopName: String = ""
    private var isSeller = false
    private var source: String = ""
    private lateinit var remoteConfig: RemoteConfig
    var maxChecked = MAX_CHECKED_DEFAULT
    private val hiddenProducts: ArrayList<String> = arrayListOf()

    companion object {
        const val MAX_CHECKED_DEFAULT = 3
        const val TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK = 324
        const val SOURCE_TALK = "talk"
        const val USE_NEW_FRAGMENT_REMOTE_CONFIG = "use_new_fragment"
    }

    private fun isUseNewFragment(): Boolean {
        return remoteConfig.getBoolean(USE_NEW_FRAGMENT_REMOTE_CONFIG, true)
    }

    private fun setupParam() {
        isSeller = intent.getBooleanExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY,
            false)
        source = intent.getStringExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY)
                ?: ""
        maxChecked = intent.getIntExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_MAX_CHECKED,
            AttachProductActivity.MAX_CHECKED_DEFAULT)
        shopId = intent.getStringExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY)
                ?: ""
        hiddenProducts.addAll(intent.getStringArrayListExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_HIDDEN)
                ?: arrayListOf())
    }

    private fun setupWarehouseId() {
        warehouseId = intent.getStringExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID)
                ?: ""
        if (warehouseId.isEmpty()) {
            warehouseId = "0"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupParam()
        setupWarehouseId()
        remoteConfig = FirebaseRemoteConfigImpl(applicationContext)
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        var fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        return if (fragment != null) {
            fragment
        } else {
            fragment = if (isUseNewFragment()) {
                newInstance(
                    this, isSeller, source, maxChecked, hiddenProducts,
                    warehouseId, shopId)
            } else {
                AttachProductFragment.newInstance(this, isSeller, source,
                    maxChecked, hiddenProducts, warehouseId)
            }

            fragment
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        supportActionBar?.let { supportActionBar ->
            supportActionBar.setBackgroundDrawable(
                resources.getDrawable(com.tokopedia.attachproduct.R.drawable.
                bg_attach_product_white_toolbar_drop_shadow))
            supportActionBar.setHomeAsUpIndicator(
                MethodChecker.getDrawable(this,
                    com.tokopedia.attachproduct.R.drawable.ic_attach_product_close_default))
        }
        shopName =
                intent.getStringExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY) ?: ""

        toolbar.setBackgroundColor(MethodChecker.getColor(this, R.color.Unify_N0))
        toolbar.subtitle = shopName
    }

    //is used for old fragment
    override fun isSeller(): Boolean {
        return isSeller
    }

    //is used for old fragment
    override fun getShopId(): String {
        return shopId
    }

    override fun finishActivityWithResult(products: ArrayList<ResultProduct>) {
        val data = Intent()
        data.putParcelableArrayListExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY, products)
        setResult(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK, data)
        finish()
    }

    override fun goToAddProduct(shopId: String) {
        if (isSeller) {
            RouteManager.route(this, ApplinkConst.PRODUCT_ADD)
        }
    }

    override fun setShopName(shopName: String) {
        this.shopName = shopName
        intent.putExtra(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY, shopName)
        toolbar.subtitle = shopName
    }
}