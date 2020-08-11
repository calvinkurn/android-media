package com.tokopedia.withdraw.auto_withdrawal.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.webview.KEY_NEED_LOGIN
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.analytics.AutoWithdrawAnalytics
import com.tokopedia.withdraw.auto_withdrawal.di.component.DaggerAutoWithdrawalComponent
import com.tokopedia.withdraw.auto_withdrawal.domain.model.GetInfoAutoWD
import com.tokopedia.withdraw.auto_withdrawal.presentation.adapter.AutoWDInfoAdapter
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant
import javax.inject.Inject

class AutoWDInfoFragment : BottomSheetUnify(), TickerCallback {

    private var infoAutoWD: GetInfoAutoWD? = null

    @Inject
    lateinit var analytics: dagger.Lazy<AutoWithdrawAnalytics>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        arguments?.let {
            if (it.containsKey(ARG_AWD_INFO)) {
                infoAutoWD = it.getParcelable(ARG_AWD_INFO)
            } else
                dismiss()
        }
        val rootView = LayoutInflater.from(context)
                .inflate(R.layout.swd_layout_awd_info, null, false)
        rootView.apply {
            setChild(this)
            val ticker = findViewById<Ticker>(R.id.awdJoinRPTicker)
            ticker.setHtmlDescription(getString(R.string.swd_awd_join_rp_for_autoWD))
            ticker.setDescriptionClickEvent(this@AutoWDInfoFragment)
            findViewById<View>(R.id.btnMoreInfo).setOnClickListener { openMoreInfo() }
            val recyclerView = findViewById<RecyclerView>(R.id.rvAutoWDInfo)
            initRecyclerAdapter(recyclerView)
        }
    }



    private fun initInjector() {
        activity?.let {
            DaggerAutoWithdrawalComponent.builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication)
                            .baseAppComponent)
                    .build()
                    .inject(this)
        }
    }


    private fun initRecyclerAdapter(recyclerView: RecyclerView) {
        infoAutoWD?.let {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = AutoWDInfoAdapter(it.data)
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun openMoreInfo() {
        context?.let {
            val intent = RouteManager.getIntent(context,"https://www.tokopedia.com/help/article/auto-withdrawal").apply {
                putExtra(KEY_NEED_LOGIN, true)
            }
            it.startActivity(intent)
        }
        if(::analytics.isInitialized){
            analytics.get().onClickMoreInfoOnInfoBottomSheet()
        }
        dismiss()
    }

    companion object {
        private const val ARG_AWD_INFO = "arg_awd_info"
        fun getInstance(getInfoAutoWD: GetInfoAutoWD)
                : AutoWDInfoFragment {
            return AutoWDInfoFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(ARG_AWD_INFO, getInfoAutoWD)
                arguments = bundle
            }
        }
    }

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        WithdrawConstant.openRekeningAccountInfoPage(context)
        if(::analytics.isInitialized){
            analytics.get().clickOnGabungRPInfo()
        }
        dismiss()
    }

    override fun onDismiss() {
        //not required
    }

}