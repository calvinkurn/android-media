package com.tokopedia.product_ar.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.modiface.mfemakeupkit.MFEMakeupEngine
import com.modiface.mfemakeupkit.data.MFEMakeupRenderingParameters
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.di.DaggerProductArComponent
import com.tokopedia.product_ar.di.ProductArComponent
import com.tokopedia.product_ar.di.ProductArModule
import com.tokopedia.product_ar.util.ProductArConstant
import com.tokopedia.product_ar.view.fragment.ProductArComparisonFragment
import com.tokopedia.product_ar.view.fragment.ProductArComparisonFragment.Companion.PRODUCT_AR_COMPARISON_FRAGMENT
import com.tokopedia.product_ar.view.fragment.ProductArFragment

class ProductArActivity : BaseSimpleActivity(), HasComponent<ProductArComponent> {

    companion object {
        const val SHOP_ID_EXTRA = "shopId"
    }

    private var productId: String = ""
    private var shopId: String = ""
    private var mMakeupEngine: MFEMakeupEngine? = null

    fun getMakeUpEngine(): MFEMakeupEngine? = mMakeupEngine

    fun goToArComparisonFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                parentViewResourceID,
                ProductArComparisonFragment.newInstance(),
                PRODUCT_AR_COMPARISON_FRAGMENT
            )
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun getLayoutRes(): Int = R.layout.activity_product_ar

    override fun getParentViewResourceID(): Int = R.id.product_ar_parent_view

    override fun getNewFragment(): Fragment = ProductArFragment.newInstance()

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            val fragment = supportFragmentManager.findFragmentByTag(PRODUCT_AR_COMPARISON_FRAGMENT)
            (fragment as? ProductArComparisonFragment)?.onBackPressed()
            supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun setupFragment(savedInstance: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    ProductArConstant.REQUEST_CODE_CAMERA_PERMISSION)
        } else {
            inflateFragment()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == ProductArConstant.REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                inflateFragment()
            } else {
                //Never ask again selected, or device policy prohibits the app from having that permission.
                Toast.makeText(this,
                        getString(R.string.product_ar_permission_rejected_message),
                        Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data

        uri?.let {
            productId = it.lastPathSegment.toString()
            shopId = intent.getStringExtra(SHOP_ID_EXTRA).orEmpty()
        }

        setupEngine()
    }

    private fun setupEngine() {
        mMakeupEngine = MFEMakeupEngine(this, MFEMakeupEngine.Region.US)
        mMakeupEngine?.setMakeupRenderingParameters(MFEMakeupRenderingParameters(false));
        mMakeupEngine?.loadResources(this, null)
    }

    override fun getComponent(): ProductArComponent {
        val baseComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerProductArComponent.builder()
                .baseAppComponent(baseComponent)
                .productArModule(ProductArModule(productId, shopId))
                .build()
    }

    override fun onDestroy() {
        mMakeupEngine?.close()
        mMakeupEngine = null
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}
