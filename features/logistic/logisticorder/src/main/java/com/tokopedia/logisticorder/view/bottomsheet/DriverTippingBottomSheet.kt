package com.tokopedia.logisticorder.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticorder.databinding.BottomsheetTippingGojekBinding
import com.tokopedia.logisticorder.di.DaggerTrackingPageComponent
import com.tokopedia.logisticorder.di.TrackingPageComponent
import com.tokopedia.logisticorder.uimodel.LogisticDriverModel
import com.tokopedia.logisticorder.view.TrackingPageViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class DriverTippingBottomSheet: BottomSheetUnify(), HasComponent<TrackingPageComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<BottomsheetTippingGojekBinding>()
    private val viewModel: TrackingPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TrackingPageViewModel::class.java]
    }

    private var orderId: String = ""

    init {
        setOnDismissListener {
            dismiss()
        }

        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialViewState()
        initObserver()
    }

   private fun iniInjector() {
       component.inject(this)
   }

    private fun initChildLayout() {
        binding = BottomsheetTippingGojekBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    private fun setInitialViewState() {
        setTitle("")
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getDriverTipsData(orderId)
    }

    override fun getComponent(): TrackingPageComponent {
        return DaggerTrackingPageComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, "")
        }
    }

    fun setOrderId(orderId: String) {
        this.orderId = orderId
    }

    private fun initObserver() {
        viewModel.driverTipData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    setDriverTipLayout(it.data)
                }

                is Fail -> {
                }
            }
        })
    }

    private fun setDriverTipLayout(logisticDriverModel: LogisticDriverModel) {
        if (logisticDriverModel.status == 100) {
            //driver found
        } else if (logisticDriverModel.status == 150)  {
            //tip sedang diproses
        } else if (logisticDriverModel.status == 200) {
            //post payment
        }
    }
}