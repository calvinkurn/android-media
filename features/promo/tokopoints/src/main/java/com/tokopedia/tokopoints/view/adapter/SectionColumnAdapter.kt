package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.ImageList
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant

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
        holder.bindData(mItems?.get(position))
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
                titleBottom.hide()
            } else {
                titleBottom.show()
                titleBottom.text = item.title
            }
            if (TextUtils.isEmpty(item.subTitle)) {
                subTitleBottom.hide()
            } else {
                subTitleBottom.show()
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
            val data = mItems?.get(holder.adapterPosition)
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
            RouteManager.route(context, context?.resources?.getString(R.string.tp_webview_format)?.let { String.format(it, ApplinkConst.WEBVIEW, webLink) })
        } else {
            RouteManager.route(context, appLink)
        }
    }

}