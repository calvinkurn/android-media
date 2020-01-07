package com.tokopedia.shop.newproduct.view.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductEtalaseTitleViewModel

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopProductEtalaseTitleViewHolder(itemView: View) : AbstractViewHolder<ShopProductEtalaseTitleViewModel>(itemView) {
    private var textView: TextView? = null
    private var ivBadge: ImageView? = null

    init {
        findViews(itemView)
    }

    override fun bind(shopProductFeaturedViewModel: ShopProductEtalaseTitleViewModel) {
        textView!!.text = MethodChecker.fromHtml(shopProductFeaturedViewModel.etalaseName)
        if (!TextUtils.isEmpty(shopProductFeaturedViewModel.etalaseBadge)) {
            ImageHandler.LoadImage(ivBadge!!, shopProductFeaturedViewModel.etalaseBadge)
            ivBadge!!.visibility = View.VISIBLE
        } else {
            ivBadge!!.visibility = View.GONE
        }
    }

    private fun findViews(view: View) {
        textView = view.findViewById(R.id.text)
        ivBadge = view.findViewById(R.id.image_view_etalase_badge)
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_etalase_title_view
    }

}
