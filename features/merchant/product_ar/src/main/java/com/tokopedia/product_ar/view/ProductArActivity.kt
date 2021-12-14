package com.tokopedia.product_ar.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.modiface.mfemakeupkit.MFEMakeupEngine
import com.modiface.mfemakeupkit.data.MFEMakeupRenderingParameters
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product_ar.di.DaggerProductArComponent
import com.tokopedia.product_ar.di.ProductArComponent
import com.tokopedia.product_ar.di.ProductArModule
import java.util.ArrayList

class ProductArActivity : BaseSimpleActivity(), HasComponent<ProductArComponent>, MFEMakeupEngine.MFEMakeupEngineErrorCallback {

    companion object {
        const val SHOP_ID_EXTRA = "shopId"
    }

    private var productId: String = ""
    private var shopId: String = ""

    private var mMakeupEngine: MFEMakeupEngine? = null

    fun getMakeUpEngine(): MFEMakeupEngine? = mMakeupEngine

    override fun getNewFragment(): Fragment = ProductArFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data

        uri?.let {
            productId = it.lastPathSegment.toString()
            shopId = intent.getStringExtra(SHOP_ID_EXTRA) ?: ""
        }

        mMakeupEngine = MFEMakeupEngine(this, MFEMakeupEngine.Region.US, this)
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

    override fun onMakeupEngineError(p0: MFEMakeupEngine.ErrorSeverity, p1: MFEMakeupEngine.ErrorType, p2: ArrayList<Throwable>) {
        Log.e("errornya", "ini ${p1.name}")
    }
}