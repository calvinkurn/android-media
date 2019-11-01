package com.tokopedia.talk.addtalk.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.addtalk.view.fragment.AddTalkFragment
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent

class AddTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {
    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    companion object {
        const val EXTRA_PRODUCT_ID: String = "product_id"

        fun createIntent(context: Context,
                         productId: String): Intent{
            val intent = Intent(context, AddTalkActivity::class.java)
            val bundle = Bundle()
            bundle.putString(AddTalkActivity.EXTRA_PRODUCT_ID, productId)
            intent.putExtras(bundle)
            return intent
        }

    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        return AddTalkFragment.newInstance(intent.extras)
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

}