package com.tokopedia.sellerhome.settings.view.adapter.socialmedialinks

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.ItemSahSocialLinksBinding
import com.tokopedia.sellerhome.settings.analytics.SocialMediaLinksTracker
import com.tokopedia.sellerhome.settings.view.uimodel.socialmedialinks.SocialMediaLinkUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SocialMediaLinksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_sah_social_links
    }

    private val binding by viewBinding<ItemSahSocialLinksBinding>()

    fun bind(uiModel: SocialMediaLinkUiModel) {
        setupLayout(uiModel)
        setCtaClick(uiModel.eventAction, uiModel.ctaLink)
    }

    private fun setupLayout(uiModel: SocialMediaLinkUiModel) {
        itemView.context?.let { context ->
            binding?.run {
                ivSahItemSocial.setImageDrawable(uiModel.getDrawable(context))
                tvSahItemSocialTitle.text = uiModel.getTitle(context)
                tvSahItemSocialDesc.text = uiModel.getDescription(context)
            }
        }
    }

    private fun setCtaClick(eventAction: String,
                            link: String) {
        binding?.btnSahItemSocial?.run {
            setOnClickListener {
                SocialMediaLinksTracker.sendClickEvent(eventAction)
                val uri = Uri.parse(link)
                goToDefaultIntent(itemView.context, uri)
            }
        }
    }

    private fun goToDefaultIntent(context: Context?, uri: Uri) {
        try {
            val myIntent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

}