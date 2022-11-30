package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.net.Uri
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
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.ImageList
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import java.util.*

class SectionCarouselAdapter(
    private val mItems:List<ImageList>?,
    private val mType:String?
) : RecyclerView.Adapter<SectionCarouselAdapter.ViewHolder>() {
    private var context: Context?=null

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        private var ivCol: ImageView? = null
        private var bnrTitle: TextView? = null
        private  var bnrSubTitle:TextView? = null
        private  var titleBottom:TextView? = null
        private  var subTitleBottom:TextView? = null
        var isVisited = false

        init {
            ivCol = itemView.findViewById(R.id.iv_col_1)
            bnrTitle = itemView.findViewById(R.id.text_title_banner)
            bnrSubTitle = itemView.findViewById(R.id.text_sub_title_banner)
            titleBottom = itemView.findViewById(R.id.text_title_bottom)
            subTitleBottom = itemView.findViewById(R.id.text_sub_title_bottom)
        }

        fun bindData(item:ImageList?){
            item?.let {
                if(URLUtil.isValidUrl(it.imageURLMobile))
                    ImageHandler.loadImageFitCenter(context,ivCol,it.imageURLMobile)

                bnrTitle?.text = it.inBannerTitle
                bnrSubTitle?.text = it.inBannerSubTitle

                itemView.setOnClickListener { _ ->
                   handledClick(it.redirectAppLink,it.redirectURL)
                }

                if(TextUtils.isEmpty(it.title)){
                    titleBottom?.visibility = View.GONE
                }
                else{
                    titleBottom?.visibility = View.VISIBLE
                    titleBottom?.text = it.title
                }

                if (TextUtils.isEmpty(it.subTitle)) {
                    subTitleBottom?.visibility = View.GONE
                } else {
                    subTitleBottom?.visibility = View.VISIBLE
                    subTitleBottom?.text = it.subTitle
                }
            }
        }
    }

    fun handledClick(appLink: String?, webLink: String?) {
        if (TextUtils.isEmpty(appLink)) {
            RouteManager.route(context, String.format(Locale.getDefault(),"%s?url=%s", ApplinkConst.WEBVIEW, Uri.encode(webLink)))
        } else {
            RouteManager.route(context, appLink)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = when(mType){
            CommonConstant.BannerType.BANNER_3_1 -> inflater.inflate(R.layout.tp_item_carousel_3_1,parent,false)
            CommonConstant.BannerType.CAROUSEL_2_1 -> inflater.inflate(R.layout.tp_item_carousel_2_1, parent, false)
            else -> inflater.inflate(R.layout.tp_item_carousel_1_1, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mItems?.let {
            holder.bindData(it[position])
        }
    }

    override fun getItemCount() = mItems?.size ?: 0

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        mItems?.let{
            try{
                val data = it[holder.adapterPosition]

                if(!holder.isVisited){
                    AnalyticsTrackerUtil.sendEvent(
                        holder.itemView.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_DYNAMIC_ICON,
                        data.title
                    )
                    holder.isVisited = true
                }
            }
            catch (err:Exception){}
        }
    }

}
