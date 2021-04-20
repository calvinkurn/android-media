package com.tokopedia.talk.feature.reporttalk.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reporttalk.view.fragment.ReportTalkFragment

/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ReportTalkFragment.newInstance(bundle)
    }

    companion object {
        const val EXTRA_TALK_ID: String = "talkId"
        const val EXTRA_SHOP_ID: String = "shopId"
        const val EXTRA_PRODUCT_ID: String = "productId"
        const val EXTRA_COMMENT_ID: String = "commentId"

        open fun createIntentReportTalk(context: Context,
                                        talkId: String,
                                        shopId: String,
                                        productId: String): Intent {
            val intent = Intent(context, ReportTalkActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_TALK_ID, talkId)
            bundle.putString(EXTRA_SHOP_ID, shopId)
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            intent.putExtras(bundle)
            return intent
        }

        open fun createIntentReportComment(context: Context,
                                           talkId: String,
                                           commentId: String,
                                           shopId: String,
                                           productId: String): Intent {
            val intent = Intent(context, ReportTalkActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_TALK_ID, talkId)
            bundle.putString(EXTRA_SHOP_ID, shopId)
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            bundle.putString(EXTRA_COMMENT_ID, commentId)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

}