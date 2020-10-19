package com.tokopedia.entertainment.pdp.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.activity.EventRedeemActivity.Companion.EXTRA_URL_REDEEM
import com.tokopedia.entertainment.pdp.common.util.EventDateUtil
import com.tokopedia.entertainment.pdp.common.util.EventDateUtil.stringToDateRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventRedeemViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_event_redeem.*
import javax.inject.Inject

class EventRedeemFragment : BaseDaggerFragment() {

    @Inject
    lateinit var eventRedeemViewModel: EventRedeemViewModel

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    lateinit var progressDialog: ProgressDialog

    private var urlRedeem: String? = ""
    private var isSuccessRedeem: Boolean = false

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_redeem, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urlRedeem = arguments?.getString(EXTRA_URL_REDEEM, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(userSessionInterface.isLoggedIn && urlRedeem != "") {
            initProgressDialog()
            requestData()
        } else if (!userSessionInterface.isLoggedIn) {
            renderNotLogin()
        } else if (urlRedeem == ""){
            renderUrlNull()
        }

        btn_redeem_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        eventRedeemViewModel.eventRedeem.observe(viewLifecycleOwner, Observer {
            it.run {
                renderLayout(it)
            }
        })

        eventRedeemViewModel.eventRedeemed.observe(viewLifecycleOwner, Observer {
            it.run {
                progressDialog.dismiss()
                if(it.data.invoice.providerInvoiceStatus.equals("SUCCESS")){
                    urlRedeem?.let {
                        isSuccessRedeem = true
                        eventRedeemViewModel.getDataRedeem(it)
                    }
                }
            }
        })

        eventRedeemViewModel.isError.observe(viewLifecycleOwner, Observer {
            it?.let {
                progressDialog.dismiss()
                NetworkErrorHelper.showEmptyState(context, view?.rootView, it.message) {
                    requestData()
                }
            }
        })
    }

    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.ent_checkout_payment))
        progressDialog.setCancelable(false)
    }

    private fun requestData() {
        urlRedeem?.let {
            eventRedeemViewModel.getDataRedeem(it)
        }
    }

    private fun renderLayout(eventRedeem: EventRedeem) {
        pg_event_redeem.gone()
        ll_parent_redeem.show()

        eventRedeem.data.apply {
            tg_url_redeem_name.text = user.name
            tg_event_redeem_total.text = quantity.toString()

            if (action.isNotEmpty()) {
                if (action.first().urlParams.appUrl.isEmpty()) {
                    card_redeemed.show()
                    tg_event_redeem_redeemed_title.text = if(isSuccessRedeem) resources.getString(R.string.ent_redeem_data_redeemed)
                                                            else resources.getString(R.string.ent_redeem_data_already_redeemed)
                    tg_event_redeem_time.text =  resources.getString(R.string.ent_redeemed_time, stringToDateRedeem(product.updatedAt))
                    btn_redeem_ticket.gone()
                }
            }

            tg_event_redeem_product.text = product.displayName
            tg_event_redeem_package.text = schedule.name
            tg_event_redeem_schedule.text = schedule.showData

            btn_redeem_ticket.apply {
                setOnClickListener {
                    progressDialog.show()
                    eventRedeemViewModel.redeemData(action.first().urlParams.appUrl)
                }
            }

        }
    }

    private fun renderError(){
        pg_event_redeem.gone()
        ll_parent_redeem.show()
        btn_redeem_ticket.gone()
        tg_url_redeem_name.gone()
        tg_event_redeem_total.gone()
        tg_event_redeem_package.gone()
        tg_event_redeem_schedule.gone()
        tg_event_redeem_title.gone()
        tg_event_redeem_total_title.gone()
    }

    private fun renderNotLogin(){
        renderError()
        tg_event_redeem_product.text = resources.getString(R.string.ent_redeem_not_login)
    }

    private fun renderUrlNull(){
        renderError()
        tg_event_redeem_product.text = resources.getString(R.string.ent_redeem_url_null)
    }

    companion object {
        fun newInstance(urlRedeem: String) = EventRedeemFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_REDEEM, urlRedeem)
            }
        }
    }
}