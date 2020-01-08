package com.tokopedia.talk.producttalk.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.talk.R
import com.tokopedia.talk.common.TalkRouter
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.producttalk.view.fragment.ProductTalkFragment

class TalkProductActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {
    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }


    companion object {

        val PRODUCT_ID = "product_id"
        val SHOP_ID = "shop_id"
        val PRODUCT_PRICE = "product_price"
        val PRODUCT_NAME = "prod_name"
        val PRODUCT_IMAGE = "product_image"
        val PRODUCT_URL = "product_url"
        val SHOP_NAME = "shop_name"
        val SHOP_AVATAR = "shop_avatar"

        @JvmStatic
        fun createIntent(context: Context, productId: String): Intent {
            val intent = Intent(context,
                    TalkProductActivity::class.java)
            intent.putExtra(PRODUCT_ID, productId)
            return intent
        }

        fun createIntent(context: Context, productId: String,
                         shopId: String,
                         productPrice: String,
                         productName: String,
                         productImage: String,
                         productUrl: String,
                         shopName: String,
                         shopAvatar: String): Intent {
            val intent = Intent(context, TalkProductActivity::class.java)
            intent.putExtra(PRODUCT_ID, productId)
            intent.putExtra(SHOP_ID, shopId)
            intent.putExtra(PRODUCT_PRICE, productPrice)
            intent.putExtra(PRODUCT_NAME, productName)
            intent.putExtra(PRODUCT_IMAGE, productImage)
            intent.putExtra(PRODUCT_URL, productUrl)
            intent.putExtra(SHOP_NAME, shopName)
            intent.putExtra(SHOP_AVATAR, shopAvatar)
            return intent
        }
    }

    object DeepLinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.PRODUCT_TALK)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            val productId = extras.getString(PRODUCT_ID, "")

            return RouteManager.getIntent(context, ApplinkConstInternalGlobal.PRODUCT_TALK)
                        .apply {
                            data = uri.build()
                            putExtras(extras)
                            putExtra(ApplinkConstInternalGlobal.PARAM_PRODUCT_ID, productId)
                        }
//            return (context.applicationContext as TalkRouter).getProductTalk(context, productId)
//                    .setData(uri.build())
//                    .putExtras(extras)
        }

    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        } else {
            finish()
        }

        return ProductTalkFragment.newInstance(intent.extras)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_talk_product
    }
}