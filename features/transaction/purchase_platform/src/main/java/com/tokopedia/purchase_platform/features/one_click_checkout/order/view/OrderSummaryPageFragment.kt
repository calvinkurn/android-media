package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.di.OrderSummaryPageComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.OrderPriceSummaryBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.PreferenceListBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderPreferenceCard
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderProductCard
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import kotlinx.android.synthetic.main.fragment_order_summary_page.*
import javax.inject.Inject

class OrderSummaryPageFragment : BaseDaggerFragment(), OrderProductCard.OrderProductCardListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: OrderSummaryPageViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[OrderSummaryPageViewModel::class.java]
    }

    private lateinit var orderProductCard: OrderProductCard
    private lateinit var orderPreferenceCard: OrderPreferenceCard

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(OrderSummaryPageComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CREATE_PREFERENCE || requestCode == REQUEST_EDIT_PREFERENCE) {
            showPreferenceListBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_summary_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view: View) {
        btn_order_detail.setOnClickListener {
            OrderPriceSummaryBottomSheet().show(this)
        }

        orderProductCard = OrderProductCard(view, this)
        orderProductCard.setProduct(OrderProduct())
        orderProductCard.initView()

        orderPreferenceCard = OrderPreferenceCard(view, this, getOrderPreferenceCardListener())
        orderPreferenceCard.initView()
    }

    private fun getOrderPreferenceCardListener() = object : OrderPreferenceCard.OrderPreferenceCardListener {

        override fun onChangePreferenceClicked() {
            showPreferenceListBottomSheet()
        }
    }

    fun showPreferenceListBottomSheet() {
        PreferenceListBottomSheet(listener = object : PreferenceListBottomSheet.PreferenceListBottomsheetListener {
            override fun onChangePreference(preference: Preference) {
                viewModel.updatePreference(preference)
            }

            override fun onEditPreference(preference: Preference) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
                intent.apply {
                    putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, 1)
                    putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, 1)
                    putExtra(PreferenceEditActivity.EXTRA_PAYMENT_ID, 1)
                }
                startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
            }

            override fun onAddPreference() {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
                startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
            }
        }).show(this@OrderSummaryPageFragment)
    }

    override fun onProductChange(product: OrderProduct) {
        viewModel.updateProduct(product)
    }

    companion object {

        const val REQUEST_EDIT_PREFERENCE = 11
        const val REQUEST_CREATE_PREFERENCE = 12
    }
}