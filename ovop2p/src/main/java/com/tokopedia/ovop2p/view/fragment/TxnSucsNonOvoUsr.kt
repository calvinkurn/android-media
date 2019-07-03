package com.tokopedia.ovop2p.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.util.AnalyticsUtil
import com.tokopedia.ovop2p.view.activity.OvoP2PFormActivity

class TxnSucsNonOvoUsr: BaseDaggerFragment(), View.OnClickListener {

    private lateinit var txtvSucs: TextView
    private lateinit var tryAgn: TextView
    private lateinit var bckToApp: TextView
    private lateinit var sucsMsg: TextView
    private var phnNo: String = ""

    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return Constants.ScreenName.FRAGMENT_THANKYOU_NON_OVO_USER
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.transaction_success_non_ovo, container,false)
        txtvSucs = view.findViewById(R.id.sucs_msg)
        tryAgn = view.findViewById(R.id.send_agn)
        tryAgn.setOnClickListener(this)
        bckToApp = view.findViewById(R.id.back_to_app)
        bckToApp.setOnClickListener(this)
        sucsMsg = view.findViewById(R.id.sucs_msg)
        phnNo = arguments?.getString(Constants.Keys.RECIEVER_PHONE, "") ?: ""
        createSucsMsg()
        return view
    }

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if(id != -1){
            when(id){
                R.id.back_to_app -> {
                    activity?.finish()
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_SUMRY_TRNSFR_SUCS, "", AnalyticsUtil.EventAction.CLK_KMBL_TKPD)
                    }
                }
                R.id.send_agn -> {
                    //relaunch the form activity
                    var intent = Intent(activity, OvoP2PFormActivity::class.java)
                    activity?.startActivity(intent)
                    activity?.finish()
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_SUMRY_TRNSFR_SUCS, "", AnalyticsUtil.EventAction.CLK_TRY_AGN)
                    }
                }
            }
        }
    }

    private fun createSucsMsg(){
        sucsMsg.text = Constants.Messages.NONOVO_USR_SUCS.replace(Constants.PlaceHolders.PHONE_NO_PLCHLDR, phnNo)
    }

    companion object{
        fun newInstance(): TxnSucsNonOvoUsr {
            return TxnSucsNonOvoUsr()
        }

        fun newInstance(bundle: Bundle): TxnSucsNonOvoUsr {
            val fragmentSucsNonOvo = newInstance()
            fragmentSucsNonOvo.setArguments(bundle)
            return fragmentSucsNonOvo
        }
    }
}