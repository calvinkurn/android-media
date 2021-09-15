package com.tokopedia.product.detail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ShopStatusLinkMovementMethod
import com.tokopedia.product.detail.view.util.goToWebView
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

    private var titleText: String = ""
    private var message: String = ""
    private var reason: String = ""
    private var btnText: String = ""
    private var btnLink: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    fun show(title: String, message: String, reason: String, btnText: String, btnLink: String,
             supportFragmentManager: FragmentManager) {
        this.titleText = title
        this.message = message
        this.reason = reason
        this.btnText = btnText
        this.btnLink = btnLink
        show(supportFragmentManager, "Shop Status BS")
    }

    private fun initView() {
        parentView = View.inflate(requireContext(), R.layout.bottom_sheet_shop_status_info, null)
        messageText = parentView?.findViewById(R.id.message_status)
        messageCloseNote = parentView?.findViewById(R.id.message_close_note)
        btnRequestOpen = parentView?.findViewById(R.id.btn_request_open_shop)

        setTitle(titleText)

        messageText?.shouldShowWithAction(message.isNotBlank()) {
            context?.let {
                messageText?.text = HtmlLinkHelper(it, message).spannedString
                messageText?.movementMethod = ShopStatusLinkMovementMethod { link ->
                    goToWebView(link)
                }
            }
        }

        messageCloseNote?.shouldShowWithAction(reason.isNotBlank()) {
            context?.let {
                messageCloseNote?.text = HtmlLinkHelper(it, reason).spannedString
            }
        }

        btnRequestOpen?.shouldShowWithAction(btnText.isNotBlank()) {
            btnRequestOpen?.text = btnText
            btnRequestOpen?.setOnClickListener {
                route(btnLink)
            }
        }
        setChild(parentView)
    }

    private fun goToWebView(url: String) {
        context?.let {
            url.goToWebView(it)
        }
    }

    private fun route(url: String) {
        context?.let {
            if (RouteManager.isSupportApplink(it, url)) {
                RouteManager.route(it, url)
            } else {
                goToWebView(url)
            }
        }
    }
}