package com.tokopedia.product.detail.view.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef
import com.tokopedia.product.detail.data.util.ShopStatusLinkMovementMethod
import com.tokopedia.product.detail.view.util.goToWebView
import com.tokopedia.product.detail.view.util.linkTextWithGiven
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 08/06/20
 */
class ShopStatusInfoBottomSheet(private val statusInfo: ShopInfo.StatusInfo, private val shopId: String) : BottomSheetUnify() {

    private var parentView: View? = null
    private var messageText: Typography? = null
    private var infoText: Typography? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        if (statusInfo.shopStatus == ProductShopStatusTypeDef.CLOSED) {
            setTitle(context?.getString(R.string.bs_title_shop_closed) ?: "")
        } else {
            setTitle(context?.getString(R.string.bs_title_shop_moderated) ?: "")
        }

        parentView = View.inflate(requireContext(), R.layout.bottom_sheet_shop_status_info, null)
        messageText = parentView?.findViewById(R.id.message_status)
        infoText = parentView?.findViewById(R.id.info_status)


        context?.let {
            messageText?.text = MethodChecker.fromHtml(statusInfo.statusMessage)
            messageText?.movementMethod = ShopStatusLinkMovementMethod {
                goToWebView(it)
            }

            infoText?.text = it.getString(R.string.bs_desc_info_shop_closed).linkTextWithGiven(it,
                    "Chat" to ::onChatClicked,
                    "Pusat Bantuan" to ::onPusatBantuanClicked)
            infoText?.movementMethod = LinkMovementMethod.getInstance()
        }

        setChild(parentView)
    }

    private fun onChatClicked() {
        context?.let {
            RouteManager.route(it,
                    ApplinkConst.TOPCHAT_ASKSELLER, shopId)
        }
    }

    private fun onPusatBantuanClicked() {
        goToWebView("asd")
    }

    private fun goToWebView(url:String) {
        context?.let {
            url.goToWebView(it)
        }
    }
}