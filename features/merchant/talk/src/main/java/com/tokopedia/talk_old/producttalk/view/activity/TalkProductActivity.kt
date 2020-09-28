package com.tokopedia.talk_old.producttalk.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.common.di.DaggerTalkComponent
import com.tokopedia.talk_old.common.di.TalkComponent
import com.tokopedia.talk_old.producttalk.view.fragment.ProductTalkFragment

class TalkProductActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        addExtrasIfFromAppLink()
        super.onCreate(savedInstanceState)
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun addExtrasIfFromAppLink() {
        val uri = intent.data ?: return
        val productId = uri.lastPathSegment ?: return
        if (productId.isNotEmpty()) {
            intent.putExtra(PRODUCT_ID, productId)
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        } else {
            finish()
        }

        return ProductTalkFragment.newInstance(intent.extras ?: Bundle())
    }

    override fun getToolbarResourceID(): Int = R.id.activity_talk_product_toolbar
    override fun getParentViewResourceID() = R.id.talk_parent_view

    override fun getLayoutRes(): Int {
        return R.layout.activity_talk_product
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
    }
}