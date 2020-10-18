package com.tokopedia.topads.sdk.view.adapter

import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener
import com.tokopedia.topads.sdk.utils.ImageLoader
import com.tokopedia.topads.sdk.view.adapter.DynamicFeedShopAdapter.DynamicFeedShopViewHolder
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

/**
 * @author by milhamj on 07/01/19.
 */
class DynamicFeedShopAdapter(private val itemClickListener: LocalAdsClickListener) : RecyclerView.Adapter<DynamicFeedShopViewHolder>() {

    private var itemImpressionListener: TopAdsShopImpressionListener? = null
    private val list: MutableList<Data> = ArrayList()

    fun setItemImpressionListener(itemImpressionListener: TopAdsShopImpressionListener?) {
        this.itemImpressionListener = itemImpressionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicFeedShopViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_dynamic_feed_shop, parent, false)
        return DynamicFeedShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: DynamicFeedShopViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: DynamicFeedShopViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    inner class DynamicFeedShopViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImageLeft: ImageView = itemView.findViewById(R.id.ivImageLeft)
        private val ivImageMiddle: ImageView = itemView.findViewById(R.id.ivImageMiddle)
        private val ivImageRight: ImageView = itemView.findViewById(R.id.ivImageRight)
        private val ivBadge: ImageView = itemView.findViewById(R.id.ivBadge)
        private val ivProfile: ImageView = itemView.findViewById(R.id.ivProfile)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val btnFollow: UnifyButton = itemView.findViewById(R.id.btnFollow)
        private val imageLoader: ImageLoader = ImageLoader(itemView.context)

        internal fun bind(data: Data?) {
            data?.let {
                initView(it)
                initListener(it)
            }
        }

        internal fun onViewRecycled() {
            ImageLoader.clearImage(ivImageLeft)
            ImageLoader.clearImage(ivImageMiddle)
            ImageLoader.clearImage(ivImageRight)
            ImageLoader.clearImage(ivProfile)
        }

        private fun initView(data: Data) {
            data.shop?.let { shop ->
                if (shop.imageProduct != null) {
                    val imageProductList = data.shop.imageProduct
                    if (imageProductList.size > 0) {
                        loadImageOrDefault(ivImageLeft, imageProductList[0].imageUrl)
                    }
                    if (imageProductList.size > 1) {
                        loadImageOrDefault(ivImageMiddle, imageProductList[1].imageUrl)
                    }
                    if (imageProductList.size > 2) {
                        loadImageOrDefault(ivImageRight, imageProductList[2].imageUrl)
                    }
                    shop.isLoaded = true
                }
                ivProfile.addOnImpressionListener(shop.imageShop, object : ViewHintListener {
                    override fun onViewHint() {
                        itemImpressionListener?.onImpressionShopAds(shop.imageShop.getsUrl(), shop.id, shop.name, shop.imageShop.xsEcs)
                    }
                })
                imageLoader.loadCircle(shop, ivProfile)
                tvName.text = fromHtml(shop.name)
                tvDescription.text = fromHtml(shop.tagline)
                bindBadge(shop)
            }
            bindFavorite(data)
        }

        private fun initListener(data: Data) {
            itemView.setOnClickListener { itemClickListener.onShopItemClicked(adapterPosition, data) }
            btnFollow.setOnClickListener {
                if (!data.isFavorit) {
                    btnFollow.buttonVariant = UnifyButton.Variant.GHOST
                    btnFollow.buttonType = UnifyButton.Type.ALTERNATE
                    btnFollow.text = btnFollow.context.getString(R.string.topads_followed)
                }
                itemClickListener.onAddFavorite(adapterPosition, data)
            }
        }

        private fun loadImageOrDefault(imageView: ImageView, imageUrl: String) {
            if (!TextUtils.isEmpty(imageUrl)) {
                imageLoader.loadImage(imageUrl, imageView)
            } else {
                imageView.setBackgroundColor(
                        ContextCompat.getColor(imageView.context, com.tokopedia.unifyprinciples.R.color.Unify_N50)
                )
            }
        }

        private fun fromHtml(string: String?): Spanned {
            val str = string ?: ""
            return MethodChecker.fromHtml(str)
        }

        private fun bindFavorite(data: Data) {
            if (data.isFavorit) {
                btnFollow.buttonVariant = UnifyButton.Variant.GHOST
                btnFollow.buttonType = UnifyButton.Type.ALTERNATE
                btnFollow.text = btnFollow.context.getString(R.string.topads_followed)
            } else {
                btnFollow.buttonVariant = UnifyButton.Variant.FILLED
                btnFollow.buttonType = UnifyButton.Type.MAIN
                btnFollow.text = btnFollow.context.getString(R.string.topads_follow)
            }
        }

        private fun bindBadge(shop: Shop) {
            val layoutParams = tvName.layoutParams as ViewGroup.MarginLayoutParams
            when {
                shop.isShop_is_official -> {
                    ivBadge.show()
                    ivBadge.setImageDrawable(
                            ImageLoader.getDrawable(ivBadge.context, R.drawable.ic_badge_shop_official)
                    )
                    layoutParams.leftMargin = ivBadge.context.resources.getDimension(R.dimen.dp_4).toInt()
                }
                shop.isGoldShopBadge -> {
                    ivBadge.show()
                    ivBadge.setImageDrawable(
                            ImageLoader.getDrawable(
                                    ivBadge.context,
                                    R.drawable.ic_power_merchant
                            )
                    )
                    layoutParams.leftMargin = ivBadge.context.resources.getDimension(R.dimen.dp_4).toInt()
                }
                else -> {
                    ivBadge.hide()
                    layoutParams.leftMargin = ivBadge.context.resources.getDimension(R.dimen.dp_0).toInt()
                }
            }
        }

    }

    interface TopAdsShopImpressionListener {
        fun onImpressionShopAds(url: String, shopId: String, shopName: String, imageUrl: String)
    }

}