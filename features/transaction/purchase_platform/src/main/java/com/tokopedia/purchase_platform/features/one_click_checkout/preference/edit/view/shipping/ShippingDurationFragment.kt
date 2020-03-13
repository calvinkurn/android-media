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
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItem
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_shipping_duration.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShippingDurationFragment : BaseDaggerFragment(), ShippingDurationItemAdapter.OnShippingMenuSelected{

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
    @Inject
    lateinit var preferenceListAnalytics: PreferenceListAnalytics

    private val viewModel: ShippingDurationViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ShippingDurationViewModel::class.java]
    }

    val adapter = ShippingDurationItemAdapter(this)

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
        if (parent is PreferenceEditActivity) {
            if(parent.shippingId > 0) {
                viewModel.selectedId = parent.shippingId
            }
        }

        viewModel.shippingDuration.observe(this, Observer {
            when (it) {
                is OccState.Success -> {
                    swipe_refresh_layout.isRefreshing = false
                    global_error.gone()
                    content_layout.visible()

                    btn_save_duration.setOnClickListener {
                        val selectedId = viewModel.selectedId
                        if (selectedId > 0 ) {
                            preferenceListAnalytics.eventClickPilihMetodePembayaranInDuration(selectedId.toString())
                            goToNextStep()
                        }
                        /*if(arguments?.getBoolean(ARG_IS_EDIT) == false) {
                            goToNextStep()
                        } else {
                            goBack()
                        }*/
                    }

                    renderData(it.data.services)
                }

                is OccState.Fail -> {
                    if (!it.isConsumed) {
                        swipe_refresh_layout.isRefreshing = false
                        if (it.throwable != null) {
                            handleError(it.throwable)
                        }
                    }
                } else -> swipe_refresh_layout.isRefreshing = true
            }
        })
    }

    private fun renderData(data: List<ServicesItem>) {
        adapter.shippingDurationList.clear()
        adapter.shippingDurationList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initViewModel()
        checkEntryPoint()

        ticker_info.setTextDescription(getString(R.string.ticker_label_text))
        shipping_duration_rv.adapter = adapter
        shipping_duration_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun checkEntryPoint(){
        val parent = activity
        if(parent is PreferenceEditActivity) {
            if(parent.listShopShipment.isNullOrEmpty() && parent.shippingParam == null) {
                viewModel.getShippingDuration()
            } else {
                hitRates()
            }
        }
    }

    private fun hitRates(){
        val parent = activity
        if(parent is PreferenceEditActivity) {
            viewModel.getRates(parent.listShopShipment, parent.shippingParam)
        }
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            val selectedId = viewModel.selectedId
            if(selectedId > 0) {
                parent.shippingId = selectedId
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

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.make(it, "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.make(it, throwable.message
                            ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                }
            }
        }
        viewModel.consumeGetShippingDurationFail()
    }

    private fun showGlobalError(type: Int) {
        global_error.setType(type)
        global_error.setActionClickListener {
            viewModel.getShippingDuration()
        }
        global_error.visible()
        content_layout.gone()
    }

}