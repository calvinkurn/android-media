package com.tokopedia.talk.shoptalk.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.talk.common.TalkRouter
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.shoptalk.view.fragment.ShopTalkFragment

/**
 * @author by nisie on 9/17/18.
 */
class ShopTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {


    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ShopTalkFragment.newInstance(bundle)
    }

    companion object {
        const val EXTRA_SHOP_ID: String = "shopId"
        const val APP_LINK_EXTRA_SHOP_ID = "shop_id"

        open fun createIntent(context: Context,
                              shopId: String): Intent {
            val intent = Intent(context, ShopTalkActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_SHOP_ID, shopId)
            intent.putExtras(bundle)
            return intent
        }


        object DeepLinkIntents {
            @JvmStatic
            @DeepLink(ApplinkConst.SHOP_TALK)
            fun getCallingIntent(context: Context, extras: Bundle): Intent {
                val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
                val shopId = extras.getString(APP_LINK_EXTRA_SHOP_ID, "")
                return (context.applicationContext as TalkRouter).getShopTalkIntent(context, shopId)
                        .setData(uri.build())
                        .putExtras(extras)

            }

        }

    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

}