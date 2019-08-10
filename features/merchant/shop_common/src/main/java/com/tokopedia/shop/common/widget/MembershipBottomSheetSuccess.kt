package com.tokopedia.shop.common.widget

import android.view.View
import android.widget.Button
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.shop.common.R

class MembershipBottomSheetSuccess : BottomSheets() {

    companion object {
        @JvmStatic
        fun newInstance() {

        }
    }

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheet_membership_success

//    override fun getTheme(): Int = R.style.BaseBottomSheetDialogRounded

    override fun getBaseLayoutResourceId(): Int = R.layout.base_bottom_sheets_rounded

    override fun initView(view: View) {
        view.findViewById<Button>(R.id.btn_check_my_coupon).setOnClickListener {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, "https://www.tokopedia.com/help/article/a-1940"))
        }
    }

}