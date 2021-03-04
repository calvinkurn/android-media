package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceTagAttribute
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.BalanceTextAttribute
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.helper.isHexColor
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_balance_widget.view.*

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
            itemView.home_progress_bar_balance_layout.gone()
            itemView.home_container_action_balance.gone()
            when (element?.state) {
                BalanceDrawerItemModel.STATE_LOADING -> itemView.home_progress_bar_balance_layout.show()
                BalanceDrawerItemModel.STATE_SUCCESS -> {
                    itemView.home_container_action_balance.show()
                    renderBalanceText(element?.balanceTitleTextAttribute, element?.balanceTitleTagAttribute, itemView.home_tv_balance)
                    renderBalanceText(element?.balanceSubTitleTextAttribute, element?.balanceSubTitleTagAttribute, itemView.home_tv_btn_action_balance)
                }
            }
            element?.defaultIconRes?.let {
                itemView.home_iv_logo_balance.setImageDrawable(itemView.context.getDrawable(it))
            }
            element?.iconImageUrl?.let {
                itemView.home_iv_logo_balance.loadImage(it)
            }
        }

        private fun renderBalanceText(textAttr: BalanceTextAttribute?, tagAttr: BalanceTagAttribute?, textView: TextView, textSize: Int = R.dimen.sp_12) {
            textView.background = null
            textView.text = null
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(textSize))
            if (tagAttr != null && tagAttr.text.isNotEmpty()) {
                renderTagAttribute(tagAttr, textView)
            } else if (textAttr != null && textAttr.text.isNotEmpty()) {
                renderTextAttribute(textAttr, textView)
            }
        }

        private fun renderTagAttribute(tagAttr: BalanceTagAttribute, textView: TextView) {
            if (tagAttr.backgroundColour.isNotEmpty() && tagAttr.backgroundColour.isHexColor()) {
                val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_tokopoints_rounded)
                (drawable as GradientDrawable?)?.let {
                    it.setColorFilter(Color.parseColor(tagAttr.backgroundColour), PorterDuff.Mode.SRC_ATOP)
                    textView.background = it
                    val horizontalPadding = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_2)
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.sp_8))
                    textView.setTypeface(null, Typeface.NORMAL)
                    textView.setPadding(horizontalPadding, 0, horizontalPadding, 0)
                }
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_N0))
            } else {
                textView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            }
            if (tagAttr.text.isNotEmpty()) {
                textView.text = tagAttr.text
            }
        }

        private fun renderTextAttribute(textAttr: BalanceTextAttribute, textView: TextView) {
            if (textAttr.colour.isNotEmpty() && textAttr.colour.isHexColor()) {
                textView.setTextColor(Color.parseColor(textAttr.colour).invertIfDarkMode(itemView.context))
            } else {
                textView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            }
            if (textAttr.isBold) {
                textView.setTypeface(null, Typeface.BOLD)
            } else {
                textView.setTypeface(null, Typeface.NORMAL)
            }
            if (textAttr.text.isNotEmpty()) {
                textView.text = textAttr.text
            }
        }
    }
}