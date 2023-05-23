package com.tokopedia.shop.settings.setting.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.databinding.ActivityShopNewInfoBinding
import com.tokopedia.shop.settings.setting.view.fragment.ShopPageSettingFragment

class ShopPageSettingActivity : BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {

    var binding : ActivityShopNewInfoBinding? = null

    override fun getNewFragment() = ShopPageSettingFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_shop_new_info

    override fun getComponent() = DaggerShopSettingsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    override fun setupLayout(savedInstanceState: Bundle?) {
        binding = ActivityShopNewInfoBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.toolbar?.apply {
            setSupportActionBar(this)
            setTitleTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window?.decorView?.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as? ShopPageSettingFragment)?.onBackPressed()
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }
}
