package com.tokopedia.tokopoints.view.tokopointhome.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.NonCarouselItemDecoration
import com.tokopedia.tokopoints.view.adapter.SectionCategoryAdapter
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder
import com.tokopedia.tokopoints.view.util.convertDpToPixel

class SectionVerticalCategoryViewBinder()
    : SectionItemViewBinder<SectionContent, SectionVerticalCategoryVH>(
        SectionContent::class.java) {

    override fun createViewHolder(parent: ViewGroup): SectionVerticalCategoryVH {
        return SectionVerticalCategoryVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionVerticalCategoryVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_layout_section_category_parent

}

class SectionVerticalCategoryVH(val viewCategory: View) : RecyclerView.ViewHolder(viewCategory) {

    lateinit var tvSectionSubtitleCateory: TextView
    lateinit var tvSectionTitleCategory: TextView
    lateinit var dynamicLinksContainer: View
    lateinit var categorySeeAll: TextView
    private var mRvDynamicLinks: RecyclerView? = null


    fun bind(content: SectionContent) {

        tvSectionTitleCategory = viewCategory.findViewById(R.id.tv_sectionTitle)
        tvSectionSubtitleCateory = viewCategory.findViewById(R.id.tv_ovopointValue)
        dynamicLinksContainer = viewCategory.findViewById(R.id.container_dynamic_links)
        categorySeeAll = viewCategory.findViewById(R.id.tv_seeall_category)
        mRvDynamicLinks = viewCategory.findViewById(R.id.rv_dynamic_link)
        if (content.layoutCategoryAttr == null || content.layoutCategoryAttr.categoryTokopointsList == null || content.layoutCategoryAttr.categoryTokopointsList.isEmpty()) {
            return
        }
        if (content.sectionTitle.isNotEmpty()) {
            tvSectionTitleCategory.show()
            tvSectionTitleCategory.text = content.sectionTitle
        }
        if (content.sectionSubTitle.isNotEmpty()) {
            tvSectionSubtitleCateory.show()
            tvSectionSubtitleCateory.text = content.sectionSubTitle
        }
        dynamicLinksContainer.visibility = View.VISIBLE

        content.cta?.let {
            if (it.text.isNotEmpty()) {
                categorySeeAll.text = it.text
                categorySeeAll.show()
            }
            if (it.appLink.isNotEmpty()) {
                categorySeeAll.setOnClickListener { _ ->
                    RouteManager.route(viewCategory.context, it.appLink)
                }
            } else if (it.appLink.isEmpty() && it.url.isNotEmpty()) {
                categorySeeAll.setOnClickListener { _ ->
                    openWebView(it.url)
                }
            }
        }
        val manager = LinearLayoutManager(viewCategory.context, LinearLayoutManager.HORIZONTAL, false)
        mRvDynamicLinks?.layoutManager = manager
        if (mRvDynamicLinks?.itemDecorationCount == 0) {
            mRvDynamicLinks?.addItemDecoration(NonCarouselItemDecoration(convertDpToPixel(16, viewCategory.context)))
        }
        mRvDynamicLinks?.adapter = SectionCategoryAdapter(viewCategory.context, content.layoutCategoryAttr.categoryTokopointsList)

    }

    fun openWebView(url: String) {
        RouteManager.route(viewCategory.context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

}
