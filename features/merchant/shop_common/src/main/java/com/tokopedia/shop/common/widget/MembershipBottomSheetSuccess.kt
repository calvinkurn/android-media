package com.tokopedia.shop.common.widget

import android.view.View
import android.widget.Button
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.shop.common.R

class MembershipBottomSheetSuccess : BottomSheets() {
    companion object{
        private const val IMG_BS_MEMBERSHIP_SUCCESS = "https://ecs7.tokopedia.net/img/android/membership/coupon_success.png"
    }

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheet_membership_success

    override fun getTheme(): Int = R.style.BaseBottomSheetDialogRounded

    override fun getBaseLayoutResourceId(): Int = R.layout.base_bottom_sheets_rounded

    override fun title(): String = ""

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }

    override fun initView(view: View) {
        ImageHandler.LoadImage(view.findViewById(R.id.img_membership_success),IMG_BS_MEMBERSHIP_SUCCESS)
        view.findViewById<Button>(R.id.btn_check_my_coupon).setOnClickListener {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, "https://m.tokopedia.com/membership/shop/detail/2000"))
        }
    }

}