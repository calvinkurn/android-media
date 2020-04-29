package com.tokopedia.entertainment.pdp.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.pdp.di.DaggerEventPDPComponent
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.fragment.EventCheckoutFragment

class EventCheckoutActivity : BaseSimpleActivity(), HasComponent<EventPDPComponent>{

    override fun getComponent(): EventPDPComponent =
            DaggerEventPDPComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getNewFragment(): Fragment? = EventCheckoutFragment.newInstance(
            intent.getStringExtra(EXTRA_URL_PDP),
            intent.getStringExtra(EXTRA_SCHEDULE_ID),
            intent.getStringExtra(EXTRA_GROUP_ID),
            intent.getStringExtra(EXTRA_PACKET_ID),
            intent.getIntExtra(EXTRA_AMOUNT,0)
    )

    companion object{
        const val EXTRA_URL_PDP= "EXTRA_URL_PDP"
        const val EXTRA_SCHEDULE_ID = "EXTRA_SCHEDULE_ID"
        const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
        const val EXTRA_PACKET_ID = "EXTRA_PACKET_ID"
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"

        fun createIntent(context: Context, urlPDP : String, scheduleID : String, groupID : String,
                         packetID : String, amount : Int
        ): Intent = Intent(context,EventCheckoutActivity::class.java)
                .putExtra(EXTRA_URL_PDP,urlPDP)
                .putExtra(EXTRA_SCHEDULE_ID, scheduleID)
                .putExtra(EXTRA_GROUP_ID,groupID)
                .putExtra(EXTRA_PACKET_ID,packetID)
                .putExtra(EXTRA_AMOUNT, amount)
    }

}