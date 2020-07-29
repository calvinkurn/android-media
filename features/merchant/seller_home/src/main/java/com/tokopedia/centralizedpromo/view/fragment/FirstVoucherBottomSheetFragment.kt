package com.tokopedia.centralizedpromo.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.FirstVoucherDataSource
import com.tokopedia.centralizedpromo.view.adapter.FirstVoucherAdapter
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.centralized_promo_first_voucher_bottomsheet_layout.*
import javax.inject.Inject

class FirstVoucherBottomSheetFragment : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context) = FirstVoucherBottomSheetFragment().apply {
            val view = View.inflate(context, R.layout.centralized_promo_first_voucher_bottomsheet_layout, null)
            setChild(view)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }
    }

    private val impressHolder: ImpressHolder = ImpressHolder()

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            initView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addOnImpressionListener(impressHolder) {
            CentralizedPromoTracking.sendFirstVoucherBottomSheetImpression(userSession.userId)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initView() {
        setCloseClickListener {
            CentralizedPromoTracking.sendFirstVoucherBottomSheetClick(userSession.userId, true)
            dismiss()
        }

        firstVoucherRecyclerView?.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val itemList = FirstVoucherDataSource.getFirstVoucherInfoItems()
            adapter = FirstVoucherAdapter(itemList)
        }

        firstVoucherButton?.setOnClickListener {
            CentralizedPromoTracking.sendFirstVoucherBottomSheetClick(userSession.userId, false)
            RouteManager.route(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER)
            this.dismiss()
        }
    }
}