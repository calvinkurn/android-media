package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.CategoryTokopointsList
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import java.util.*

class SectionCategoryAdapter(
    private val context: Context?,
    private val mCategories:List<CategoryTokopointsList>?
) : RecyclerView.Adapter<SectionCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var viewCategoryNew: View? = null
        private var tvTitle: TextView? = null
        private var ivBg: AppCompatImageView? = null
        var isVisited = false

        init{
            ivBg = itemView.findViewById(R.id.iv_bg)
            tvTitle = itemView.findViewById(R.id.text_title)
            viewCategoryNew = itemView.findViewById(R.id.view_category_new)
        }

        fun bindData(category:CategoryTokopointsList?){
            category?.let{
                if (it.isNewCategory)
                    viewCategoryNew?.visibility = View.VISIBLE
                else
                    viewCategoryNew?.visibility = View.GONE

                if (URLUtil.isValidUrl(it.iconImageURL))
                    ImageHandler.loadImageCircle2(context, ivBg, it.iconImageURL)


                if (!TextUtils.isEmpty(it.text)) {
                    tvTitle?.text = it.text
                }

                itemView.setOnClickListener { _ ->
                    if (TextUtils.isEmpty(it.appLink)) {
                        RouteManager.route(
                            context, String.format(
                                Locale.getDefault(),
                                "%s?url=%s",
                                ApplinkConst.WEBVIEW,
                                Uri.encode(it.url)
                            )
                        )
                    } else {
                        RouteManager.route(context, it.appLink)
                    }

                    AnalyticsTrackerUtil.sendEvent(
                        itemView.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_DYNAMIC_ICON,
                        it.text
                    )
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.tp_item_section_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mCategories?.let {
            holder.bindData(it[position])
        }
    }

    override fun getItemCount() = mCategories?.size ?: 0

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        mCategories?.let{
            val data = mCategories[holder.adapterPosition]

            try{
                if (!holder.isVisited) {
                    AnalyticsTrackerUtil.sendEvent(
                        holder.itemView.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_DYNAMIC_CAT,
                        data.text
                    )
                    holder.isVisited = true
                }
            }
            catch (err:Exception){}
        }
    }
}
