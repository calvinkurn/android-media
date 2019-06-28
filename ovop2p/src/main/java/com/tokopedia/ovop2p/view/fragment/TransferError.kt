package com.tokopedia.ovop2p.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent

class TransferError : BaseDaggerFragment(), View.OnClickListener {

    private lateinit var errorMsg: TextView
    private lateinit var tryAgain: TextView
    private lateinit var backToApp: TextView
    private var errorTxt: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.transfer_error, container, false)
        errorMsg = view.findViewById(R.id.txn_err)
        tryAgain = view.findViewById(R.id.try_agn)
        tryAgain.setOnClickListener(this)
        backToApp = view.findViewById(R.id.back_to_app)
        backToApp.setOnClickListener(this)
        errorTxt = arguments?.getString(Constants.Keys.ERR_MSG_ARG) ?: ""
        if(!TextUtils.isEmpty(errorTxt)){
            errorMsg.text = errorTxt
        }
        return view
    }

    override fun getScreenName(): String {
        return Constants.ScreenName.ERROR_FRAGMENT
    }

    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if (id != -1) {
            when (id) {
                R.id.try_agn -> {
                    activity?.supportFragmentManager?.popBackStack()
                }
                R.id.back_to_app -> {
                    activity?.finish()
                }
            }
        }
    }

    companion object{

        var TAG = "Error Fragment"

        fun createInstance() : TransferError{
            return TransferError()
        }
    }
}
