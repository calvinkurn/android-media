package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel

/**
 * @author : Steven 13/02/19
 */
interface PlayViewState {

    fun loadImageChannelBanner(context: Context, bannerUrl: String?, blurredBannerUrl: String?)
    fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?)
    fun getToolbar(): Toolbar?
    fun setSponsorData(adsId: String?, adsImageUrl: String?, adsName: String?)
    fun initVideoFragment(fragmentManager: FragmentManager, viewModel: ChannelInfoViewModel)
}