package com.tokopedia.pdpsimulation.activateCheckout.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import javax.inject.Inject


class ActivationCheckoutFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterActivationViewModel: PayLaterActivationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(PayLaterActivationViewModel::class.java)
    }


    override fun initInjector() = getComponent(PdpSimulationComponent::class.java).inject(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_activation_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observerData()

    }

    private fun observerData() {
        TODO("Not yet implemented")
    }

    private fun initView() {

    }

    override fun getScreenName(): String {
        return "Activation PayLater"
    }


}
