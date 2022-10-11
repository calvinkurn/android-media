package com.tokopedia.ovop2p.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.util.AnalyticsUtil
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.interfaces.ActivityListener
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrPage
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrSnkBar
import com.tokopedia.ovop2p.view.viewStates.ThankYouSucs
import com.tokopedia.ovop2p.viewmodel.OvoP2PTransactionThankYouVM
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

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
    private lateinit var thankYouDataCntnr: OvoP2pTransferThankyouBase
    private lateinit var errorSnackbar: Snackbar


    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val ovoP2PTransactionThankYouVM: OvoP2PTransactionThankYouVM by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider =
            ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(OvoP2PTransactionThankYouVM::class.java)
    }


    override fun initInjector() {
        getComponent(OvoP2pTransferComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return Constants.ScreenName.FRAGMENT_THANKYOU_OVO_USER
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.transaction_success_page, container, false)
        initViews(view)
        createAndSubscribeToThankYouVM()
        getTransferid()
        return view
    }

    private fun initViews(view: View) {
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
    }

    private fun setRcvrUserData() {
        thankYouDataCntnr.ovoP2pTransferThankyou?.destination.let {
            rcvrName.text = it?.name?:""
            var number = it?.phone?:""
            if (!TextUtils.isEmpty(number)) {
                number = Constants.Prefixes.OVO + number
            } else {
                number = ""
            }
            rcvrNum.text = number
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateHeader()
        if (!TextUtils.isEmpty(transferId)) {
            var dataMap: HashMap<String, Any> = HashMap()
            dataMap.put(Constants.Keys.TRANSFER_ID, transferId.toIntOrZero())
            context?.let {
                activity?.let {
                    (it as LoaderUiListener).showProgressDialog()
                }
            }
            ovoP2PTransactionThankYouVM.makeThankyouDataCall(dataMap)

        }
    }

    private fun getTransferid() {
        transferId = arguments?.getString(Constants.Keys.TRANSFER_ID) ?: ""
    }

    private fun createAndSubscribeToThankYouVM() =
        ovoP2PTransactionThankYouVM.transferThankyouLiveData.observe(
            viewLifecycleOwner){
            hideLoader()
            when(it)
            {
                is ThankYouErrPage ->  gotoErrorPage(it.errMsg)
                is ThankYouErrSnkBar ->  showErrorSnackBar(it.errMsg)
                is ThankYouSucs ->  assignThankYouData(it.thankyouBase)
            }
        }


    private fun hideLoader()
    {
        activity?.let {
            (it as LoaderUiListener).hideProgressDialog()
        }
    }



    private fun gotoErrorPage(errMsg: String) {
        var fragment: BaseDaggerFragment = TransferError.createInstance()
        var bundle = Bundle()
        bundle.putString(Constants.Keys.ERR_MSG_ARG, errMsg)
        fragment.arguments = bundle
        (activity as ActivityListener).addReplaceFragment(fragment, true, TransferError.TAG)
    }

    private fun updateHeader() {
        activity?.let {
            (it as LoaderUiListener).setHeaderTitle(Constants.Headers.TRANSFER_SUCCESS)
        }
    }

    private fun assignThankYouData(thankYouData: OvoP2pTransferThankyouBase) {
        thankYouDataCntnr = thankYouData
        date.text = thankYouData.ovoP2pTransferThankyou?.trnsfrDate?:""
        val rpFormattedString = thankYouData.ovoP2pTransferThankyou?.amt?.toDouble()
            ?.let { CurrencyFormatUtil.getThousandSeparatorString(it, false, 0) }
        trnsfrAmt.text = rpFormattedString?.formattedString?:""
        var sourceName = thankYouData.ovoP2pTransferThankyou?.source?.name
        if (TextUtils.isEmpty(sourceName)) {
            sourceName = context?.let {
                OvoP2pUtil.getUserName(it)
            }.toString()
            thankYouDataCntnr.ovoP2pTransferThankyou?.source?.name = sourceName
        }
        sndrName.text = sourceName?:""
        sndrNum.text = Constants.Prefixes.OVO + thankYouData.ovoP2pTransferThankyou?.source?.phone
        setRcvrUserData()
    }

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if (id != -1) {
            when (id) {
                R.id.see_dtl -> {
                    //go to see detail fragment
                    var bundle = Bundle()
                    bundle.putParcelable(Constants.Keys.THANKYOU_ARGS, thankYouDataCntnr)
                    bundle.putString(Constants.Keys.RECIEVER_NAME, rcvrName.text.toString())
                    bundle.putSerializable(Constants.Keys.RECIEVER_PHONE, rcvrNum.text.toString())
                    (activity as ActivityListener).addReplaceFragment(TransactionDetails.newInstance(bundle), false,
                            TransactionDetails.TAG)
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_SUMRY_TRNSFR_SUCS, "", AnalyticsUtil.EventAction.CLK_SEE_DTL)
                    }
                }
                R.id.back_to_app -> {
                    context?.let {
                        startActivity(OvoP2pUtil.getDiscoveryPageIntent(it))
                        activity?.finish()
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_SUMRY_TRNSFR_SUCS, "", AnalyticsUtil.EventAction.CLK_KMBL_TKPD)
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
            fragmentSucsOvoUsr.arguments = bundle
            return fragmentSucsOvoUsr
        }
    }

    private fun showErrorSnackBar(errMsg: String) {
        activity?.let {
            errorSnackbar = OvoP2pUtil.createErrorSnackBar(it,  errMsg)
        {
            errorSnackbar.let { errorSnackbar->
                if (errorSnackbar.isShownOrQueued) errorSnackbar.dismiss()
            }
            }
            errorSnackbar.show()
        }
    }
}