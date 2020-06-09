package com.tokopedia.product.detail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef
import com.tokopedia.product.detail.data.util.ShopStatusLinkMovementMethod
import com.tokopedia.product.detail.view.util.goToWebView
import com.tokopedia.product.detail.view.widget.CheckImeiBottomSheet
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by Yehezkiel on 08/06/20
 */
class ShopStatusInfoBottomSheet(val mActivity: FragmentActivity, private val statusInfo: ShopInfo.StatusInfo) : BottomSheetUnify() {

    private var parentView: View? = null
    private var messageText: Typography? = null

    companion object {
        fun showShopStatusBottomSheet(context: FragmentActivity, statusInfo: ShopInfo.StatusInfo, shopId: String) {
            ShopStatusInfoBottomSheet(context, statusInfo).showDialog()
        }
    }

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


        context?.let {
            messageText?.text = MethodChecker.fromHtml(statusInfo.statusMessage)
            messageText?.movementMethod = ShopStatusLinkMovementMethod {
                goToWebView(it)
            }
        }

        setChild(parentView)
    }

    private fun goToWebView(url: String) {
        mActivity.let {
            url.goToWebView(it)
        }
    }

    fun showDialog() {
        mActivity.supportFragmentManager?.run {
            show(this, CheckImeiBottomSheet.TAG)
        }
    }
}