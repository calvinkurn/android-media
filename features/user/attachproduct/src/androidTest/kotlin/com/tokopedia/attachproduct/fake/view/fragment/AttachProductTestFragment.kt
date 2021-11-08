package com.tokopedia.attachproduct.fake.view.fragment

import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachproduct.AttachProductTest
import com.tokopedia.attachproduct.di.DaggerAttachProductComponent
import com.tokopedia.attachproduct.fake.di.DaggerFakeAttachProductComponent
import com.tokopedia.attachproduct.fake.di.DaggerFakeBaseAppComponent
import com.tokopedia.attachproduct.fake.di.FakeAppModule
import com.tokopedia.attachproduct.fake.di.FakeAttachProductModule
import com.tokopedia.attachproduct.fake.view.AttachProductTestActivity
import com.tokopedia.attachproduct.view.fragment.AttachProductFragment
import com.tokopedia.attachproduct.view.presenter.AttachProductContract
import java.util.ArrayList

class AttachProductTestFragment : AttachProductFragment() {
    override fun initInjector() {
        AttachProductTest.fakeComponent?.inject(this)
    }

    companion object {
        private const val IS_SELLER = "isSeller"
        private const val SOURCE = "source"
        private const val MAX_CHECKED = "max_checked"
        private const val HIDDEN_PRODUCTS = "hidden_products"
        private const val SHOP_ID = "shop_id"

        @JvmStatic
        fun newInstance(
            checkedUIView: AttachProductContract.Activity?,
            isSeller: Boolean, source: String?, maxChecked: Int,
            hiddenProducts: ArrayList<String>?, warehouseId: String?, shopId: String
        ): AttachProductTestFragment {
            val args = Bundle()
            args.putString(SHOP_ID, shopId)
            args.putBoolean(IS_SELLER, isSeller)
            args.putString(SOURCE, source)
            args.putString(
                ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID,
                warehouseId
            )
            args.putInt(MAX_CHECKED, maxChecked)
            args.putStringArrayList(HIDDEN_PRODUCTS, hiddenProducts)
            val fragment = AttachProductTestFragment()
            fragment.setActivityContract(checkedUIView)
            fragment.arguments = args
            return fragment
        }
    }
}