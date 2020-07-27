package com.tokopedia.seller_migration_common.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_migration_common.presentation.fragment.SellerMigrationFragment

class SellerMigrationActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
    }

    override fun getNewFragment(): Fragment {
        val featureName = intent.data?.getQueryParameter(SellerMigrationFragment.KEY_PARAM_FEATURE_NAME).orEmpty()
        return SellerMigrationFragment.createInstance(featureName)
    }
}