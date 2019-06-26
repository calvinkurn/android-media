package com.tokopedia.ovop2p.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.view.interfaces.ActivityListener
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener
import com.tokopedia.ovop2p.viewmodel.OvoP2pTxnThankYouOvoUsrVM
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class FragmentTransactionSuccessOvoUser : BaseDaggerFragment(), View.OnClickListener {

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
    private lateinit var userSession : UserSessionInterface

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

    private fun setSenderUserData(){
        userSession = UserSession(context)
        sndrName.text = userSession.name
        sndrNum.text = userSession.phoneNumber
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateHeader()
        if(!TextUtils.isEmpty(transferId)) {
            var dataMap: HashMap<String, Any> = HashMap()
            dataMap.put(Constants.Keys.TRANSFER_ID, transferId)
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
                    if(it != null){
                        (activity as LoaderUiListener).hideProgressDialog()
                        if(TextUtils.isEmpty(it.ovoP2pTransferThankyou.errors.message)){
                            assignThankYouData(it)
                        }
                        else{
                            it.ovoP2pTransferThankyou.errors.message?.let { it1 -> gotoErrorPage(it1) }
                        }
                    }
                })
            }
        }
    }

    private fun gotoErrorPage(errMsg: String){
        var fragment: BaseDaggerFragment = FragmentTransferError.createInstance()
        var bundle = Bundle()
        bundle.putString(Constants.Keys.ERR_MSG_ARG, errMsg)
        fragment.arguments = bundle
        (activity as ActivityListener).addReplaceFragment(fragment, true, FragmentTransferError.TAG)
    }

    private fun updateHeader(){
        (activity as LoaderUiListener).setHeaderTitle(Constants.Headers.TRANSFER_SUCCESS)
    }

    private fun assignThankYouData(thankYouData: OvoP2pTransferThankyouBase) {
        date.text = thankYouData.ovoP2pTransferThankyou.trnsfrDate
        trnsfrAmt.text = thankYouData.ovoP2pTransferThankyou.amt.toString()
        rcvrName.text = thankYouData.ovoP2pTransferThankyou.soure1.name
        rcvrNum.text = thankYouData.ovoP2pTransferThankyou.soure1.phone
        setSenderUserData()
    }

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if (id != -1) {
            when (id) {
                R.id.see_dtl -> {
                    //go to see detail fragment
                    (activity as ActivityListener).addReplaceFragment(FragmentTransactionDetails.createInstance(), true,
                            FragmentTransactionDetails.TAG)
                }
                R.id.back_to_app -> {
                    activity?.finish()
                }
            }
        }
    }

    companion object {
        fun newInstance(): FragmentTransactionSuccessOvoUser {
            return FragmentTransactionSuccessOvoUser()
        }

        fun newInstance(bundle: Bundle): FragmentTransactionSuccessOvoUser {
            val fragmentSucsOvoUsr = newInstance()
            fragmentSucsOvoUsr.setArguments(bundle)
            return fragmentSucsOvoUsr
        }
    }
}