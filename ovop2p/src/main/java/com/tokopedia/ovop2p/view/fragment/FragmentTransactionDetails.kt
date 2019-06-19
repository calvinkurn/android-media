package com.tokopedia.ovop2p.view.fragment

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent

class FragmentTransactionDetails : BaseDaggerFragment(), View.OnClickListener {
    private lateinit var sucsMsg: TextureView
    private lateinit var date: TextView
    private lateinit var senderName: TextView
    private lateinit var senderNumber: TextView
    private lateinit var rcvrName: TextView
    private lateinit var rcvrNum: TextView
    private lateinit var txtMsgTxn: TextView
    private lateinit var srcFunds: TextView
    private lateinit var amt: TextView
    private lateinit var txnNo: TextView

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
        return view
    }
    override fun getScreenName(): String {
        return Constants.ScreenName.TXN_DTL_FRAGMENT
    }

    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if(id != -1){
        }
    }
}