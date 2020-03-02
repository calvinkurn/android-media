package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationFragment
import kotlinx.android.synthetic.main.fragment_choose_address.*
import javax.inject.Inject

class AddressListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AddressListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AddressListViewModel::class.java]
    }

    companion object {
        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
    }

    val adapter = AddressListItemAdapter()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_address, container, false)
    }

    private fun initViewModel(){
        viewModel.addressList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        if(empty_state_order_list.visibility == View.GONE){
            btn_save_address.text = getString(R.string.label_button_input_address)
            btn_save_address.setOnClickListener {
                goToNextStep()
            }
        } else {
            address_list_layout.visibility = View.GONE
            empty_state_order_list.visibility = View.VISIBLE
            btn_save_address.text = getString(R.string.label_button_input_address_empty)
            btn_save_address.setOnClickListener {
                goToPickLocation()
            }
        }

        address_list_rv.adapter = adapter
        address_list_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    private fun goToPickLocation(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
        intent.putExtra(EXTRA_IS_FULL_FLOW, true)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        setStep()
    }

    private fun setStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.showStepper()
            parent.setStepperValue(25, true)
            parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
            parent.setHeaderSubtitle(getString(R.string.activity_subtitle_choose_address))
        }
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.addFragment(ShippingDurationFragment())
        }
    }

}