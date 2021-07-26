package com.tokopedia.product.detail.view.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ShopStatusLinkMovementMethod
import com.tokopedia.product.detail.view.util.goToWebView
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by Yehezkiel on 08/06/20
 */
class ShopStatusInfoBottomSheet : BottomSheetUnify() {

    private var parentView: View? = null
    private var messageText: Typography? = null
    private var messageCloseNote: Typography? = null
    private var btnRequestOpen: UnifyButton? = null
    var statusInfo: ShopInfo.StatusInfo? = null
    var closedInfo: ShopInfo.ClosedInfo? = null
    var isShopOwner: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        parentView = View.inflate(requireContext(), R.layout.bottom_sheet_shop_status_info, null)
        messageText = parentView?.findViewById(R.id.message_status)
        messageCloseNote = parentView?.findViewById(R.id.message_close_note)
        btnRequestOpen = parentView?.findViewById(R.id.btn_request_open_shop)

        when (statusInfo?.shopStatus) {
            ShopStatusDef.CLOSED -> {
                btnRequestOpen?.hide()
                messageCloseNote?.show()
                setTitle(context?.getString(R.string.bs_title_shop_closed) ?: "")
            }
            ShopStatusDef.MODERATED -> {
                showBtnRequest()
                messageCloseNote?.hide()
                setTitle(context?.getString(R.string.bs_title_shop_moderated) ?: "")
            }
            ShopStatusDef.MODERATED_PERMANENTLY -> {
                showBtnRequest()
                messageCloseNote?.hide()
                setTitle(context?.getString(R.string.bs_title_shop_moderated_permanent) ?: "")
            }
            else -> {
                btnRequestOpen?.hide()
                messageCloseNote?.hide()
            }
        }


        context?.let {
            messageText?.text = HtmlLinkHelper(it, statusInfo?.statusMessage ?: "").spannedString
            messageCloseNote?.text = MethodChecker.fromHtml(closedInfo?.note ?: "")
            messageText?.movementMethod = ShopStatusLinkMovementMethod {
                goToWebView(it)
            }
            btnRequestOpen?.text = it.getString(R.string.bs_btn_title)
            btnRequestOpen?.setOnClickListener {
                goToContactUs()
            }
        }

        setChild(parentView)
    }

    private fun goToWebView(url: String) {
        context?.let {
            url.goToWebView(it)
        }
    }

    private fun goToContactUs() {
        context?.let {
            RouteManager.route(it, ApplinkConst.CONTACT_US_NATIVE)
        }
    }

    private fun showBtnRequest(){
        btnRequestOpen?.showWithCondition(isShopOwner)
    }

}