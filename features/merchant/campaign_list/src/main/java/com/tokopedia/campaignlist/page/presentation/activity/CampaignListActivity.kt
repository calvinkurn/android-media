package com.tokopedia.campaignlist.page.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.page.presentation.fragment.CampaignListComposeFragment

class CampaignListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign_list)

        if (savedInstanceState == null) {
            val fragment = CampaignListComposeFragment.createInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }

    }



}