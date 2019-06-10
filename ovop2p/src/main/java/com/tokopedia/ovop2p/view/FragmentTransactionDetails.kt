package com.tokopedia.ovop2p.view

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.R

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
    private lateinit var icFwd: ImageView
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
        icFwd = view.findViewById(R.id.fwd)
        icFwd.setOnClickListener(this)
        return view
    }
    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}