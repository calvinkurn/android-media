package com.tokopedia.pms.payment.view.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.di.PaymentListComponent
import com.tokopedia.pms.paymentlist.viewmodel.PaymentListViewModel
import javax.inject.Inject

class PaymentListFragmentK:  BaseDaggerFragment(){

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: PaymentListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PaymentListViewModel::class.java)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(PaymentListComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_payment_list, container, false)
        viewModel.getPaymentList("")
        return view
    }

    companion object {
        fun createInstance() = PaymentListFragmentK()
    }

}