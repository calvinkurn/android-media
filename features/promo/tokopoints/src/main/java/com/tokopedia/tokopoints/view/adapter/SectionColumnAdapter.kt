package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.ImageList
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.convertDpToPixel

class SectionColumnAdapter(private val mItems: List<ImageList>?, private val mType: String) : RecyclerView.Adapter<SectionColumnAdapter.ViewHolder?>() {
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view: View
        view = when (mType) {
            CommonConstant.BannerType.COLUMN_3_1_BY_1 -> LayoutInflater.from(parent.context).inflate(R.layout.tp_layout_column_1_on_1, parent, false)
            CommonConstant.BannerType.COLUMN_2_3_BY_4 -> LayoutInflater.from(parent.context).inflate(R.layout.tp_layout_column_3_on_1, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.tp_layout_column_2_on_1, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val params = holder.itemView.layoutParams as MarginLayoutParams
        if (position == 0) {
            params.setMargins(convertDpToPixel(10, holder.itemView.context), 0, 0, 0)
        } else if (position == mItems?.size) {
            params.setMargins(0, 0, convertDpToPixel(16, holder.itemView.context), 0)

        } else {
            params.setMargins(convertDpToPixel(16, holder.itemView.context), 0, 0, 0)
        }
        holder.itemView.layoutParams = params
        holder.bindData(mItems!![position])
    }

    override fun getItemCount(): Int {
        return mItems?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCol: ImageView
        private val bnrTitle: TextView
        private val bnrSubTitle: TextView
        private val titleBottom: TextView
        private val subTitleBottom: TextView
        var isVisited = false
        fun bindData(item: ImageList?) {
            if (item == null) {
                return
            }
            if (URLUtil.isValidUrl(item.imageURLMobile)) {
                ImageHandler.loadImageFitCenter(context, ivCol, item.imageURLMobile)
            }
            bnrTitle.text = item.inBannerTitle
            bnrSubTitle.text = item.inBannerSubTitle
            itemView.setOnClickListener { view: View? -> handledClick(item.redirectAppLink, item.redirectURL) }
            if (TextUtils.isEmpty(item.title)) {
                titleBottom.visibility = View.GONE
            } else {
                titleBottom.visibility = View.VISIBLE
                titleBottom.text = item.title
            }
            if (TextUtils.isEmpty(item.subTitle)) {
                subTitleBottom.visibility = View.GONE
            } else {
                subTitleBottom.visibility = View.VISIBLE
                subTitleBottom.text = item.subTitle
            }
        }

        init {
            ivCol = itemView.findViewById(R.id.iv_col_1)
            bnrTitle = itemView.findViewById(R.id.text_title_banner)
            bnrSubTitle = itemView.findViewById(R.id.text_sub_title_banner)
            titleBottom = itemView.findViewById(R.id.text_title_bottom_1)
            subTitleBottom = itemView.findViewById(R.id.text_sub_title_bottom_1)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        try {
            val data = mItems!![holder.adapterPosition]
            if (data == null || holder.itemView == null) {
                return
            }
            if (!holder.isVisited) {
                AnalyticsTrackerUtil.sendEvent(holder.itemView.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_DYNAMIC_ICON,
                        data.title)
                holder.isVisited = true
            }
        } catch (e: Exception) {
        }
    }

    fun handledClick(appLink: String?, webLink: String?) {
        if (TextUtils.isEmpty(appLink)) {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, webLink))
        } else {
            RouteManager.route(context, appLink)
        }
    }

}