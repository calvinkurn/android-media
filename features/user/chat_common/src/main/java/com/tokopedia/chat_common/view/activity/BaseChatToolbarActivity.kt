package com.tokopedia.chat_common

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.di.ChatRoomComponent
import com.tokopedia.chat_common.di.DaggerChatRoomComponent
import com.tokopedia.chat_common.view.fragment.TopChatRoomFragment

/**
 * @author by nisie on 23/11/18.
 */
class BaseChatToolbarActivity : BaseChatActivity(), HasComponent<ChatRoomComponent> {
    override fun getComponent(): ChatRoomComponent {
        return DaggerChatRoomComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    companion object {

        @JvmStatic
        fun getCallingIntent(context: Context, messageId: String): Intent{
            val intent = Intent(context, BaseChatToolbarActivity::class.java)
            intent.putExtra("message_id", messageId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

    }

    protected fun setupToolbar() {
        val mInflater = LayoutInflater.from(this)
        val mCustomView = mInflater.inflate(R.layout.header_chat, null)
        toolbar.addView(mCustomView)
        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)

            val upArrow = MethodChecker.getDrawable(applicationContext, R.drawable.ic_action_back)
            if (upArrow != null) {
                upArrow.setColorFilter(
                        MethodChecker.getColor(this@BaseChatToolbarActivity, R.color.grey_700),
                        PorterDuff.Mode.SRC_ATOP
                )
                supportActionBar!!.setHomeAsUpIndicator(upArrow)
            }
        }

        title = ""
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.contentInsetEndWithActions = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 10f
        }

    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putString("message_id", intent.getStringExtra("message_id"))
        return TopChatRoomFragment.createInstance(bundle)
    }

}