package com.tokopedia.oneclickcheckout.preference.edit.view.shipping

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditParent
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.fragment_shipping_duration.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShippingDurationFragment : BaseDaggerFragment(), ShippingDurationItemAdapter.OnShippingMenuSelected {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferenceListAnalytics: PreferenceListAnalytics

    private val viewModel: ShippingDurationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShippingDurationViewModel::class.java]
    }

    private val adapter = ShippingDurationItemAdapter(this)

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var tickerInfo: Ticker? = null
    private var shippingDurationList: RecyclerView? = null
    private var buttonSaveDuration: UnifyButton? = null
    private var bottomLayout: FrameLayout? = null

    private var contentLayout: Group? = null
    private var globalError: GlobalError? = null

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

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipping_duration, container, false)
    }

    override fun onSelect(selection: Int) {
        preferenceListAnalytics.eventClickOnDurasiOptionInPilihDurasiPengirimanPage(selection.toString())
        viewModel.setSelectedShipping(selection)
    }

    private fun initViewModel() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            if (parent.getShippingId() > 0) {
                viewModel.selectedId = parent.getShippingId()
            }
        }

        viewModel.shippingDuration.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OccState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalError?.gone()
                    contentLayout?.visible()

                    btn_save_duration.setOnClickListener {
                        val selectedId = viewModel.selectedId
                        if (selectedId > 0) {
                            preferenceListAnalytics.eventClickPilihMetodePembayaranInDuration(selectedId.toString())
                            goToNextStep()
                        }
                    }

                    adapter.renderData(it.data.services)
                }

                is OccState.Failed -> {
                    swipeRefreshLayout?.isRefreshing = false
                    it.getFailure()?.let { failure ->
                        handleError(failure.throwable)
                    }
                }

                is OccState.Loading -> swipeRefreshLayout?.isRefreshing = true
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initViews()
        initViewModel()
        checkEntryPoint()

        tickerInfo?.setTextDescription(getString(R.string.ticker_label_text))
        shippingDurationList?.adapter = adapter
        shippingDurationList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initViews() {
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        tickerInfo = view?.findViewById(R.id.ticker_info)
        shippingDurationList = view?.findViewById(R.id.shipping_duration_rv)
        buttonSaveDuration = view?.findViewById(R.id.btn_save_address)
        bottomLayout = view?.findViewById(R.id.bottom_layout_shipping)
        contentLayout = view?.findViewById(R.id.content_layout)
        globalError = view?.findViewById(R.id.global_error)
    }

    private fun checkEntryPoint() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            if (parent.getListShopShipment().isNullOrEmpty() && parent.getShippingParam() == null) {
                viewModel.getShippingDuration()
            } else {
                hitRates()
            }
        }
    }

    private fun hitRates() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            viewModel.getRates(parent.getListShopShipment(), parent.getShippingParam())
        }
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            val selectedId = viewModel.selectedId
            if (selectedId > 0) {
                parent.setShippingId(selectedId)
                if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
                    parent.goBack()
                } else {
                    parent.addFragment(PaymentMethodFragment.newInstance())
                }
            }

        }
    }

    private fun initHeader() {
        if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
            val parent = activity
            if (parent is PreferenceEditParent) {
                parent.hideStepper()
                parent.setHeaderTitle(getString(R.string.activity_title_shipping_duration))
                parent.hideDeleteButton()
                parent.hideAddButton()
            }
        } else {
            val parent = activity
            if (parent is PreferenceEditParent) {
                parent.hideDeleteButton()
                parent.hideAddButton()
                parent.showStepper()
                parent.setStepperValue(50)
                parent.setHeaderTitle(getString(R.string.activity_title_shipping_duration))
                parent.setHeaderSubtitle(getString(R.string.activity_subtitle_shipping_address))
            }
        }
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable?.message
                            ?: DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            checkEntryPoint()
        }
        globalError?.visible()
        contentLayout?.gone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        swipeRefreshLayout = null
        tickerInfo = null
        shippingDurationList = null
        buttonSaveDuration = null
        bottomLayout = null
        contentLayout = null
        globalError = null
    }
}