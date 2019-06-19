package com.tokopedia.ovop2p.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.R

class FragmentTransactionSuccessNonOvoUser: BaseDaggerFragment(), View.OnClickListener {

    private lateinit var txtvSucs: TextView
    private lateinit var tryAgn: TextView
    private lateinit var bckToApp: TextView

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.transaction_success_non_ovo, container,false)
        txtvSucs = view.findViewById(R.id.sucs_msg)
        tryAgn = view.findViewById(R.id.try_agn)
        tryAgn.setOnClickListener(this)
        bckToApp = view.findViewById(R.id.back_to_app)
        bckToApp.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}