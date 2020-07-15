package com.tokopedia.withdraw.auto_withdrawal.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.di.component.AutoWithdrawalComponent
import com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel.AutoWDSettingsViewModel
import kotlinx.android.synthetic.main.swd_fragment_awd_settings.*
import javax.inject.Inject

class AutoWithdrawalSettingsFragment : BaseDaggerFragment() {
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val autoWDSettingsViewModel: AutoWDSettingsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(AutoWDSettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_fragment_awd_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAutoWDStatus()
    }

    private fun loadAutoWDStatus() {
        autoWDSettingsViewModel.getAutoWDStatus()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(AutoWithdrawalComponent::class.java).inject(this)
    }

    private fun observeViewModel() {
        autoWDSettingsViewModel.thanksPageDataResultLiveData.observe(this, Observer {
            when (it) {
               /* is Success -> onThankYouPageDataLoaded(it.data)
                is Fail -> onThankYouPageDataLoadingFail(it.throwable)*/
            }
        })
    }



    private fun showGlobalError(errorType: Int, retryAction: () -> Unit) {
        globalError.visible()
        globalError.setType(errorType)
        globalError.errorAction.visible()
        globalError.errorAction.setOnClickListener {
            showLoaderView()
            retryAction.invoke()
        }
    }

    private fun showLoaderView() {

    }

    companion object {

        fun getInstance(bundle: Bundle): AutoWithdrawalSettingsFragment = AutoWithdrawalSettingsFragment().apply {
            arguments = bundle
        }
    }
}