package com.tokopedia.ovop2p.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.R

class FragmentTransactionSuccessOvoUser: BaseDaggerFragment(), View.OnClickListener {

    private lateinit var date:TextView
    private lateinit var trnsfrAmt:TextView
    private lateinit var infoDesc:TextView
    private lateinit var sndrName: TextView
    private lateinit var sndrNum: TextView
    private lateinit var rcvrName: TextView
    private lateinit var rcvrNum: TextView
    private lateinit var seeDtl: TextView
    private lateinit var backToApp: Button
    private lateinit var infoIcon: ImageView

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.transaction_success_page, container,false)
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
        return view
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}