package com.tokopedia.ovop2p.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.R

class FragmentTransferError : BaseDaggerFragment(), View.OnClickListener {
    private lateinit var errorMsg: TextView
    private lateinit var tryAgain: TextView
    private lateinit var backToApp: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.transfer_error, container, false)
        errorMsg = view.findViewById(R.id.txn_err)
        tryAgain = view.findViewById(R.id.try_agn)
        tryAgain.setOnClickListener(this)
        backToApp = view.findViewById(R.id.back_to_app)
        backToApp.setOnClickListener(this)
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