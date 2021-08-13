package com.tokopedia.pdpsimulation.creditcard.presentation.tnc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardPdpMetaData
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.adapter.CreditCardTncAdapter
import com.tokopedia.pdpsimulation.creditcard.viewmodel.CreditCardViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_credit_card_tnc.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CreditCardTncFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val creditCardViewModel: CreditCardViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(CreditCardViewModel::class.java)
    }

    private var pdpSimulationCallback: PdpSimulationCallback? = null

    override fun initInjector() {
        getComponent(PdpSimulationComponent::class.java).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_credit_card_tnc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
        creditCardViewModel.getCreditCardTncData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPdpInfo.adapter = CreditCardTncAdapter {
            openUrlWebView(creditCardViewModel.getRedirectionUrl())
        }
        rvPdpInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModel() {
        creditCardViewModel.creditCardPdpMetaInfoLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onPdpInfoMetaDataLoaded(it.data)
                is Fail -> onPdpInfoMetaDataLoadingFailed(it.throwable)
            }
        })
    }

    override fun getScreenName(): String {
        return "Detail Penawaran"
    }

    private fun onPdpInfoMetaDataLoaded(data: CreditCardPdpMetaData) {
        rvPdpInfo.visible()
        // pdpInfoContentList is non nullable here in case of Success
        (rvPdpInfo.adapter as CreditCardTncAdapter).setData(
                data.pdpInfoContentList ?: arrayListOf(), data.ctaRedirectionLabel)
    }

    private fun onPdpInfoMetaDataLoadingFailed(throwable: Throwable) {
        rvPdpInfo.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                pdpSimulationCallback?.showNoNetworkView()
                return
            }
            is IllegalStateException -> {
                setGlobalErrors(GlobalError.PAGE_FULL)
            }
            else -> {
                setGlobalErrors(GlobalError.SERVER_ERROR)
            }
        }
    }

    private fun setGlobalErrors(errorType: Int) {
        tncGlobalError.setType(errorType)
        tncGlobalError.show()
        tncGlobalError.setActionClickListener {
            tncGlobalError.gone()
            creditCardViewModel.getCreditCardTncData()
        }
    }

    private fun openUrlWebView(urlString: String) {
        if (urlString.isNotEmpty()) {
            val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + urlString
            RouteManager.route(context, webViewAppLink)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(pdpSimulationCallback: PdpSimulationCallback): CreditCardTncFragment {
            val tncFragment = CreditCardTncFragment()
            tncFragment.pdpSimulationCallback = pdpSimulationCallback
            return tncFragment
        }
    }
}