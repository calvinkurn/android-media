package com.tokopedia.feedplus.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.fragment.PlayFeedSeeMoreFragment

class PlayVideoLiveListActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
            val bundle = Bundle()
            intent.extras?.let {
                bundle.putString(ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE, it.getString(ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE))
                bundle.putString(ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_TYPE, it.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_TYPE))
                bundle.putString(ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_ID, it.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_ID))
                bundle.putString(ApplinkConstInternalFeed.PLAY_UPCOMING_FILTER_CATEGORY, it.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_FILTER_CATEGORY))
            }
            return PlayFeedSeeMoreFragment.createInstance(bundle)
        }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        hideStatusBar()
    }

    private fun hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


    override fun getLayoutRes(): Int {
        return R.layout.activity_play_feed_live_lehat_semua
    }
    override fun getParentViewResourceID(): Int {
        return R.id.fragment_container
    }
    fun getShopInfoLayout(): View? {
        return findViewById(R.id.shop_info_card)
    }


}