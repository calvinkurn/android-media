package com.tokopedia.groupchat.room.view.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.tokopedia.groupchat.room.view.activity.PlayActivity
import com.tokopedia.groupchat.room.view.fragment.PlayFragment

/**
 * @author : Steven 12/02/19
 */
class FragmentPagerAdapter(fm: FragmentManager?, var channelId: String?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var bundle = Bundle()
        bundle.putString(PlayActivity.EXTRA_CHANNEL_UUID, channelId)
        return PlayFragment.createInstance(bundle)
    }

    override fun getCount(): Int {
        return 1
    }
}