package com.tokopedia.talk.producttalk.view.activity

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
import com.tokopedia.talk.producttalk.view.fragment.ProductTalkFragment

class TalkProductActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {
    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }


    companion object {

        val PRODUCT_ID = "product_id"

        @JvmStatic
        fun createIntent(context: Context, productId: String): Intent {
            val intent = Intent(context,
                    TalkProductActivity::class.java)
            intent.putExtra(PRODUCT_ID, productId)
            return intent
        }
    }

    object DeepLinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.PRODUCT_TALK)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            val productId = extras.getString(PRODUCT_ID, "")
            return (context.applicationContext as TalkRouter).getProductTalk(context, productId)
                    .setData(uri.build())
                    .putExtras(extras)
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

}