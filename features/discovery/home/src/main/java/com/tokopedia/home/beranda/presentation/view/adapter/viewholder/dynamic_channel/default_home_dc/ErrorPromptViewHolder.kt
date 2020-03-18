package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.default_home_dc

import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.design.utils.StripedUnderlineUtil
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder

/**
 * Created by devarafikry on 01/02/20.
 */

class ErrorPromptViewHolder(val view: View,
                            val homeCategoryListener: HomeCategoryListener,
                            countDownListener: CountDownView.CountDownListener) :
        DynamicChannelViewHolder(
                view, homeCategoryListener, countDownListener
        ) {
    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_default_error_prompt
    }

    override fun getViewHolderClassName(): String {
        return ErrorPromptViewHolder::class.java.simpleName
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val imgErrorPrompt = view.findViewById<ImageView>(R.id.img_home_default_error)
        val tvDesc = view.findViewById<TextView>(R.id.default_home_desc_copy)

        val descString = view.context.resources.getString(R.string.discovery_home_desc_error)
        val actionString = view.context.resources.getString(R.string.discovery_home_action_error)
        val errorSpannable = SpannableString("$descString $actionString")
        errorSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.Green_G500)),
                descString.length+1, errorSpannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        errorSpannable.setSpan(
                StyleSpan(BOLD),descString.length+1, errorSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvDesc.text = errorSpannable

        imgErrorPrompt.loadImage(channel.banner.imageUrl)

        tvDesc.setOnClickListener { homeCategoryListener.refreshHomeData() }
    }
}
