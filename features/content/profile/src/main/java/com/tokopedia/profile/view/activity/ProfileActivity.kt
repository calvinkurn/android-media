package com.tokopedia.profile.view.activity

import android.content.Context
import android.content.Intent

class ProfileActivity {

    companion object {
        fun createInstance(context: Context): Intent = Intent(context, ProfileActivity::class.java)
    }
}