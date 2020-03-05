package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ServicesItemModelNoPrice
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.payment.PaymentMethodFragment
import kotlinx.android.synthetic.main.fragment_shipping_duration.*
import javax.inject.Inject

class ShippingDurationFragment : BaseDaggerFragment(){

    companion object {
        private const val ARG_IS_EDIT = "is_edit"

        fun newInstance(isEdit: Boolean = false): ShippingDurationFragment {
            val shippingDurationFragment = ShippingDurationFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            shippingDurationFragment.arguments = bundle
            return shippingDurationFragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ShippingDurationViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ShippingDurationViewModel::class.java]
    }

    val adapter = ShippingDurationItemAdapter()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipping_duration, container, false)
    }

    private fun initViewModel(){
        viewModel.shippingDuration.observe(this, Observer {
            renderData(it.services)
        })
    }

    private fun renderData(data: List<ServicesItemModelNoPrice>){
        adapter.shippingDurationList.clear()
        adapter.shippingDurationList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initViewModel()
        viewModel.getShippingDuration()

        ticker_info.setTextDescription(getString(R.string.ticker_label_text))
        shipping_duration_rv.adapter = adapter
        shipping_duration_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        btn_save_duration.setOnClickListener {
            goToNextStep()
        }
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
                parent.goBack()
            } else {
                parent.addFragment(PaymentMethodFragment.newInstance())
            }
        }
    }

    private fun initHeader() {
        if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                parent.hideStepper()
                parent.setHeaderTitle(getString(R.string.activity_title_shipping_duration))
                parent.hideDeleteButton()
                parent.hideAddButton()
            }
        } else {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                parent.hideDeleteButton()
                parent.hideAddButton()
                parent.showStepper()
                parent.setStepperValue(50, true)
                parent.setHeaderTitle(getString(R.string.activity_title_shipping_duration))
                parent.setHeaderSubtitle(getString(R.string.activity_subtitle_shipping_address))
            }
        }
    }

}