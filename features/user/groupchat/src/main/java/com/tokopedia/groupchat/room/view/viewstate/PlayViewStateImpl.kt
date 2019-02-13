package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.common.util.TextFormatter

/**
 * @author : Steven 13/02/19
 */
class PlayViewStateImpl(var view: View) : PlayViewState {


    private var toolbar: Toolbar = view.findViewById(R.id.toolbar)
    private var channelBanner: ImageView = view.findViewById(R.id.channel_banner)
    var sponsorLayout = view.findViewById<View>(R.id.sponsor_layout)
    var sponsorImage = view.findViewById<ImageView>(R.id.sponsor_image)


    override fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?) {

        toolbar.title = title

        loadImageChannelBanner(view.context, bannerUrl, blurredBannerUrl)

        setToolbarParticipantCount(view.context, TextFormatter.format(totalView))

        when {
            title != null -> setVisibilityHeader(View.VISIBLE)
            else -> setVisibilityHeader(View.GONE)
        }
    }

    override fun loadImageChannelBanner(context: Context, bannerUrl: String?, blurredBannerUrl: String?) {
        if (TextUtils.isEmpty(blurredBannerUrl)) {
            ImageHandler.loadImageBlur(context, channelBanner, bannerUrl)
        } else {
            ImageHandler.LoadImage(channelBanner, blurredBannerUrl)
        }
    }
    private fun setToolbarParticipantCount(context: Context, totalParticipant: String) {
        val textParticipant = String.format("%s %s", totalParticipant, context.getString(R.string.view))
        toolbar.subtitle = textParticipant
    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    fun setVisibilityHeader(visible: Int) {
        toolbar.visibility = visible
        channelBanner.visibility = visible
    }


    override fun setSponsorData(adsId: String?, adsImageUrl: String?) {
        if(adsId == null) {
            sponsorLayout.visibility = View.GONE
        } else {
            sponsorLayout.visibility = View.VISIBLE

            ImageHandler.loadImage2(sponsorImage,
                    adsImageUrl,
                    R.drawable.loading_page)
        }
    }
}
