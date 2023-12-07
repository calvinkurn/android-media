package com.tokopedia.shareexperience.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.ui.ShareExBottomSheet

class ShareExperienceDebugActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shareexperience_debug_activity)

        ShareExBottomSheet().show(supportFragmentManager, "")
    }
}
