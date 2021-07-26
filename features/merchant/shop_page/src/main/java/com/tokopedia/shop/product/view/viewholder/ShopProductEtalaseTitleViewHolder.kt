package com.tokopedia.shop.product.view.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseTitleUiModel

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopProductEtalaseTitleViewHolder(itemView: View) : AbstractViewHolder<ShopProductEtalaseTitleUiModel>(itemView) {
    private var textView: TextView? = null
    private var ivBadge: ImageView? = null

    init {
        findViews(itemView)
    }

    override fun bind(shopProductFeaturedUiModel: ShopProductEtalaseTitleUiModel) {
        textView!!.text = MethodChecker.fromHtml(shopProductFeaturedUiModel.etalaseName)
        if (!TextUtils.isEmpty(shopProductFeaturedUiModel.etalaseBadge)) {
            ivBadge?.loadIcon(shopProductFeaturedUiModel.etalaseBadge)
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
        val LAYOUT = R.layout.item_new_shop_product_etalase_title_view
    }

}
