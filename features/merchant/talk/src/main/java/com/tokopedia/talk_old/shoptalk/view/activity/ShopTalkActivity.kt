package com.tokopedia.talk_old.shoptalk.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk_old.common.di.DaggerTalkComponent
import com.tokopedia.talk_old.common.di.TalkComponent
import com.tokopedia.talk_old.shoptalk.view.fragment.ShopTalkFragment

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

    override fun onCreate(savedInstanceState: Bundle?) {
        addExtrasIfFromAppLink()
        super.onCreate(savedInstanceState)
    }

    private fun addExtrasIfFromAppLink() {
        val uri = intent.data ?: return
        val shopId = uri.lastPathSegment ?: ""
        if (shopId.isNotEmpty()) {
            intent.putExtra(APP_LINK_EXTRA_SHOP_ID, shopId)
        }
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    companion object {
        const val EXTRA_SHOP_ID: String = "shopId"
        const val APP_LINK_EXTRA_SHOP_ID = "shop_id"
    }
}