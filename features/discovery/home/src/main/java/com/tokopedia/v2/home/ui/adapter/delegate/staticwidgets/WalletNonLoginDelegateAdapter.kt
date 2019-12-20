package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.util.ViewUtils
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.vo.WalletNonLoginDataModel
import com.tokopedia.v2.home.ui.ext.setSafeOnClickListener
import kotlinx.android.synthetic.main.layout_wallet_non_login.view.*

class WalletNonLoginDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return WalletNonLoginViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType) {
        holder as WalletNonLoginViewHolder
        holder.bind()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>) {
        if(payload.isNotEmpty() && holder is WalletNonLoginViewHolder){
            holder.bind()
        }
    }

    override fun isForViewType(item: ModelViewType): Boolean {
        return item is WalletNonLoginDataModel
    }

    inner class WalletNonLoginViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.layout_wallet_non_login)){
        val containerScan = itemView.container_action_scan
        val containerNonLogin = itemView.container_nonlogin
        val imageViewNonLogin = itemView.bg_container_nonlogin
        val containerRoot = itemView.container_wallet
        private val BG_CONTAINER_URL = "https://ecs7.tokopedia.net/img/android/bg_product_fintech_tokopoint_normal/drawable-xhdpi/bg_product_fintech_tokopoint_normal.png"

        fun bind() {
            containerRoot.background = ViewUtils.generateBackgroundWithShadow(containerRoot, R.color.white, R.dimen.dp_8, R.color.shadow_6, R.dimen.dp_2, Gravity.CENTER)
            val radius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8f, itemView.resources.displayMetrics)
            ImageHandler.loadImageRounded2(itemView.context, imageViewNonLogin, BG_CONTAINER_URL, radius)

            itemView.setSafeOnClickListener {

            }
            containerScan.setSafeOnClickListener {  }
        }
    }
}