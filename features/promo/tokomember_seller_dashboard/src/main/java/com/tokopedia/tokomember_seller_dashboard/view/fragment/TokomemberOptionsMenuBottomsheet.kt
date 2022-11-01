package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.ProgramActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponActions
import com.tokopedia.tokomember_seller_dashboard.model.Actions
import com.tokopedia.tokomember_seller_dashboard.model.TripleDotsItem
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ADD_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPTION_MENU
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_MAX_CASHBACK
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.CANCEL
import com.tokopedia.tokomember_seller_dashboard.util.DELETE
import com.tokopedia.tokomember_seller_dashboard.util.DUPLICATE
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.SHARE
import com.tokopedia.tokomember_seller_dashboard.util.STOP
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tm_dash_options_menu_bottomsheet.*

class TokomemberOptionsMenuBottomsheet: BottomSheetUnify() {

    private val childLayoutRes = R.layout.tm_dash_options_menu_bottomsheet
    private var tripleDotsList: List<TripleDotsItem?>? = null
    private var shopId = 0
    private var programId = 0
    private var voucherId = ""
    private var voucherType = ""
    private var voucherQuota = 0
    private var maxCashback: Int = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getString(BUNDLE_OPTION_MENU)?.let {
            tripleDotsList = Gson().fromJson(it, Actions::class.java).tripleDots
        }
        arguments?.getString(BUNDLE_VOUCHER_ID)?.let {
            voucherId = it
        }
        arguments?.getString(BUNDLE_VOUCHER_TYPE)?.let {
            voucherType = it
        }
        arguments?.getInt(BUNDLE_VOUCHER_QUOTA)?.let {
            voucherQuota = it
        }
        arguments?.getInt(BUNDLE_SHOP_ID)?.let {
            shopId = it
        }
        arguments?.getInt(BUNDLE_PROGRAM_ID)?.let {
            programId = it
        }
        arguments?.getInt(BUNDLE_VOUCHER_MAX_CASHBACK)?.let {
            maxCashback = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO get all triple dots type from product team
        tripleDotsList?.forEach {
            when {
                it?.type.equals(EXTEND) -> {
                    it?.text?.let { it1 -> addOptionItem(it1, IconUnify.CLOCK, EXTEND) }
                }
                it?.type.equals(CANCEL) -> {
                    it?.text?.let { it1 -> addOptionItem(it1, IconUnify.CLEAR, CANCEL) }
                }
                it?.type.equals(EDIT) -> {
                    it?.text?.let { it1 -> addOptionItem(it1, IconUnify.EDIT, EDIT) }
                }
                it?.type.equals(DELETE) -> {
                    it?.text?.let { it1 -> addOptionItem(it1, IconUnify.DELETE, DELETE) }
                }
                it?.type.equals(STOP) -> {
                    it?.text?.let { it1 -> addOptionItem(it1, IconUnify.CLEAR, STOP) }
                }
                it?.type.equals(SHARE) -> {
                    it?.text?.let { it1 -> addOptionItem(it1, IconUnify.SHARE_MOBILE, SHARE) }
                }
                it?.type.equals(DUPLICATE) -> {
                    it?.text?.let { it1 -> addOptionItem(it1, IconUnify.COPY, DUPLICATE) }
                }
                it?.type.equals(ADD_QUOTA) -> {
                    it?.text?.let { it1 -> addOptionItem(it1, IconUnify.ADD_CIRCLE, ADD_QUOTA) }
                }
            }
        }

    }

    private fun addOptionItem(text: String, icon: Int, type: String) {
        val childLayout = LayoutInflater.from(context).inflate(R.layout.tm_dash_options_menu_item, container_options_menu ,false)
        childLayout.findViewById<ImageUnify>(R.id.icon_options_menu).setImageDrawable(getIconUnifyDrawable(childLayout.context, icon))
        childLayout.findViewById<Typography>(R.id.tv_options_menu).text = text
        container_options_menu.addView(childLayout)
        childLayout.setOnClickListener {
            parentFragment?.let{
                if(it is ProgramActions){
                    if(type == EXTEND){
                        tmTracker?.clickProgramBsExtension(shopId.toString(), programId.toString())
                    }
                    it.option(type, shopId = shopId, programId = programId)
                }
                if(it is TmCouponActions){
                    it.option(type, voucherId = voucherId, voucherType, voucherQuota, maxCashback = maxCashback)
                }
            }
            dismiss()
        }
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true

        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {

        const val TAG = "TM_DASH_OPTIONS_MENU_BOTTOM_SHEET"
        private lateinit var programActions: ProgramActions
        private var tmTracker: TmTracker? = null
        private lateinit var tmCouponActions: TmCouponActions

        fun show(
            actions: String,
            shopId: Int,
            programId: Int,
            childFragmentManager: FragmentManager,
            programActions: ProgramActions,
            tmTracker: TmTracker?
        ) {
            val bundle = Bundle()
            bundle.putString(BUNDLE_OPTION_MENU, actions)
            bundle.putInt(BUNDLE_PROGRAM_ID, programId)
            bundle.putInt(BUNDLE_SHOP_ID, shopId)
            val tokomemberIntroBottomsheet = TokomemberOptionsMenuBottomsheet().apply {
                arguments = bundle
            }
            this.programActions = programActions
            if (tmTracker != null) {
                this.tmTracker = tmTracker
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }

        fun show(
            actions: String,
            childFragmentManager: FragmentManager,
            tmCouponActions: TmCouponActions,
            voucherId: String,
            voucherType: String,
            voucherQuota: Int,
            maxCashback: Int = 1
        ){
            val bundle = Bundle()
            bundle.putString(BUNDLE_OPTION_MENU, actions)
            bundle.putString(BUNDLE_VOUCHER_ID, voucherId)
            bundle.putString(BUNDLE_VOUCHER_TYPE, voucherType)
            bundle.putInt(BUNDLE_VOUCHER_QUOTA, voucherQuota)
            bundle.putInt(BUNDLE_VOUCHER_MAX_CASHBACK, maxCashback)
            val tokomemberIntroBottomsheet = TokomemberOptionsMenuBottomsheet().apply {
                arguments = bundle
            }
            this.tmCouponActions = tmCouponActions
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }
    }

}
