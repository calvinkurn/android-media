package com.tokopedia.chat_common

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.di.ChatRoomComponent
import com.tokopedia.chat_common.di.DaggerChatRoomComponent
import com.tokopedia.chat_common.view.fragment.TopChatRoomFragment
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel

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
        fun getCallingIntent(context: Context, messageId: String, name: String, label: String, senderId: String, role: String, mode: Int, keyword: String, image: String): Intent{
            val intent = Intent(context, BaseChatToolbarActivity::class.java)
            intent.putExtra("message_id", messageId)
            val model = ChatRoomHeaderViewModel()
            model.name = name
            model.label = label
            model.senderId = senderId
            model.role = role
            model.mode = mode
            model.keyword = keyword
            model.image = image
            intent.putExtra("header", model)
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
        toolbar.removeAllViews()
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


        intent.getParcelableExtra<ChatRoomHeaderViewModel>("header")?.let {

            ImageHandler.loadImageCircle2(this@BaseChatToolbarActivity, findViewById<ImageView>(R.id.user_avatar), it.image)
            (findViewById<TextView>(R.id.title)).text = it.name
            (findViewById<TextView>(R.id.label)).visibility = View.GONE
            it.label?.let {
                (findViewById<TextView>(R.id.label)).text = it
                (findViewById<TextView>(R.id.label)).visibility = View.VISIBLE
            }
            (findViewById<TextView>(R.id.subtitle)).visibility = View.GONE
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putString("message_id", intent.getStringExtra("message_id"))
        return TopChatRoomFragment.createInstance(bundle)
    }

}