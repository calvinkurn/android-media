package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent

/**
 * Created by Rafli Syam on 29/04/2021
 */
class ShopSettingsSetOperationalHoursFragment : BaseDaggerFragment(), HasComponent<ShopSettingsComponent> {

    companion object {

        @LayoutRes
        val FRAGMENT_LAYOUT = R.layout.fragment_shop_settings_set_operational_hours

        @JvmStatic
        fun createInstance(): ShopSettingsSetOperationalHoursFragment = ShopSettingsSetOperationalHoursFragment()

    }

    private var opsHourAccordion: AccordionUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(FRAGMENT_LAYOUT, container, false).apply {
            initView(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackgroundColor()
        setAccordion()
    }

    override fun getComponent(): ShopSettingsComponent? {
        return activity?.run {
            DaggerShopSettingsComponent
                    .builder()
                    .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }

    override fun getScreenName(): String {
        return ShopSettingsSetOperationalHoursFragment::class.java.simpleName
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun initView(view: View) {
        with(view) {
            opsHourAccordion = findViewById(R.id.shop_ops_hour_list_accordion)
        }
    }

    private fun setBackgroundColor() {
        activity?.run {
            window.decorView.setBackgroundColor(
                    androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        }
    }

    private fun setAccordion() {
        val listOpsHour = listOf<ShopOperationalHour>(
                ShopOperationalHour(
                        day = 1,
                        startTime = "Buka 24 Jam"
                ),
                ShopOperationalHour(
                        day = 2,
                        startTime = "Buka 24 Jam"
                ),
                ShopOperationalHour(
                        day = 3,
                        startTime = "Buka 24 Jam"
                ),
                ShopOperationalHour(
                        day = 4,
                        startTime = "Buka 24 Jam"
                ),
                ShopOperationalHour(
                        day = 5,
                        startTime = "Buka 24 Jam"
                ),
                ShopOperationalHour(
                        day = 6,
                        startTime = "Buka 24 Jam"
                ),
                ShopOperationalHour(
                        day = 7,
                        startTime = "Buka 24 Jam"
                )
        )
        listOpsHour.forEach {
            opsHourAccordion?.addGroup(AccordionDataUnify(
                    title = "Senin",
                    subtitle = it.startTime,
                    expandableView = View.inflate(context, R.layout.item_shop_set_ops_hour_accordion, null)
            ))
        }
    }
}