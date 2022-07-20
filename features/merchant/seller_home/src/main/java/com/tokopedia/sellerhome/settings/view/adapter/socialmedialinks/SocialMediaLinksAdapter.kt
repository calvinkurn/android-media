package com.tokopedia.sellerhome.settings.view.adapter.socialmedialinks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.analytics.SocialMediaLinksTracker
import com.tokopedia.sellerhome.settings.view.uimodel.socialmedialinks.SocialMediaLinkUiModel

class SocialMediaLinksAdapter(private var context: Context) :
    RecyclerView.Adapter<SocialMediaLinksViewHolder>() {

    companion object {
        private const val IG_LINK = "https://www.instagram.com/tokopediaseller/"
        private const val YT_LINK = "https://www.youtube.com/channel/UCu6IZtHGs6bsh5Hue0jAQsQ"
        private const val FB_APPLINK = "fb://page/2023042821056179"
        private const val FB_LINK = "https://www.facebook.com/tokopediaseller/"
        private const val FB_FAMILY_LINK = "https://www.facebook.com/groups/2362730034006601/"

        private fun getSocialMediaItems(): List<SocialMediaLinkUiModel> =
            listOf(
                SocialMediaLinkUiModel(
                    R.drawable.ic_sah_social_ig,
                    R.string.sah_social_ig_title,
                    R.string.sah_social_ig_desc,
                    IG_LINK,
                    "",
                    SocialMediaLinksTracker.EventAction.CLICK_IG
                ),
                SocialMediaLinkUiModel(
                    R.drawable.ic_sah_social_yt,
                    R.string.sah_social_youtube_fb_title,
                    R.string.sah_social_youtube_desc,
                    YT_LINK,
                    "",
                    SocialMediaLinksTracker.EventAction.CLICK_YT
                ),
                SocialMediaLinkUiModel(
                    R.drawable.ic_sah_social_fb,
                    R.string.sah_social_youtube_fb_title,
                    R.string.sah_social_fb_desc,
                    FB_APPLINK,
                    FB_LINK,
                    SocialMediaLinksTracker.EventAction.CLICK_FB
                ),
                SocialMediaLinkUiModel(
                    R.drawable.ic_sah_social_fb,
                    R.string.sah_social_fb_com_title,
                    R.string.sah_social_fb_com_desc,
                    FB_FAMILY_LINK,
                    "",
                    SocialMediaLinksTracker.EventAction.CLICK_COM_FB
                )
            )
    }

    private val itemList = getSocialMediaItems()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialMediaLinksViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(
                SocialMediaLinksViewHolder.LAYOUT,
                parent,
                false)
        return SocialMediaLinksViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SocialMediaLinksViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

}