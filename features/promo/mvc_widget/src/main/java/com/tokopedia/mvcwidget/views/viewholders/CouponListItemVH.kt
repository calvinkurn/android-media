package com.tokopedia.mvcwidget.views.viewholders

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvcwidget.MvcCouponListItem
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcDetailViewContract
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CouponListItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rvImage: RecyclerView = itemView.findViewById(R.id.rvImage)
    val tv1: Typography = itemView.findViewById(R.id.tv1)
    val tv2: Typography = itemView.findViewById(R.id.tv2)
    val tv3: Typography = itemView.findViewById(R.id.tv3)
    val tv4: Typography = itemView.findViewById(R.id.tv4)
    val tv_info: Typography = itemView.findViewById(R.id.tv_info)
    val rel_cta: RelativeLayout = itemView.findViewById(R.id.rel_cta)
    val rel_info: RelativeLayout = itemView.findViewById(R.id.rel_info)
    val iv_check: ImageUnify = itemView.findViewById(R.id.iv_check)
    val divider: View = itemView.findViewById(R.id.divider)
    private val REDIRECT_CHECK = "redirect"
    private val INFO_CHECK = "info"

    init {
        rvImage.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setData(data: MvcCouponListItem, contract: MvcDetailViewContract) {
        tv1.text = data.title1
        tv2.text = data.title2
        tv3.text = data.title3
        if (data.ctaCatalog.type.isNullOrEmpty().not()) {
            when (data.ctaCatalog.type) {
                REDIRECT_CHECK -> {
                    if (contract.getMvcSource() == MvcSource.PDP) {
                        iv_check.visibility = View.VISIBLE
                    } else {
                        iv_check.visibility = View.GONE
                    }
                    rel_cta.show()
                    if (data.ctaCatalog.text.isNullOrEmpty().not()) {
                        contract.getMvcTracker()?.viewMVCLockToProduct(
                            shopId = contract.getShopId(),
                            userId = UserSession(itemView.context).userId,
                            source = contract.getMvcSource(),
                            productId = contract.getProductId()
                        )
                        tv4.text = data.ctaCatalog.text
                    } else {
                        rel_cta.hide()
                    }
                    if (data.ctaCatalog.appLink.isNullOrEmpty().not()) {
                        rel_cta.setOnClickListener {
                            contract.getMvcTracker()?.userClickEntryPointOnMVCLockToProduct(
                                shopId = contract.getShopId(),
                                userId = UserSession(itemView.context).userId,
                                source = contract.getMvcSource(),
                                productId = contract.getProductId()
                            )
                            RouteManager.route(itemView.context, data.ctaCatalog.appLink)
                        }
                    } else {
                        rel_cta.hide()
                    }
                }
                INFO_CHECK -> {
                    rel_info.show()
                    if (data.ctaCatalog.text.isNullOrEmpty().not()) {
                        tv_info.text = data.ctaCatalog.text
                    } else {
                        rel_info.hide()
                    }
                }
            }
        }

        toggleView(data.title2, tv2)
        toggleView(data.title3, tv3)

        if (!data.urlList.isNullOrEmpty()) {
            rvImage.adapter = ImageAdapter(data.urlList)
            (rvImage.adapter as ImageAdapter).notifyDataSetChanged()
        }
    }

    private fun toggleView(text: String?, view: View) {
        if (text.isNullOrEmpty()) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    private fun toggleView(text: SpannableString?, view: View) {
        if (text.isNullOrEmpty()) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    class ImageAdapter(val urlList: List<String?>) : RecyclerView.Adapter<ListItemImageVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemImageVH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.mvc_list_item_images, parent, false)
            return ListItemImageVH(v)
        }

        override fun getItemCount() = urlList.size

        override fun onBindViewHolder(holder: ListItemImageVH, position: Int) {
            if (!urlList[position].isNullOrEmpty()) {
                holder.image.loadImage(urlList[position]) {
                    isAnimate(false)
                }
            }
        }
    }

    class ListItemImageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: AppCompatImageView = itemView.findViewById(R.id.image)
    }
}
