package com.tokopedia.mvc.presentation.product.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.util.constant.BundleConstant

class AddProductActivity : AppCompatActivity() {

    companion object {
        fun buildCreateModeIntent(context: Context, voucherConfiguration: VoucherConfiguration): Intent {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
            }

            val intent = Intent(context, AddProductActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }

        fun buildEditModeIntent(context: Context, voucherConfiguration: VoucherConfiguration): Intent {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.EDIT)
                putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
            }

            val intent = Intent(context, AddProductActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }

    private val pageMode by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_add_product)

        val pageMode = pageMode ?: return
        val voucherConfiguration = voucherConfiguration ?: return

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AddProductFragment.newInstance(pageMode, voucherConfiguration))
            .commit()
    }

}
