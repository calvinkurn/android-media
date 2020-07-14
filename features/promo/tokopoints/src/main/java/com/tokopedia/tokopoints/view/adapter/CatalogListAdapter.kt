package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.catalogdetail.CouponCatalogDetailsActivity.Companion.getCatalogDetail
import com.tokopedia.tokopoints.view.cataloglisting.CatalogPurchaseRedemptionPresenter
import com.tokopedia.tokopoints.view.model.CatalogListingOuter
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.ImageUtil
import rx.Subscriber
import java.util.*

class CatalogListAdapter(private val mPresenter: CatalogPurchaseRedemptionPresenter, private val mContext: Context?, callback: AdapterCallback?, private val categoryId: Int, private val subCategoryId: Int, private val pointsRange: Int, private val mIsLimitEnable: Boolean) : BaseAdapter<CatalogsValueEntity?>(callback) {

    inner class ViewHolder(view: View) : BaseVH(view) {
        var quota: TextView
        var description: TextView
        var pointValue: TextView
        var timeLabel: TextView
        var timeValue: TextView
        var disabledError: TextView
        var btnContinue: TextView
        var labelPoint: TextView
        var textDiscount: TextView
        var imgBanner: ImageView
        var imgTime: ImageView
        var pbQuota: ProgressBar
        var isVisited = false
        override fun bindView(item: CatalogsValueEntity?, position: Int) {
            setData(this, item, position)
        }

        init {
            quota = view.findViewById(R.id.text_quota_count)
            description = view.findViewById(R.id.text_description)
            pointValue = view.findViewById(R.id.text_point_value)
            timeLabel = view.findViewById(R.id.text_time_label)
            timeValue = view.findViewById(R.id.text_time_value)
            disabledError = view.findViewById(R.id.text_disabled_error)
            btnContinue = view.findViewById(R.id.button_continue)
            imgBanner = view.findViewById(R.id.img_banner)
            imgTime = view.findViewById(R.id.img_time)
            labelPoint = view.findViewById(R.id.text_point_label)
            textDiscount = view.findViewById(R.id.text_point_discount)
            pbQuota = view.findViewById(R.id.progress_timer_quota)
        }
    }

    private fun setData(holder: ViewHolder, item: CatalogsValueEntity?, position: Int) {
        if (item == null) return
        holder.btnContinue.isEnabled = !item.isDisabledButton
        holder.description.text = item.title
        holder.btnContinue.setText(R.string.tp_label_exchange) //TODO asked for server driven value
        ImageHandler.loadImageFitCenter(holder.imgBanner.context, holder.imgBanner, item.thumbnailUrlMobile)
        //setting points info if exist in response
        if (item.pointsStr == null || item.pointsStr.isEmpty()) {
            holder.pointValue.visibility = View.GONE
        } else {
            holder.pointValue.visibility = View.VISIBLE
            holder.pointValue.text = item.pointsStr
        }
        //setting expiry time info if exist in response
        if (item.expiredLabel == null || item.expiredLabel.isEmpty()) {
            holder.timeLabel.visibility = View.GONE
            holder.timeValue.visibility = View.GONE
            holder.imgTime.visibility = View.GONE
        } else {
            holder.timeLabel.visibility = View.VISIBLE
            holder.timeValue.visibility = View.VISIBLE
            holder.imgTime.visibility = View.VISIBLE
            holder.timeLabel.text = item.expiredLabel
            holder.timeValue.text = item.expiredStr
        }
        //Quota text handling
        if (item.upperTextDesc == null || item.upperTextDesc.isEmpty()) {
            holder.quota.visibility = View.GONE
            holder.pbQuota.visibility = View.GONE
        } else {
            holder.quota.visibility = View.VISIBLE
            holder.pbQuota.visibility = View.VISIBLE
            holder.pbQuota.progress = 0
            val upperText = StringBuilder()
            if (item.catalogType == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.context, com.tokopedia.design.R.color.red_150))
            } else {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.context, com.tokopedia.design.R.color.black_38))
            }
            for (i in item.upperTextDesc.indices) {
                if (i == 1) {
                    if (item.catalogType == CommonConstant.CATALOG_TYPE_FLASH_SALE) { //for flash sale progress bar handling
                        holder.pbQuota.progress = item.quota
                        upperText.append(item.upperTextDesc[i])
                    } else { //exclusive case for handling font color of second index.
                        upperText.append("<font color='#ff5722'>" + item.upperTextDesc[i] + "</font>")
                    }
                } else {
                    upperText.append(item.upperTextDesc[i]).append(" ")
                }
            }
            holder.quota.text = MethodChecker.fromHtml(upperText.toString())
        }
        //Quota text handling
        if (item.disableErrorMessage == null || item.disableErrorMessage.isEmpty()) {
            holder.disabledError.visibility = View.GONE
        } else {
            holder.disabledError.visibility = View.VISIBLE
            holder.disabledError.text = item.disableErrorMessage
        }
        //disabling the coupons if not eligible for current membership
        if (item.isDisabled) {
            ImageUtil.dimImage(holder.imgBanner)
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.context, com.tokopedia.tokopoints.R.color.clr_31353b))
        } else {
            ImageUtil.unDimImage(holder.imgBanner)
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.context, com.tokopedia.tokopoints.R.color.clr_31353b))
        }
        if (item.isDisabledButton) {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.context, com.tokopedia.abstraction.R.color.black_12))
        } else {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.context, com.tokopedia.design.R.color.white))
        }

        if (item.pointsSlash <= 0) {
            holder.labelPoint.visibility = View.GONE
        } else {
            holder.labelPoint.visibility = View.VISIBLE
            holder.labelPoint.text = item.pointsSlashStr
            holder.labelPoint.paintFlags = holder.labelPoint.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        if (item.discountPercentage <= 0) {
            holder.textDiscount.visibility = View.GONE
        } else {
            holder.textDiscount.visibility = View.VISIBLE
            holder.textDiscount.text = item.discountPercentageStr
        }

        holder.btnContinue.setOnClickListener { v: View? ->
            //call validate api the show dialog
            mPresenter.startValidateCoupon(item)
            AnalyticsTrackerUtil.sendEvent(holder.btnContinue.context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                    item.baseCode)
        }
        holder.imgBanner.setOnClickListener { v: View? ->
            val bundle = Bundle()
            bundle.putString(CommonConstant.EXTRA_CATALOG_CODE, item.slug)
            holder.imgBanner.context.startActivity(getCatalogDetail(holder.imgBanner.context, bundle), bundle)
            sendClickEvent(holder.imgBanner.context, item, position)
        }
        holder.btnContinue.visibility = if (item.isShowTukarButton) View.VISIBLE else View.GONE
    }

    override fun getItemViewHolder(parent: ViewGroup, inflater: LayoutInflater, viewType: Int): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.tp_item_coupon, parent, false)
        return ViewHolder(itemView)
    }

    override fun loadData(currentPageIndex: Int) {
        if (mIsLimitEnable) {
            return
        }
        super.loadData(currentPageIndex)
        val mGetHomePageData = GraphqlUseCase()
        mGetHomePageData.clearRequest()
        //Adding request for main query
        val variablesMain: MutableMap<String, Any> = HashMap()
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE] = currentPageIndex
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE_SIZE] = CommonConstant.PAGE_SIZE
        //Default page sort id
        variablesMain[CommonConstant.GraphqlVariableKeys.SORT_ID] = sortId
        variablesMain[CommonConstant.GraphqlVariableKeys.CATEGORY_ID] = categoryId
        variablesMain[CommonConstant.GraphqlVariableKeys.SUB_CATEGORY_ID] = subCategoryId
        //Point range will be zero for all catalog
        variablesMain[CommonConstant.GraphqlVariableKeys.POINTS_RANGE] = pointsRange
        val graphqlRequestMain = GraphqlRequest(GraphqlHelper.loadRawString(mContext?.resources, R.raw.tp_gql_catalog_listing),
                CatalogListingOuter::class.java,
                variablesMain, false)
        mGetHomePageData.addRequest(graphqlRequestMain)
        mGetHomePageData.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                loadCompletedWithError()
            }

            override fun onNext(graphqlResponse: GraphqlResponse) { //handling the catalog listing and tabs
                val catalogListingOuter = graphqlResponse.getData<CatalogListingOuter>(CatalogListingOuter::class.java)
                if (catalogListingOuter != null) {
                    loadCompleted(catalogListingOuter.catalog.catalogs, catalogListingOuter)
                    isLastPage = !catalogListingOuter.catalog.paging.isHasNext
                } else {
                    loadCompletedWithError()
                }
            }
        })
    }

    private val sortId: Int
        private get() = 1

    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)
        if (vh is ViewHolder) {
            val holder = vh
            val data = items[vh.getAdapterPosition()] ?: return
            if (!holder.isVisited) {
                val item: MutableMap<String, String> = HashMap()
                item["id"] = data.id.toString()
                item["name"] = data.title
                item["position"] = holder.adapterPosition.toString()
                item["creative"] = data.title
                item["creative_url"] = data.imageUrlMobile
                item["promo_code"] = data.baseCode
                val promotions: MutableMap<String, List<Map<String, String>>> = HashMap()
                promotions["promotions"] = Arrays.asList<Map<String, String>>(item)
                val promoView: MutableMap<String, Map<String, List<Map<String, String>>>> = HashMap()
                promoView["promoView"] = promotions
                AnalyticsTrackerUtil.sendECommerceEvent(holder.btnContinue.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
                        data.title, promoView)
                holder.isVisited = true
            }
        }
    }

    private fun sendClickEvent(context: Context, data: CatalogsValueEntity, position: Int) {
        val item: MutableMap<String, String> = HashMap()
        item["id"] = data.id.toString()
        item["name"] = data.title
        item["position"] = position.toString()
        item["creative"] = data.title
        item["creative_url"] = data.imageUrlMobile
        item["promo_code"] = data.baseCode
        val promotions: MutableMap<String, List<Map<String, String>>> = HashMap()
        promotions["promotions"] = Arrays.asList<Map<String, String>>(item)
        val promoClick: MutableMap<String, Map<String, List<Map<String, String>>>> = HashMap()
        promoClick["promoClick"] = promotions
        AnalyticsTrackerUtil.sendECommerceEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
                data.title, promoClick)
    }

}