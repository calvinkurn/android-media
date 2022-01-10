package com.tokopedia.product.detail.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.util.goToWebView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

class RedirectActivateBottomSheet:BottomSheetUnify() {

    private var parentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    fun show( supportFragmentManager: FragmentManager) {
        show(supportFragmentManager, "RedirectActivate")
    }

    private fun initView() {
        parentView = View.inflate(requireContext(), R.layout.bottom_sheet_activate_redirect, null)

        setChild(parentView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val redirectButton = view.findViewById<UnifyButton>(R.id.redirectButton)
        redirectButton.setOnClickListener {
            context?.let { context ->
                if (RouteManager.isSupportApplink(context, "www.tokopedia.com")) {
                    RouteManager.route(context, "www.tokopedia.com")
                } else {
                    "www.tokopedia.com".goToWebView(context);
                }

            }
        }

    }




}