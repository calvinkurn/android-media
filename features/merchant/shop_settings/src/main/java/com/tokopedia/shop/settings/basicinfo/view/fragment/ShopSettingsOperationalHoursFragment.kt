package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopSettingsSetOperationalHoursActivity
import com.tokopedia.shop.settings.basicinfo.view.model.ShopOperationalHourUiModel
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsOperationalHoursViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

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

    @Inject
    lateinit var shopSettingsOperationalHoursViewModel: ShopSettingsOperationalHoursViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var headerOpsHour: HeaderUnify? = null
    private var icEditOpsHour: IconUnify? = null
    private var tvShopOperationalHoursList: Typography? = null

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
        observeLiveData()
        getShopOperationalHoursList(userSession.shopId)
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
        tvShopOperationalHoursList = view?.findViewById(R.id.tv_shop_operational_hours_list)
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

    private fun observeLiveData() {

        // observe get shop operational hours list data
        observe(shopSettingsOperationalHoursViewModel.shopOperationalHoursListData) { result ->
            if (result is Success) {
                val opsHourListMap = result.data.groupBy { it.operationalHours }
                renderShopOperationalHoursList(opsHourListMap)
            }
        }

    }

    private fun getShopOperationalHoursList(shopId: String) {
        shopSettingsOperationalHoursViewModel.getShopOperationalHoursList(shopId)
    }

    private fun renderShopOperationalHoursList(opsHoursListMap: Map<String, List<ShopOperationalHourUiModel>>) {
        val stringBuilder = SpannableStringBuilder()
        for ((key, value) in opsHoursListMap) {
            // append day name list
            value.forEachIndexed { index, opsHour ->
                if (index != value.lastIndex) {
                    stringBuilder.append("${opsHour.dayName}, ")
                }
                else {
                    stringBuilder.append(opsHour.dayName)
                }
            }
            stringBuilder.append(" - ")
            context?.let {
                val opsHour = HtmlLinkHelper(it, "<b>$key</b>")
                stringBuilder.append("${opsHour.spannedString}\n")
                opsHour.spannedString = ""
            }
        }
        tvShopOperationalHoursList?.text = stringBuilder
    }
}