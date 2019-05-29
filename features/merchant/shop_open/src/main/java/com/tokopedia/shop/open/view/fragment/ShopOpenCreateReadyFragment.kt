package com.tokopedia.shop.open.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.util.MethodChecker
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity
import com.tokopedia.seller.SellerModuleRouter
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.di.component.DaggerShopOpenDomainComponent
import com.tokopedia.shop.open.view.activity.ShopOpenCreateReadyActivity.Companion.ARGUMENT_DATA_SHOP_ID
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_open_create_ready.*
import javax.inject.Inject

class ShopOpenCreateReadyFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        fun newInstance(shopId:String) =
                ShopOpenCreateReadyFragment().also {
                    it.arguments = Bundle().apply {
                        putString(ARGUMENT_DATA_SHOP_ID,shopId)
                    }
                }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerShopOpenDomainComponent.builder()
                    .shopComponent((it.application as SellerModuleRouter).shopComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val helloName = getString(R.string.hello_user_name, userSession.name)
        tv_user_name.text = MethodChecker.fromHtml(helloName)
        button_add_product.setOnClickListener {
            val intent = ProductAddNameCategoryActivity.createInstance(activity);
            startActivity(intent)
            activity?.finish()
        }
        button_add_product_later.setOnClickListener {
            RouteManager.route(context, ApplinkConst.SHOP,arguments?.getString(ARGUMENT_DATA_SHOP_ID) ?: "" )
            activity?.finish()
        }
        initTncCourier()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_open_create_ready, container, false)

    }

    private fun initTncCourier() {
        val spanText = android.text.SpannableString(getString(R.string.open_shop_label_courier))

        spanText.setSpan(StyleSpan(Typeface.BOLD),
                0, 47, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(ForegroundColorSpan(resources.getColor(R.color.tkpd_main_green)),
                spanText.length - 7, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(StyleSpan(Typeface.BOLD),
                spanText.length -7, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                RouteManager.route(context,ApplinkConst.SELLER_SHIPPING_EDITOR)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, spanText.length - 7, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_tnc_courier.movementMethod = LinkMovementMethod.getInstance();
        tv_tnc_courier.text = spanText
    }
}
