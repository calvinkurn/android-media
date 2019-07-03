package com.tokopedia.ovop2p.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.util.AnalyticsUtil
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.interfaces.ActivityListener
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener
import com.tokopedia.ovop2p.viewmodel.OvoP2pTxnThankYouOvoUsrVM

class TxnSucsOvoUser : BaseDaggerFragment(), View.OnClickListener {

    private lateinit var date: TextView
    private lateinit var trnsfrAmt: TextView
    private lateinit var infoDesc: TextView
    private lateinit var sndrName: TextView
    private lateinit var sndrNum: TextView
    private lateinit var rcvrName: TextView
    private lateinit var rcvrNum: TextView
    private lateinit var seeDtl: TextView
    private lateinit var backToApp: Button
    private lateinit var infoIcon: ImageView
    private var transferId: String = ""
    private lateinit var txnThankYouPageVM: OvoP2pTxnThankYouOvoUsrVM
    private lateinit var thankYouDataCntnr: OvoP2pTransferThankyouBase
    private lateinit var errorSnackbar: Snackbar


    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return Constants.ScreenName.FRAGMENT_THANKYOU_OVO_USER
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.transaction_success_page, container, false)
        date = view.findViewById(R.id.date)
        trnsfrAmt = view.findViewById(R.id.trnsfr_amt)
        trnsfrAmt.setOnClickListener(this)
        infoDesc = view.findViewById(R.id.info_desc)
        infoDesc.setOnClickListener(this)
        sndrName = view.findViewById(R.id.name_sender)
        sndrNum = view.findViewById(R.id.num_sender)
        rcvrName = view.findViewById(R.id.name_rcvr)
        rcvrNum = view.findViewById(R.id.num_rcvr)
        seeDtl = view.findViewById(R.id.see_dtl)
        seeDtl.setOnClickListener(this)
        backToApp = view.findViewById(R.id.back_to_app)
        backToApp.setOnClickListener(this)
        infoIcon = view.findViewById(R.id.info_icon)
        infoIcon.setOnClickListener(this)
        createAndSubscribeToThankYouVM()
        getTransferid()
        return view
    }

    private fun setRcvrUserData() {
        rcvrName.text = arguments?.getString(Constants.Keys.RECIEVER_NAME) ?: ""
        rcvrNum.text = "Ovo - " + arguments?.getString(Constants.Keys.RECIEVER_PHONE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateHeader()
        if (!TextUtils.isEmpty(transferId)) {
            var dataMap: HashMap<String, Any> = HashMap()
            dataMap.put(Constants.Keys.TRANSFER_ID, transferId.toInt())
            context?.let {
                (activity as LoaderUiListener).showProgressDialog()
                txnThankYouPageVM.makeThankyouDataCall(it, dataMap)
            }
        }
    }

    private fun getTransferid() {
        transferId = arguments?.getString(Constants.Keys.TRANSFER_ID) ?: ""
    }

    private fun createAndSubscribeToThankYouVM() {
        if (!::txnThankYouPageVM.isInitialized) {
            if (activity != null) {
                txnThankYouPageVM = ViewModelProviders.of(this.activity!!).get(OvoP2pTxnThankYouOvoUsrVM::class.java)
                txnThankYouPageVM.ovoP2pTransferThankyouBaseMutableLiveData?.observe(this, Observer<OvoP2pTransferThankyouBase> {
                    if (it != null) {
                        (activity as LoaderUiListener).hideProgressDialog()
                        if (it.ovoP2pTransferThankyou.errors?.isEmpty()!!) {
                            assignThankYouData(it)
                        } else {
                            var errMsg = it.ovoP2pTransferThankyou.errors!!.get(0).get(Constants.Keys.MESSAGE)
                            errMsg?.let { it1 -> gotoErrorPage(it1) }
                        }
                    } else {
                        showErrorSnackBar()
                    }
                })
            }
        }
    }

    private fun gotoErrorPage(errMsg: String) {
        var fragment: BaseDaggerFragment = TransferError.createInstance()
        var bundle = Bundle()
        bundle.putString(Constants.Keys.ERR_MSG_ARG, errMsg)
        fragment.arguments = bundle
        (activity as ActivityListener).addReplaceFragment(fragment, false, TransferError.TAG)
    }

    private fun updateHeader() {
        (activity as LoaderUiListener).setHeaderTitle(Constants.Headers.TRANSFER_SUCCESS)
    }

    private fun assignThankYouData(thankYouData: OvoP2pTransferThankyouBase) {
        thankYouDataCntnr = thankYouData
        date.text = thankYouData.ovoP2pTransferThankyou.trnsfrDate
        val rpFormattedString = CurrencyFormatUtil.getThousandSeparatorString(thankYouData.ovoP2pTransferThankyou.amt.toDouble(), false, 0)
        trnsfrAmt.text = rpFormattedString.formattedString
        var sourceName = thankYouData.ovoP2pTransferThankyou.source.name
        if (TextUtils.isEmpty(sourceName)) {
            sourceName = context?.let {
                OvoP2pUtil.getUserName(it)
            }.toString()
            thankYouDataCntnr.ovoP2pTransferThankyou.source.name = sourceName
        }
        sndrName.text = sourceName
        sndrNum.text = "Ovo - " + thankYouData.ovoP2pTransferThankyou.source.phone
        setRcvrUserData()
    }

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if (id != -1) {
            when (id) {
                R.id.see_dtl -> {
                    //go to see detail fragment
                    var bundle = Bundle()
                    bundle.putSerializable(Constants.Keys.THANKYOU_ARGS, thankYouDataCntnr)
                    bundle.putString(Constants.Keys.RECIEVER_NAME, rcvrName.text.toString())
                    bundle.putSerializable(Constants.Keys.RECIEVER_PHONE, rcvrNum.text.toString())
                    (activity as ActivityListener).addReplaceFragment(TxnDetails.newInstance(bundle), true,
                            TxnDetails.TAG)
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_SUMRY_TRNSFR_SUCS, "", AnalyticsUtil.EventAction.CLK_SEE_DTL)
                    }
                }
                R.id.back_to_app -> {
                    activity?.finish()
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_SUMRY_TRNSFR_SUCS, "", AnalyticsUtil.EventAction.CLK_KMBL_TKPD)
                    }
                }
                R.id.btn_ok -> {
                    errorSnackbar.let {
                        if (it.isShownOrQueued) it.dismiss()
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): TxnSucsOvoUser {
            return TxnSucsOvoUser()
        }

        fun newInstance(bundle: Bundle): TxnSucsOvoUser {
            val fragmentSucsOvoUsr = newInstance()
            fragmentSucsOvoUsr.setArguments(bundle)
            return fragmentSucsOvoUsr
        }
    }

    private fun showErrorSnackBar() {
        activity?.let {
            errorSnackbar = OvoP2pUtil.createErrorSnackBar(it, this, Constants.Messages.GENERAL_ERROR)
            errorSnackbar.show()
        }
    }
}