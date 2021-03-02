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
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeBalanceModel
import com.tokopedia.home_component.util.invertIfDarkMode

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



        }


    }

}