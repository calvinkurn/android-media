package com.tokopedia.tokopoints.view.adapter

import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.listexchangecoupon.CatalogListItem


class PromoCheckOutExchangeCouponAdapter(items: ArrayList<CatalogListItem>,listener:ListenerCouponExchange) : RecyclerView.Adapter<PromoCheckOutExchangeCouponAdapter.ViewHolder>() {

    interface ListenerCouponExchange{
        fun onClickRedeemCoupon(position: Int,slug:String?)
    }
    var mListener:ListenerCouponExchange
    var items: ArrayList<CatalogListItem>? = null
        private set

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var quota: TextView
        internal var description: TextView
        //  internal var pointLabel: TextView
        internal var pointValue: TextView
        internal var timeLabel: TextView
        internal var timeValue: TextView
        internal var disabledError: TextView
        internal var btnContinue: TextView
        internal var labelPoint: TextView
        internal var textDiscount: TextView
        internal var imgBanner: ImageView
        internal var imgTime: ImageView
        internal var imgPoint: ImageView
        internal var pbQuota: ProgressBar
        internal var isVisited = false

        init {
            quota = view.findViewById(R.id.text_quota_count)
            description = view.findViewById(R.id.text_description)
            //  pointLabel = view.findViewById(R.id.text_my_points_label)
            pointValue = view.findViewById(R.id.text_point_value)
            timeLabel = view.findViewById(R.id.text_time_label)
            timeValue = view.findViewById(R.id.text_time_value)
            disabledError = view.findViewById(R.id.text_disabled_error)
            btnContinue = view.findViewById(R.id.button_continue)
            imgBanner = view.findViewById(R.id.img_banner)
            imgTime = view.findViewById(R.id.img_time)
            imgPoint = view.findViewById(R.id.img_points_stack)
            labelPoint = view.findViewById(R.id.text_point_label)
            textDiscount = view.findViewById(R.id.text_point_discount)
            pbQuota = view.findViewById(R.id.progress_timer_quota)
        }
    }

    init {
        this.items = items
        mListener=listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_exchange_coupon, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items!![position]
        holder.btnContinue.isEnabled = item.isDisabledButton!!
        holder.description.setText(item.title)
        //  holder.btnContinue.setText(R.string.tp_label_exchange) //TODO asked for server driven value
        ImageHandler.loadImageFitCenter(holder.imgBanner.context, holder.imgBanner, item.thumbnailURLMobile)
        //setting points info if exist in response
        if (item.pointsStr == null || item.pointsStr.isEmpty()) {
            holder.pointValue.visibility = View.GONE
            holder.imgPoint.visibility = View.GONE
        } else {
            holder.pointValue.visibility = View.VISIBLE
            holder.imgPoint.visibility = View.VISIBLE
            holder.pointValue.setText(item.pointsStr)
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
            holder.timeLabel.setText(item.expiredLabel)
            holder.timeValue.setText(item.expiredStr)
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
//CommonConstant.CATALOG_TYPE_FLASH_SALE=3
            if (item.catalogType == 3) {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.context, R.color.red_150))
            } else {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.context, R.color.black_38))
            }

            for (i in 0 until item.upperTextDesc.size) {
                if (i == 1) {
                    if (item.catalogType == 3) {
                        //for flash sale progress bar handling
                        holder.pbQuota.progress = item.quota!!
                        upperText.append(item.upperTextDesc.get(i))
                    } else {
                        //exclusive case for handling font color of second index.
                        upperText.append("<font color='#ff5722'>" + item.upperTextDesc.get(i) + "</font>")
                    }
                } else {
                    upperText.append(item.upperTextDesc.get(i)).append(" ")
                }
            }

            holder.quota.text = MethodChecker.fromHtml(upperText.toString())
        }

        //Quota text handling
        if (item.disableErrorMessage == null || item.disableErrorMessage.isEmpty()) {
            holder.disabledError.visibility = View.GONE
        } else {
            holder.disabledError.visibility = View.VISIBLE
            holder.disabledError.setText(item.disableErrorMessage)
        }

        //disabling the coupons if not eligible for current membership
     /*   if (item.isDisabled!!) {
            ImageUtil.dimImage(holder.imgBanner)
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.context, R.color.black_54))
        } else {
            ImageUtil.unDimImage(holder.imgBanner)
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.context, R.color.orange_red))
        }*/

        if (item.isDisabledButton) {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.context, R.color.black_12))
        } else {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.context, R.color.white))
        }

        if (item.pointsSlash!! <= 0) {
            holder.labelPoint.visibility = View.GONE
        } else {
            holder.labelPoint.visibility = View.VISIBLE
            holder.labelPoint.setText(item.pointsSlashStr)
            holder.labelPoint.paintFlags = holder.labelPoint.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        if (item.discountPercentage!! <= 0) {
            holder.textDiscount.visibility = View.GONE
        } else {
            holder.textDiscount.visibility = View.VISIBLE
            holder.textDiscount.setText(item.discountPercentageStr)
        }

        holder.btnContinue.setOnClickListener { v ->


            //call validate api the show dialog
            /*    mPresenter.startValidateCoupon(item)
                AnalyticsTrackerUtil.sendEvent(holder.btnContinue.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                        item.baseCode)*/
        }


        //TODO implement click listener to open DetailFragment
           holder.imgBanner.setOnClickListener { v ->

            mListener.onClickRedeemCoupon(position,item.slug)
              /* newFragmentRedeem=NewFragmentRedeem()*/
              /* newFragmentRedeem.onClickRedeemCoupon(position)*/
              /* val fragmentManager = v.context.getSupportFragmentManager()*/

              /* beginTransaction().replace(newFragmentRedeem, "NewFragmentRedeem").addToBackStack(null).commit();*/
               /* val bundle = Bundle()
                bundle.putString("catalog_code", items!![position].slug)
               val intent=Intent(this,PromoCheckoutDetailDigitalActivity::class.java)
                /*  val fragment = PromoCheckoutDetailDigitalFragment.createInstance(promoCheckoutListModel?.code ?: "", false, promoDigitalModel, pageTracking);

               childFragmentManager.beginTransaction().add(fragment, "PromoCheckoutDetailDigitalFragment").commit()*/holder.imgBanner.context.startActivity(Intent(this,PromoCheckoutDetailDigitalActivity::class.java))*/
                //sendClickEvent(holder.imgBanner.context, item, position)

             /*  val fragment = PromoCheckoutDetailDigitalFragment.createInstance(promoCheckoutListModel?.code ?: "", false, promoDigitalModel, pageTracking);

               childFragmentManager.beginTransaction().add(fragment, "PromoCheckoutDetailDigitalFragment").commit()*/
            }

        holder.btnContinue.visibility = if (item.isShowTukarButton!!) View.VISIBLE else View.GONE
       // setUpHeight(item)
    }


    override fun getItemCount(): Int {
        return if (items == null) 0 else items!!.size
    }

    fun updateItems(items: ArrayList<CatalogListItem>) {
        this.items = items
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        /*       super.onViewAttachedToWindow(holder)

               val data = items!![holder.adapterPosition] ?: return

               if (!holder.isVisited) {
                   val item = HashMap<String, Any>()
                   item["name"] = "/tokopoints/penukaran point - p(x) - promo list"
                   item["position"] = holder.adapterPosition.toString()
                   item["creative"] = data.title            item["promo_code"] = data.getBaseCode()

                   val promotions = HashMap<String, Any>()
                   promotions["promotions"] = Arrays.asList<Map<String, Any>>(item)


                   AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                           AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                           AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON_ON_CATALOG,
                           data.getTitle() + " - " + data.getBaseCode(), promotions)

                   holder.isVisited = true
               }*/
    }

/*
    private fun sendClickEvent(context: Context, data: CatalogsValueEntity, position: Int) {
        val item = HashMap<String, Any>()
        item["name"] = "/tokopoints/penukaran point - p(x) - promo list"
        item["position"] = position.toString()
        item["creative"] = data.getTitle()
        item["promo_code"] = data.getBaseCode()

        val promotions = HashMap<String, Any>()
        promotions["promotions"] = Arrays.asList<Map<String, Any>>(item)


        AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON_ON_CATALOG,
                data.getTitle() + " - " + data.getBaseCode(), promotions)
    }
*/

/*
    private fun setUpHeight(data: CatalogListItem?) {
        if (data == null || mRecyclerView == null) {
            return
        }

        if (data!!.pointsSlash!! > 0
                && !TextUtils.isEmpty(data!!.expiredLabel)
                && !TextUtils.isEmpty(data!!.disableErrorMessage)) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.dp_280)
        } else if (data!!.pointsSlash!! > 0 && !TextUtils.isEmpty(data!!.expiredLabel)) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.dp_260)
        } else if (!TextUtils.isEmpty(data!!.expiredLabel) && !TextUtils.isEmpty(data!!.disableErrorMessage)) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.dp_260)
        } else if (data!!.pointsSlash!! > 0 && !TextUtils.isEmpty(data!!.disableErrorMessage)) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.dp_240)
        } else if (data!!.pointsSlash!! > 0) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.dp_220)
        } else {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.dp_220)
        }

        mRecyclerView.requestLayout()
    }*/
}
