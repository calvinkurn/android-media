package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopSettingsSetOperationalHoursActivity
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent

/**
 * Created by Rafli Syam on 28/04/2021
 */
class ShopSettingsOperationalHoursFragment : BaseDaggerFragment(), HasComponent<ShopSettingsComponent> {

    companion object {

        @JvmStatic
        fun createInstance(): ShopSettingsOperationalHoursFragment = ShopSettingsOperationalHoursFragment()

        @LayoutRes
        val FRAGMENT_LAYOUT = R.layout.fragment_shop_settings_operational_hours

    }

    private var headerOpsHour: HeaderUnify? = null
    private var icEditOpsHour: IconUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(FRAGMENT_LAYOUT, container, false).apply {
            initView(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setBackgroundColor()
        initListener()
    }

    override fun getScreenName(): String {
        return ShopSettingsOperationalHoursFragment::class.java.simpleName
    }

    override fun getComponent(): ShopSettingsComponent? {
        return activity?.run {
            DaggerShopSettingsComponent
                    .builder()
                    .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun initView(view: View?) {
        headerOpsHour = view?.findViewById(R.id.header_shop_operational_hours)
        icEditOpsHour = view?.findViewById(R.id.ic_edit_ops_hour)
    }

    private fun initListener() {
        // set click listener for icon edit ops hour
        icEditOpsHour?.setOnClickListener {
            activity?.run {
                startActivity(Intent(this, ShopSettingsSetOperationalHoursActivity::class.java))
            }
        }
    }

    private fun setupToolbar() {
        headerOpsHour?.addRightIcon(R.drawable.ic_ops_hour_help)
    }

    private fun setBackgroundColor() {
        activity?.run {
            window.decorView.setBackgroundColor(
                    androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        }
    }
}