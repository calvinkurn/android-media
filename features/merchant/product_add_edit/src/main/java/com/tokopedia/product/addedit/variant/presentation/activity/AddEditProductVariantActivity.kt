package com.tokopedia.product.addedit.variant.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantModule
import com.tokopedia.product.addedit.variant.di.DaggerAddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.fragment.AddEditProductVariantFragment

class AddEditProductVariantActivity: BaseSimpleActivity(), HasComponent<AddEditProductVariantComponent> {

    companion object {
        fun createInstance(context: Context?, cacheManagerId: String?): Intent =
                Intent(context, AddEditProductVariantActivity::class.java)
                        .putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)

    }

    override fun getNewFragment(): Fragment {
        val cacheManagerId = intent?.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        return AddEditProductVariantFragment.createInstance(cacheManagerId)
    }

    override fun getComponent(): AddEditProductVariantComponent {
        return DaggerAddEditProductVariantComponent
                .builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductVariantModule(AddEditProductVariantModule())
                .build()
    }

    override fun onBackPressed() {
        val f = fragment
        if (f!= null && f is AddEditProductVariantFragment) {
            f.onBackPressed()
        }
    }
}
