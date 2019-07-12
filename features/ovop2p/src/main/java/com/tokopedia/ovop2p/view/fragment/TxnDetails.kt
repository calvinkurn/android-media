package com.tokopedia.ovop2p.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase

class TxnDetails : BaseDaggerFragment(){
    private lateinit var sucsMsg: TextView
    private lateinit var date: TextView
    private lateinit var senderName: TextView
    private lateinit var senderNumber: TextView
    private lateinit var rcvrName: TextView
    private lateinit var rcvrNum: TextView
    private lateinit var txtMsgTxn: TextView
    private lateinit var srcFunds: TextView
    private lateinit var amt: TextView
    private lateinit var txnNo: TextView
    private lateinit var ovoP2pTransferThankyouBase: OvoP2pTransferThankyouBase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.transaction_details, container, false)
        sucsMsg = view.findViewById(R.id.sucs_ttl)
        date = view.findViewById(R.id.trnsfr_date)
        senderName = view.findViewById(R.id.name_sender)
        senderNumber = view.findViewById(R.id.num_sender)
        rcvrName = view.findViewById(R.id.name_rcvr)
        rcvrNum = view.findViewById(R.id.num_rcvr)
        txtMsgTxn = view.findViewById(R.id.txt_msg)
        srcFunds = view.findViewById(R.id.txt_src_fnd)
        amt = view.findViewById(R.id.txt_amt)
        txnNo = view.findViewById(R.id.txt_ref_no)
        getThankyouDataFromArgs()
        return view
    }
    override fun getScreenName(): String {
        return Constants.ScreenName.TXN_DTL_FRAGMENT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewData()
    }

    fun setViewData(){
        if(::ovoP2pTransferThankyouBase.isInitialized) {
            date.text = ovoP2pTransferThankyouBase.ovoP2pTransferThankyou.trnsfrDate
            txtMsgTxn.text = ovoP2pTransferThankyouBase.ovoP2pTransferThankyou.msg
            val rpFormattedString = CurrencyFormatUtil.getThousandSeparatorString(ovoP2pTransferThankyouBase.ovoP2pTransferThankyou.amt.toDouble(), false, 0)
            amt.text = "-Rp" + rpFormattedString.formattedString
            txnNo.text = "Ref - " + ovoP2pTransferThankyouBase.ovoP2pTransferThankyou.refNum
            setSenderUserData()
            setRcvrUserData()
        }
    }

    private fun setRcvrUserData(){
        rcvrName.text = arguments?.getString(Constants.Keys.RECIEVER_NAME) ?: ""
        rcvrNum.text = arguments?.getString(Constants.Keys.RECIEVER_PHONE)
    }

    companion object{
        var TAG: String = "TXN_DTL_FRAG"
        fun newInstance() : TxnDetails{
            return TxnDetails()
        }
        fun newInstance(bundle: Bundle): TxnDetails {
            val fragmentTxnDetails = newInstance()
            fragmentTxnDetails.setArguments(bundle)
            return fragmentTxnDetails
        }
    }

    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    private fun getThankyouDataFromArgs(){
        ovoP2pTransferThankyouBase = arguments?.getSerializable(Constants.Keys.THANKYOU_ARGS) as OvoP2pTransferThankyouBase
    }

    private fun setSenderUserData(){
        senderName.text = ovoP2pTransferThankyouBase.ovoP2pTransferThankyou.source.name
        senderNumber.text = Constants.Prefixes.OVO+ovoP2pTransferThankyouBase.ovoP2pTransferThankyou.source.phone
    }
}