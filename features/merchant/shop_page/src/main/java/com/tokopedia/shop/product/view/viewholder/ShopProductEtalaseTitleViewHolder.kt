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
import com.tokopedia.shop.databinding.ItemNewShopProductEtalaseTitleViewBinding
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopProductEtalaseTitleViewHolder(itemView: View) : AbstractViewHolder<ShopProductEtalaseTitleUiModel>(itemView) {
    private val viewBinding: ItemNewShopProductEtalaseTitleViewBinding? by viewBinding()
    private var textView: TextView? = null
    private var ivBadge: ImageView? = null

    init {
        findViews()
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

    private fun findViews() {
        textView = viewBinding?.text
        ivBadge = viewBinding?.imageViewEtalaseBadge
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_new_shop_product_etalase_title_view
    }
}
