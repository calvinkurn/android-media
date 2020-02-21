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
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import kotlinx.android.synthetic.main.fragment_shipping_duration.*
import javax.inject.Inject

class ShippingDurationFragment : BaseDaggerFragment(){

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
            adapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        shipping_duration_rv.adapter = adapter
        shipping_duration_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

//        item_shipping_radio.isChecked = !item_shipping_radio.isChecked
    }

}