package com.tokopedia.quest_widget.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.quest_widget.di.DaggerQuestComponent
import com.tokopedia.quest_widget.di.QuestComponent

class QuestActivity : BaseSimpleActivity() , HasComponent<QuestComponent> {

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getComponent(): QuestComponent {
        return DaggerQuestComponent.builder()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    companion object{
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, QuestActivity::class.java)
            intent.putExtras(extras)
            return intent
        }

        fun getMerchantCoupon(context: Context, extras: Bundle): Intent {
            return getCallingIntent(context, extras)
        }
    }
}