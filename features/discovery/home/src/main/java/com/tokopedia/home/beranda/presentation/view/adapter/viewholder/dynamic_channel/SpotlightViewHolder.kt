package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.LinearHorizontalSpacingDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifyprinciples.Typography

import java.util.ArrayList

class SpotlightViewHolder(itemView: View, val listener: HomeCategoryListener) : AbstractViewHolder<SpotlightViewModel>(itemView) {
    private val recyclerView: RecyclerView
    private val adapter: SpotlightAdapter

    init {
        adapter = SpotlightAdapter(listener)
        recyclerView = itemView.findViewById(R.id.list)
        val edgeMargin = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16)
        val spacingBetween = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_8)
        recyclerView.addItemDecoration(LinearHorizontalSpacingDecoration(spacingBetween, edgeMargin))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.HORIZONTAL, false)
    }

    override fun bind(element: SpotlightViewModel) {
        adapter.setData(element.spotlightItems)
        if (!element.isCache) {
            itemView.addOnImpressionListener(
                    element, OnSpotlightImpression(element, listener, adapterPosition)
            )
        }
    }

    private class SpotlightAdapter(private val listener: HomeCategoryListener) : RecyclerView.Adapter<SpotlightItemViewHolder>() {
        private val spotlightItemViewModels = ArrayList<SpotlightItemViewModel>()

        fun setData(spotlightItemViewModels: List<SpotlightItemViewModel>) {
            this.spotlightItemViewModels.clear()
            this.spotlightItemViewModels.addAll(spotlightItemViewModels)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotlightItemViewHolder {
            return SpotlightItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_spotlight_item, parent, false),
                    listener)
        }

        override fun onBindViewHolder(holder: SpotlightItemViewHolder, position: Int) {
            holder.bind(spotlightItemViewModels[position], position)
        }

        override fun getItemCount(): Int {
            return spotlightItemViewModels.size
        }
    }

    private class SpotlightItemViewHolder(itemView: View, private val listener: HomeCategoryListener) : RecyclerView.ViewHolder(itemView) {

        private var title: Typography = itemView.findViewById(R.id.spotlightTitle)
        private val tag: TextView = itemView.findViewById(R.id.spotlightTag)
        private val description: TextView = itemView.findViewById(R.id.spotlightDesc)
        private val background: ImageView = itemView.findViewById(R.id.spotlightBackground)
        private val container: View = itemView.findViewById(R.id.spotlightContainer)
        private val context: Context = itemView.context

        fun bind(model: SpotlightItemViewModel, position: Int) {
            /**
             * Hardcoded spotlight title to use dip unit
             * prevent spotlight title increase text size
             * when user font size preference is large
             */
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16).toFloat())
            title.text = model.title
            if (!TextUtils.isEmpty(model.tagName)) {
                tag.text = model.tagName.toUpperCase()
                tag.setTextColor(Color.parseColor(model.tagNameHexcolor))
                ViewCompat.setBackgroundTintList(
                        tag,
                        ColorStateList.valueOf(Color.parseColor(model.tagHexcolor)))
                tag.visibility = View.VISIBLE
            } else {
                tag.visibility = View.GONE
            }

            val longDescription = SpannableStringBuilder()
            longDescription.append(model.description)
            longDescription.append(" ")
            val start = longDescription.length
            longDescription.append(model.ctaText)
            longDescription.setSpan(ForegroundColorSpan(Color.parseColor(model.ctaTextHexcolor)), start, longDescription.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            longDescription.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, longDescription.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            description.text = longDescription

            ImageHandler.loadImageFitCenter(context, background, model.backgroundImageUrl)

            container.setOnClickListener { view ->
                eventClickSpotlight(view.context, model, position)
                listener.onSpotlightItemClicked(DynamicLinkHelper.getActionLink(model))
            }
        }

        private fun eventClickSpotlight(context: Context, model: SpotlightItemViewModel, position: Int) {
            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(context, model.getEnhanceClickSpotlightHomePage(position, model.channeldId))
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_spotlight
    }
}

class OnSpotlightImpression(
        val spotlightViewModel: SpotlightViewModel,
        val listener: HomeCategoryListener,
        val position: Int) : ViewHintListener {
    override fun onViewHint() {
        listener.putEEToIris(
                HomePageTracking.getIrisEnhanceImpressionSpotlightHomePage(
                        spotlightViewModel.channelId,
                        spotlightViewModel.spotlightItems,
                        position
                )
        )
    }
}

