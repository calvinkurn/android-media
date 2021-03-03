package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.OvoWidgetTracking
import com.tokopedia.home.beranda.data.model.SectionContentItem
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceTagAttribute
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceTextAttribute
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeBalanceModel
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by yfsx on 3/1/21.
 */

class BalanceAdapter(val listener: HomeCategoryListener?): RecyclerView.Adapter<BalanceAdapter.Holder>() {

    companion object {
        private const val TITLE_HEADER_WEBSITE = "Tokopedia"
        private const val TITLE = "OVO"
        private const val WALLET_TYPE = "OVO"
        private const val BEBAS_ONGKIR_TYPE = "bebas ongkir"
        private const val KUPON_SAYA_URL_PATH = "kupon-saya"
        private const val CDN_URL = "https://ecs7.tokopedia.net/img/android/"
        private const val BG_CONTAINER_URL = CDN_URL + "bg_product_fintech_tokopoint_normal/" +
                "drawable-xhdpi/bg_product_fintech_tokopoint_normal.png"
    }

    private var itemMap: HomeBalanceModel = HomeBalanceModel()

    fun setItemMap(itemMap: HomeBalanceModel) {
        this.itemMap = HomeBalanceModel()
        this.itemMap = itemMap
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_balance_widget, parent, false))
    }

    override fun getItemCount(): Int {
        return itemMap.balanceDrawerItemModels.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemMap.balanceDrawerItemModels[position], listener)
    }

    class Holder(v: View): RecyclerView.ViewHolder(v) {
        private var listener: HomeCategoryListener? = null
        fun bind(drawerItem: BalanceDrawerItemModel?, listener: HomeCategoryListener?) {
            this.listener = listener
            renderTokoPoint(drawerItem)
        }

        private fun renderTokoPoint(element: BalanceDrawerItemModel?) {
            val tokoPointHolder = itemView.findViewById<View>(R.id.container_tokopoint)
            val tvBalanceTokoPoint = itemView.findViewById<TextView>(R.id.tv_balance_tokopoint)
            val tvActionTokopoint = itemView.findViewById<TextView>(R.id.tv_btn_action_tokopoint)
            val ivLogoTokoPoint = itemView.findViewById<ImageView>(R.id.iv_logo_tokopoint)
            val tokopointProgressBarLayout = itemView.findViewById<View>(R.id.progress_bar_tokopoint_layout)
            val tokopointActionContainer = itemView.findViewById<View>(R.id.container_action_tokopoint)
            val mTextCouponCount = itemView.findViewById<TextView>(R.id.text_coupon_count)
            tokopointProgressBarLayout.gone()
            tokopointActionContainer.gone()
            when (element?.state) {
                BalanceDrawerItemModel.STATE_LOADING -> tokopointProgressBarLayout.show()
                BalanceDrawerItemModel.STATE_SUCCESS -> {
                    tokopointActionContainer.show()
                    renderBalanceText(element?.balanceTitleTextAttribute, element?.balanceTitleTagAttribute, tvBalanceTokoPoint)
                    renderBalanceText(element?.balanceSubTitleTextAttribute, element?.balanceSubTitleTagAttribute, tvActionTokopoint)
                }
            }


        }

        private fun renderBalanceText(textAttr: BalanceTextAttribute?, tagAttr: BalanceTagAttribute?, textView: TextView, textSize: Int = R.dimen.sp_12) {
            textView.background = null
            textView.text = null
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(textSize))
            if (tagAttr != null && !TextUtils.isEmpty(tagAttr.text)) {
                if (!TextUtils.isEmpty(tagAttr.backgroundColour) && HexValidator.validate(tagAttr.backgroundColour)) {
                    val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_tokopoints_rounded)
                    if (drawable is GradientDrawable) {
                        val shapeDrawable = drawable as GradientDrawable?
                        shapeDrawable!!.setColorFilter(Color.parseColor(tagAttr.backgroundColour), PorterDuff.Mode.SRC_ATOP)
                        textView.background = shapeDrawable
                        val horizontalPadding = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_2)
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.sp_8))
                        textView.setTypeface(null, Typeface.NORMAL)
                        textView.setPadding(horizontalPadding, 0, horizontalPadding, 0)
                    }
                    textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_N0))
                } else {
                    textView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                }
                if (!TextUtils.isEmpty(tagAttr.text)) {
                    textView.text = tagAttr.text
                }
            } else if (textAttr != null && !TextUtils.isEmpty(textAttr.text)) {
                if (!TextUtils.isEmpty(textAttr.colour) && HexValidator.validate(textAttr.colour)) {
                    textView.setTextColor(Color.parseColor(textAttr.colour).invertIfDarkMode(itemView.context))
                } else {
                    textView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                }
                if (textAttr.isBold) {
                    textView.setTypeface(null, Typeface.BOLD)
                } else {
                    textView.setTypeface(null, Typeface.NORMAL)
                }
                if (!TextUtils.isEmpty(textAttr.text)) {
                    textView.text = textAttr.text
                }
            }
        }
    }
}