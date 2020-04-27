package com.tokopedia.product.addedit.detail.presentation.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailModule
import com.tokopedia.product.addedit.detail.di.DaggerAddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel

class AddEditProductDetailActivity : BaseSimpleActivity(), HasComponent<AddEditProductDetailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
        }
    }

    override fun getNewFragment(): Fragment? {

        // setup cache manager
        val cacheManagerId = intent?.getStringExtra(EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(this, cacheManagerId)

        // extras that will be passed to fragment
        var productInputModel = ProductInputModel()
        var isEditing = false
        var isAdding = false

        // try to get passed extras from the cache manager
        cacheManagerId?.run {
            productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                    ?: ProductInputModel()
            isEditing = saveInstanceCacheManager.get(EXTRA_IS_EDITING_PRODUCT, Boolean::class.java)
                    ?: false
            isAdding = saveInstanceCacheManager.get(EXTRA_IS_ADDING_PRODUCT, Boolean::class.java)
                    ?: false
        }
        return AddEditProductDetailFragment.createInstance(productInputModel, isEditing, isAdding)
    }

    override fun getComponent(): AddEditProductDetailComponent {
        return DaggerAddEditProductDetailComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(application))
                .addEditProductDetailModule(AddEditProductDetailModule())
                .build()
    }

    override fun onBackPressed() {
        onBackPressedHitTracking()
        sendDataBack()
    }

    private fun sendDataBack() {
        val f = fragment
        if (f != null && f is AddEditProductDetailFragment) {
            f.sendDataBack()
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductDetailFragment) {
            f.onBackPressed()
        }
    }
}