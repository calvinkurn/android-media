package com.tokopedia.topchat.stub.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.topchat.R

class ChatListActivityStub: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_tab_list)
    }

    fun setupFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, "TAG_FRAGMENT")
                .commit()
    }
}