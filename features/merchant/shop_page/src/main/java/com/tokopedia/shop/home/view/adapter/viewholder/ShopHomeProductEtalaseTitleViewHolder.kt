package com.tokopedia.shop.home.view.adapter.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopPageHomeProductEtalaseTitleViewBinding
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopHomeProductEtalaseTitleViewHolder(
    itemView: View,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomeProductEtalaseTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_page_home_product_etalase_title_view
    }
    private val viewBinding: ItemShopPageHomeProductEtalaseTitleViewBinding? by viewBinding()
    private var textView: TextView? = null
    private var ivBadge: ImageView? = null

    init {
        findViews()
    }

    override fun bind(uiModel: ShopHomeProductEtalaseTitleUiModel) {
        textView?.text = MethodChecker.fromHtml(uiModel.etalaseName)
        if (!TextUtils.isEmpty(uiModel.etalaseBadge)) {
            ivBadge?.let {
                it.loadIcon(uiModel.etalaseBadge)
            }
            ivBadge?.visibility = View.VISIBLE
        } else {
            ivBadge?.visibility = View.GONE
        }
        configColorTheme()
    }

    private fun configColorTheme() {
        if (shopHomeListener.isShopHomeTabHasFestivity()) {
            setDefaultColorConfig()
        } else {
            if (shopHomeListener.isOverrideTheme()) {
                setReimaginedColorConfig(shopHomeListener.getShopPageColorSchema())
            } else {
                setDefaultColorConfig()
            }
        }
    }

    private fun setReimaginedColorConfig(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
        )
        setHeaderColor(titleColor)
    }

    private fun setDefaultColorConfig() {
        val titleColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
        )
        setHeaderColor(titleColor)
    }

    private fun setHeaderColor(titleColor: Int) {
        textView?.setTextColor(titleColor)
    }

    private fun findViews() {
        textView = viewBinding?.text
        ivBadge = viewBinding?.imageViewEtalaseBadge
    }
}
