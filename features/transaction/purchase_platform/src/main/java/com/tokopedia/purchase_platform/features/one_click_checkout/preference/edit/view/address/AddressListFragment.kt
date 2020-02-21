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
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import kotlinx.android.synthetic.main.fragment_choose_address.*
import javax.inject.Inject

class AddressListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AddressListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AddressListViewModel::class.java]
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

        btn_save_address.setOnClickListener {
            if(empty_state_order_list.visibility == View.VISIBLE) {
                address_list_layout.visibility = View.VISIBLE
                empty_state_order_list.visibility = View.GONE
            } else {
  /*              val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_layout_id, fragment)
                transaction.commit()
                supportFragmentManager.beginTransaction().replace(R.id.container, ShippingDurationFragment()).commit()*/
/*
                address_list_layout.visibility = View.GONE
                empty_state_order_list.visibility = View.VISIBLE*/
            }
        }

        address_list_rv.adapter = adapter
        address_list_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

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
        }
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.addFragment(ShippingDurationFragment())
            parent.setStepperValue(50, true)
        }
    }

}