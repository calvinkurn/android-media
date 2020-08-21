package com.tokopedia.tokopoints.view.tokopointhome

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.*
import com.tokopedia.tokopoints.view.model.section.ImageList
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.TimerUnify
import kotlinx.android.synthetic.main.tp_topads_reward_layout.view.*
import java.util.*

class ExploreSectionPagerAdapter(context: Context?, presenter: TokoPointsHomeViewModel, sections: List<SectionContent>?, val topAdsImageViewModel: TopAdsImageViewModel?) : PagerAdapter() {
    private val mLayoutInflater: LayoutInflater
    private val mSections: List<SectionContent>?
    private val mPresenter: TokoPointsHomeViewModel
    private val swipeToRefresh = arrayOfNulls<SwipeToRefresh>(2)
    private var countDownView: TimerUnify? = null
    private val mCouponListAdapterList: ArrayList<CouponListAdapter>? = ArrayList()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = null
        if (position == TAB_EXPLORE) {
            view = mLayoutInflater.inflate(R.layout.tp_view_section_explore, container, false)
            setUpExploreTab(view)
        }
        view!!.tag = position
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return CommonConstant.HOMEPAGE_TAB_COUNT
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    fun setRefreshing(refresh: Boolean) {
        for (swipeToRefrsh in swipeToRefresh) {
            if (swipeToRefrsh != null) swipeToRefrsh.isRefreshing = refresh
        }
    }

    fun setUpExploreTab(view: View?) {
        if (view == null || mSections == null) {
            return
        }
        val container = view.findViewById<LinearLayout>(R.id.ll_sections)
        for (sectionContent in mSections) {
            if (sectionContent == null) {
                continue
            }
            if (sectionContent.layoutCouponAttr != null && sectionContent.layoutCouponAttr.couponList != null && !sectionContent.layoutCouponAttr.couponList.isEmpty()) {
                container.addView(getCouponCard(sectionContent))
                continue
            }
            if (sectionContent.layoutCatalogAttr != null && sectionContent.layoutCatalogAttr.catalogList != null && !sectionContent.layoutCatalogAttr.catalogList.isEmpty()) {
                container.addView(getCatalogCarousel(sectionContent))
                continue
            }

            if (sectionContent.layoutTopAdsAttr != null && !sectionContent.layoutTopAdsAttr.jsonTopAdsDisplayParam.isEmpty() && topAdsImageViewModel != null) {
                container.addView(getTopadsBanner(sectionContent, topAdsImageViewModel))
                continue
            }

            if (sectionContent.layoutBannerAttr == null
                    || sectionContent.layoutBannerAttr.bannerType == null) {
                continue
            }
            when (sectionContent.layoutBannerAttr.bannerType) {
                CommonConstant.BannerType.BANNER_1_1 -> container.addView(getBanner1on1(sectionContent))
                CommonConstant.BannerType.BANNER_2_1 -> container.addView(getBanner2on1(sectionContent))
                CommonConstant.BannerType.BANNER_3_1 -> container.addView(getBanner3on1(sectionContent))
                CommonConstant.BannerType.COLUMN_3_1_BY_1 -> container.addView(getCaulmn311(sectionContent))
                CommonConstant.BannerType.COLUMN_2_1_BY_1 -> container.addView(getCaulmn211(sectionContent))
                CommonConstant.BannerType.COLUMN_2_3_BY_4 -> container.addView(getCaulmn234(sectionContent))
                CommonConstant.BannerType.CAROUSEL_1_1 -> container.addView(getCarousel1on1(sectionContent))
                CommonConstant.BannerType.CAROUSEL_2_1 -> container.addView(getCarousel2on1(sectionContent))
                CommonConstant.BannerType.CAROUSEL_3_1 -> container.addView(getCarousel3on1(sectionContent))
                else -> {
                }
            }
        }
    }

    private fun getTopadsBanner(content: SectionContent, topAdsImageViewModel: TopAdsImageViewModel?): View? {


        val view = mLayoutInflater.inflate(R.layout.tp_topads_reward_layout, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.tv_topads_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.tv_topad_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.tv_topad_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.tv_topads_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.tv_topads_sub_title) as TextView).text = content.sectionSubTitle
        }

        topAdsImageViewModel?.let { view.topads_reward.loadImage(it,16) }

        view.topads_reward.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {

                sendBannerImpression(content.sectionTitle)
                TopAdsUrlHitter(className).hitImpressionUrl(
                        view.context,
                        viewUrl,
                        "",
                        "",
                        ""
                )
            }
        })

        view.topads_reward.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                RouteManager.route(view.context, applink)
            }
        })
        return view

    }

    fun getCatalogCarousel(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false)
        if (content == null || content.sectionTitle == null || content.layoutCatalogAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (content.countdownAttr != null &&
                content.countdownAttr.isShowTimer && content.countdownAttr.expiredCountDown > 0) {
            countDownView = view.findViewById(R.id.tp_count_down_view)
            countDownView?.findViewById<View>(R.id.tp_count_down_view)?.visibility = View.VISIBLE
            countDownView?.remainingMilliseconds = content.countdownAttr.expiredCountDown * 1000
            countDownView?.onFinish = { view?.visibility = View.GONE }
            view.findViewById<View>(R.id.text_title).layoutParams.width = view.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_180)
        } else {
            view.findViewById<View>(R.id.text_title).layoutParams.width = view.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_280)
        }
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_CATALOG, content.sectionTitle)
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutCatalogAttr.catalogList != null) {
            view.setPadding(0, view.paddingTop, 0, 0)
            val rvCarousel: RecyclerView = view.findViewById(R.id.rv_carousel)
            rvCarousel.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = CatalogListCarouselAdapter(mPresenter, content.layoutCatalogAttr.catalogList, rvCarousel)
            rvCarousel.adapter = adapter
        }
        return view
    }

    fun getBanner1on1(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_banner_1_on_1, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null && !content.layoutBannerAttr.imageList.isEmpty()
                && URLUtil.isValidUrl(content.layoutBannerAttr.imageList[0].imageURLMobile)) {
            val imgBanner = view.findViewById<ImageView>(R.id.img_banner)
            val data = content.layoutBannerAttr.imageList[0]
            ImageHandler.loadImageFitCenter(imgBanner.context, imgBanner, data.imageURLMobile)
            imgBanner.setOnClickListener { v: View? ->
                handledClick(data.redirectAppLink, data.redirectURL, "", "")
                if (!TextUtils.isEmpty(content.sectionTitle)) sendBannerClick(content.sectionTitle)
            }
            (view.findViewById<View>(R.id.text_title_banner) as TextView).text = data.inBannerTitle
            (view.findViewById<View>(R.id.text_sub_title_banner) as TextView).text = data.inBannerSubTitle
            if (!TextUtils.isEmpty(data.title)) {
                view.findViewById<View>(R.id.text_title_bottom).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.text_title_bottom) as TextView).text = data.title
            }
            if (!TextUtils.isEmpty(data.subTitle)) {
                view.findViewById<View>(R.id.text_sub_title_bottom).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.text_sub_title_bottom) as TextView).text = data.subTitle
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) sendBannerImpression(content.sectionTitle)
        return view
    }

    fun getBanner2on1(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_banner_2_on_1, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null && !content.layoutBannerAttr.imageList.isEmpty()
                && URLUtil.isValidUrl(content.layoutBannerAttr.imageList[0].imageURLMobile)) {
            val imgBanner = view.findViewById<ImageView>(R.id.img_banner)
            val data = content.layoutBannerAttr.imageList[0]
            ImageHandler.loadImageFitCenter(imgBanner.context, imgBanner, data.imageURLMobile)
            imgBanner.setOnClickListener { v: View? ->
                handledClick(data.redirectAppLink, data.redirectURL, "", "")
                if (!TextUtils.isEmpty(content.sectionTitle)) sendBannerClick(content.sectionTitle)
            }
            (view.findViewById<View>(R.id.text_title_banner) as TextView).text = data.inBannerTitle
            (view.findViewById<View>(R.id.text_sub_title_banner) as TextView).text = data.inBannerSubTitle
            if (!TextUtils.isEmpty(data.title)) {
                view.findViewById<View>(R.id.text_title_bottom).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.text_title_bottom) as TextView).text = data.title
            }
            if (!TextUtils.isEmpty(data.subTitle)) {
                view.findViewById<View>(R.id.text_sub_title_bottom).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.text_sub_title_bottom) as TextView).text = data.subTitle
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) sendBannerImpression(content.sectionTitle)
        return view
    }

    fun getBanner3on1(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_banner_3_on_1, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null && !content.layoutBannerAttr.imageList.isEmpty()
                && URLUtil.isValidUrl(content.layoutBannerAttr.imageList[0].imageURLMobile)) {
            val imgBanner = view.findViewById<ImageView>(R.id.img_banner)
            val data = content.layoutBannerAttr.imageList[0]
            ImageHandler.loadImageFitCenter(imgBanner.context, imgBanner, data.imageURLMobile)
            imgBanner.setOnClickListener { v: View? ->
                handledClick(data.redirectAppLink, data.redirectURL, "", "")
                if (!TextUtils.isEmpty(content.sectionTitle)) sendBannerClick(content.sectionTitle)
            }
            (view.findViewById<View>(R.id.text_title_banner) as TextView).text = data.inBannerTitle
            (view.findViewById<View>(R.id.text_sub_title_banner) as TextView).text = data.inBannerSubTitle
            if (!TextUtils.isEmpty(data.title)) {
                view.findViewById<View>(R.id.text_title_bottom).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.text_title_bottom) as TextView).text = data.title
            }
            if (!TextUtils.isEmpty(data.subTitle)) {
                view.findViewById<View>(R.id.text_sub_title_bottom).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.text_sub_title_bottom) as TextView).text = data.subTitle
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) sendBannerImpression(content.sectionTitle)
        return view
    }

    fun getCaulmn311(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_column_1_on_1, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null) {
            var data: ImageList? = null
            var imgCol: ImageView? = null
            if (content.layoutBannerAttr.imageList.size > 0) {
                data = content.layoutBannerAttr.imageList[0]
                val appLink = data.redirectAppLink
                val webLink = data.redirectURL
                imgCol = view.findViewById(R.id.iv_col_1)
                ImageHandler.loadImageFitCenter(imgCol.context, imgCol, data.imageURLMobile)
                imgCol.setOnClickListener(View.OnClickListener { v: View? -> handledClick(appLink, webLink, "", "") })
                (view.findViewById<View>(R.id.text_title_banner) as TextView).text = data.inBannerTitle
                (view.findViewById<View>(R.id.text_sub_title_banner) as TextView).text = data.inBannerSubTitle
                setText(view.findViewById(R.id.text_title_bottom_1), data.title)
                setText(view.findViewById(R.id.text_sub_title_bottom_1), data.subTitle)
            }
            if (content.layoutBannerAttr.imageList.size > 1) {
                data = content.layoutBannerAttr.imageList[1]
                val appLink = data.redirectAppLink
                val webLink = data.redirectURL
                imgCol = view.findViewById(R.id.iv_col_2)
                ImageHandler.loadImageFitCenter(imgCol.context, imgCol, data.imageURLMobile)
                imgCol.setOnClickListener(View.OnClickListener { v: View? -> handledClick(appLink, webLink, "", "") })
                (view.findViewById<View>(R.id.text_title_banner2) as TextView).text = data.inBannerTitle
                (view.findViewById<View>(R.id.text_sub_title_banner2) as TextView).text = data.inBannerSubTitle
                setText(view.findViewById(R.id.text_title_bottom_2), data.title)
                setText(view.findViewById(R.id.text_sub_title_bottom_2), data.subTitle)
            }
            if (content.layoutBannerAttr.imageList.size > 2) {
                data = content.layoutBannerAttr.imageList[2]
                val appLink = data.redirectAppLink
                val webLink = data.redirectURL
                imgCol = view.findViewById(R.id.iv_col_3)
                ImageHandler.loadImageFitCenter(imgCol.context, imgCol, data.imageURLMobile)
                imgCol.setOnClickListener(View.OnClickListener { v: View? -> handledClick(appLink, webLink, "", "") })
                (view.findViewById<View>(R.id.text_title_banner3) as TextView).text = data.inBannerTitle
                (view.findViewById<View>(R.id.text_sub_title_banner3) as TextView).text = data.inBannerSubTitle
                setText(view.findViewById(R.id.text_title_bottom_3), data.title)
                setText(view.findViewById(R.id.text_sub_title_bottom_3), data.subTitle)
            }
        }
        return view
    }

    fun getCaulmn211(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_column_2_on_1, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink,
                        content.cta.url, AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null) {
            var data: ImageList? = null
            var imgCol: ImageView? = null
            if (content.layoutBannerAttr.imageList.size > 0) {
                data = content.layoutBannerAttr.imageList[0]
                val appLink = data.redirectAppLink
                val webLink = data.redirectURL
                imgCol = view.findViewById(R.id.iv_col_1)
                ImageHandler.loadImageFitCenter(imgCol.context, imgCol, data.imageURLMobile)
                imgCol.setOnClickListener(View.OnClickListener { v: View? -> handledClick(appLink, webLink, "", "") })
                (view.findViewById<View>(R.id.text_title_banner) as TextView).text = data.inBannerTitle
                (view.findViewById<View>(R.id.text_sub_title_banner) as TextView).text = data.inBannerSubTitle
                setText(view.findViewById(R.id.text_title_bottom_1), data.title)
                setText(view.findViewById(R.id.text_sub_title_bottom_1), data.subTitle)
            }
            if (content.layoutBannerAttr.imageList.size > 1) {
                data = content.layoutBannerAttr.imageList[1]
                val appLink = data.redirectAppLink
                val webLink = data.redirectURL
                imgCol = view.findViewById(R.id.iv_col_2)
                ImageHandler.loadImageFitCenter(imgCol.context, imgCol, data.imageURLMobile)
                imgCol.setOnClickListener(View.OnClickListener { v: View? -> handledClick(appLink, webLink, "", "") })
                (view.findViewById<View>(R.id.text_title_banner2) as TextView).text = data.inBannerTitle
                (view.findViewById<View>(R.id.text_sub_title_banner2) as TextView).text = data.inBannerSubTitle
                setText(view.findViewById(R.id.text_title_bottom_2), data.title)
                setText(view.findViewById(R.id.text_sub_title_bottom_2), data.subTitle)
            }
        }
        return view
    }

    fun getCaulmn234(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_column_3_on_1, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null) {
            var data: ImageList? = null
            var imgCol: ImageView? = null
            if (content.layoutBannerAttr.imageList.size > 0) {
                data = content.layoutBannerAttr.imageList[0]
                val appLink = data.redirectAppLink
                val webLink = data.redirectURL
                imgCol = view.findViewById(R.id.iv_col_1)
                ImageHandler.loadImageFitCenter(imgCol.context, imgCol, data.imageURLMobile)
                imgCol.setOnClickListener(View.OnClickListener { v: View? -> handledClick(appLink, webLink, "", "") })
                (view.findViewById<View>(R.id.text_title_banner) as TextView).text = data.inBannerTitle
                (view.findViewById<View>(R.id.text_sub_title_banner) as TextView).text = data.inBannerSubTitle
                setText(view.findViewById(R.id.text_title_bottom_1), data.title)
                setText(view.findViewById(R.id.text_sub_title_bottom_1), data.subTitle)
            }
            if (content.layoutBannerAttr.imageList.size > 1) {
                data = content.layoutBannerAttr.imageList[1]
                imgCol = view.findViewById(R.id.iv_col_2)
                val appLink = data.redirectAppLink
                val webLink = data.redirectURL
                ImageHandler.loadImageFitCenter(imgCol.context, imgCol, data.imageURLMobile)
                imgCol.setOnClickListener(View.OnClickListener { v: View? -> handledClick(appLink, webLink, "", "") })
                (view.findViewById<View>(R.id.text_title_banner2) as TextView).text = data.inBannerTitle
                (view.findViewById<View>(R.id.text_sub_title_banner2) as TextView).text = data.inBannerSubTitle
                setText(view.findViewById(R.id.text_title_bottom_2), data.title)
                setText(view.findViewById(R.id.text_sub_title_bottom_2), data.subTitle)
            }
        }
        return view
    }

    fun getCarousel1on1(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null) {
            val rvCarousel: RecyclerView = view.findViewById(R.id.rv_carousel)
            rvCarousel.addItemDecoration(CarouselItemDecoration(mLayoutInflater.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_4)))
            rvCarousel.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            rvCarousel.adapter = SectionCarouselAdapter(content.layoutBannerAttr.imageList, CommonConstant.BannerType.CAROUSEL_1_1)
        }
        return view
    }

    fun getCarousel2on1(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null) {
            val rvCarousel: RecyclerView = view.findViewById(R.id.rv_carousel)
            rvCarousel.addItemDecoration(CarouselItemDecoration(mLayoutInflater.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_4)))
            rvCarousel.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            rvCarousel.adapter = SectionCarouselAdapter(content.layoutBannerAttr.imageList, CommonConstant.BannerType.CAROUSEL_2_1)
        }
        return view
    }

    fun getCarousel3on1(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false)
        if (content == null || content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (content.layoutBannerAttr.imageList != null) {
            val rvCarousel: RecyclerView = view.findViewById(R.id.rv_carousel)
            rvCarousel.addItemDecoration(CarouselItemDecoration(mLayoutInflater.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_4)))
            rvCarousel.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            rvCarousel.adapter = SectionCarouselAdapter(content.layoutBannerAttr.imageList, CommonConstant.BannerType.CAROUSEL_3_1)
        }
        return view
    }

    fun getCouponCard(content: SectionContent?): View {
        val view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false)
        if (content == null || content.sectionTitle == null || content.layoutCatalogAttr == null) {
            view.visibility = View.GONE
            return view
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (content.countdownAttr != null &&
                content.countdownAttr.isShowTimer && content.countdownAttr.expiredCountDown > 0) {
            countDownView = view.findViewById(R.id.tp_count_down_view)
            countDownView?.findViewById<View>(R.id.tp_count_down_view)?.visibility = View.VISIBLE
            countDownView?.remainingMilliseconds = content.countdownAttr.expiredCountDown * 1000
            countDownView?.onFinish = { view?.visibility = View.GONE }

            view.findViewById<View>(R.id.text_title).layoutParams.width = view.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_180)
        } else {
            view.findViewById<View>(R.id.text_title).layoutParams.width = view.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_280)
        }
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_CATALOG, content.sectionTitle)
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutCatalogAttr.catalogList != null) {
            view.setPadding(0, view.paddingTop, 0, 0)
            val rvCarousel: RecyclerView = view.findViewById(R.id.rv_carousel)
            rvCarousel.addItemDecoration(NonCarouselItemDecoration(mLayoutInflater.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_4)))
            rvCarousel.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = CouponListAdapter(content.layoutCouponAttr.couponList)
            rvCarousel.adapter = adapter
            mCouponListAdapterList!!.add(adapter)
        }
        return view
    }

    fun handledClick(appLink: String?, webLink: String?, action: String?, label: String?) {
        try {
            if (mLayoutInflater.context == null) {
                return
            }
            if (TextUtils.isEmpty(appLink)) {
                RouteManager.getIntent(mLayoutInflater.context, ApplinkConstInternalGlobal.WEBVIEW, webLink)
            } else {
                RouteManager.route(mLayoutInflater.context, appLink)
            }
            AnalyticsTrackerUtil.sendEvent(mLayoutInflater.context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    action,
                    label)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendBannerImpression(bannerName: String) {
        val promotionItem = HashMap<String, Any>()
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.NAME] = "/tokopoints - p{x} - promo lis"
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.POSITION] = -1
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.CREATIVE] = bannerName
        val promotionMap = HashMap<String, Any>()
        promotionMap[AnalyticsTrackerUtil.EcommerceKeys.PROMOTIONS] = listOf(promotionItem)
        AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.VIEW_BANNERS_ON_HOME_TOKOPOINTS,
                bannerName, promotionMap)
    }

    private fun sendBannerClick(bannerName: String) {
        val promotionItem = HashMap<String, Any>()
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.NAME] = "/tokopoints - p{x} - promo lis"
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.POSITION] = -1
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.CREATIVE] = bannerName
        val promotionMap = HashMap<String, Any>()
        promotionMap[AnalyticsTrackerUtil.EcommerceKeys.PROMOTIONS] = listOf(promotionItem)
        AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.CLICK_BANNERS_ON_HOME_TOKOPOINTS,
                bannerName, promotionMap)
    }

    private fun setText(view: View?, text: String) {
        if (view == null) {
            return
        }
        if (view is TextView && !TextUtils.isEmpty(text)) {
            view.text = text
            view.setVisibility(View.VISIBLE)
        } else {
            view.visibility = View.GONE
        }
    }

    fun onDestroyView() {
        if (mCouponListAdapterList != null) {
            for (adapter in mCouponListAdapterList) {
                adapter.onDestroyView()
            }
        }
    }

    companion object {
        const val TAB_EXPLORE = 0
        const val TAB_MY_COUPON = 1
        private const val className = "com.tokopedia.tokopoints.view.tokopointhome"
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mSections = sections
        mPresenter = presenter
    }
}