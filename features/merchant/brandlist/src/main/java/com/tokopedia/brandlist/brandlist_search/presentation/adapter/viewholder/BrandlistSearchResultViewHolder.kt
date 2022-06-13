package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultUiModel
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener
import com.tokopedia.brandlist.databinding.ItemSearchResultBinding
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*


class BrandlistSearchResultViewHolder(view: View): AbstractViewHolder<BrandlistSearchResultUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_search_result
    }

    private var binding: ItemSearchResultBinding? by viewBinding()
    private val context: Context = itemView.context
    private val imgBrandLogo = binding?.ivBrandLogo
    private val imgBrandImage = binding?.ivBrandImage
    private val txtBrandName = binding?.tvBrandName

    override fun bind(element: BrandlistSearchResultUiModel) {
        processString(element.name, element.searchQuery)
        bindData(element.defaultUrl, element.logoUrl)
        setupApplink(element.appsUrl, element.listener, element.id)
    }

    private fun bindData(brandLogoUrl: String, brandImageUrl: String) {
        ImageHandler.loadImage(context, imgBrandLogo, brandLogoUrl, null)
        if(brandImageUrl.isNotBlank()) {
            ImageHandler.loadImage(context, imgBrandImage, brandImageUrl, null)
        } else {
            imgBrandImage?.visibility = View.GONE
        }

    }

    private fun processString(name: String, searchQuery: String) {
        val startIndex = indexOfSearchQuery(name, searchQuery)
        if (startIndex == -1) {
            txtBrandName?.text = name
        } else {
            val highlightedTitle = SpannableString(name)
            highlightedTitle.setSpan(TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            highlightedTitle.setSpan(TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                    startIndex + searchQuery.length,
                    name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            txtBrandName?.text = highlightedTitle
        }
    }

    private fun indexOfSearchQuery(displayName: String, searchTerm: String): Int {
        return if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun setupApplink(applink: String, tracking: BrandlistSearchTrackingListener, shopId: Int) {
        binding?.searchResult?.setOnClickListener {
            tracking.clickBrandOnSearchBox(shopId.toString())
            RouteManager.route(itemView.context, applink)
        }
    }
}