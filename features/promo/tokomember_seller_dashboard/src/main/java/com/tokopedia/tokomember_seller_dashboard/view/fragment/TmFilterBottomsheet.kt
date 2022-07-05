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
import com.tokopedia.tokomember_seller_dashboard.util.ADD_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPTION_MENU
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
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

class TmFilterBottomsheet: BottomSheetUnify() {

    private val childLayoutRes = R.layout.tm_filter_bottomsheet
    private var tripleDotsList: List<TripleDotsItem?>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getString(BUNDLE_OPTION_MENU)?.let {
            tripleDotsList = Gson().fromJson(it, Actions::class.java).tripleDots
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
        private lateinit var tmCouponActions: TmCouponActions

        fun show(
            childFragmentManager: FragmentManager,
        ) {
            val bundle = Bundle()
            val tokomemberIntroBottomsheet = TmFilterBottomsheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }

    }

}