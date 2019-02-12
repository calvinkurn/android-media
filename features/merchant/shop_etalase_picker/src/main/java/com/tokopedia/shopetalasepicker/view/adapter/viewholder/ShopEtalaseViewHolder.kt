package com.tokopedia.shopetalasepicker.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shopetalasepicker.R
import com.tokopedia.shopetalasepicker.view.model.ShopEtalaseViewModel

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopEtalaseViewHolder(itemView: View) : AbstractViewHolder<ShopEtalaseViewModel>(itemView) {
    private val etalasePickerItemName: TextView
    private val etalasePickerRadioButton: ImageView
    private val etalaseBadgeImageView: ImageView
    private val tvCount: TextView

    init {
        etalasePickerItemName = itemView.findViewById(R.id.text_view_name)
        etalasePickerRadioButton = itemView.findViewById(R.id.image_view_etalase_checked)
        etalaseBadgeImageView = itemView.findViewById(R.id.image_view_etalase_badge)
        tvCount = itemView.findViewById(R.id.tv_count)
    }

    override fun bind(shopEtalaseViewModel: ShopEtalaseViewModel) {
        if (!TextUtils.isEmpty(shopEtalaseViewModel.etalaseBadge)) {
            ImageHandler.LoadImage(etalaseBadgeImageView, shopEtalaseViewModel.etalaseBadge)
            etalaseBadgeImageView.visibility = View.VISIBLE
        } else {
            etalaseBadgeImageView.visibility = View.GONE
        }

        etalasePickerItemName.text = shopEtalaseViewModel.etalaseName

        if (shopEtalaseViewModel.isSelected) {
            etalasePickerRadioButton.visibility = View.VISIBLE
        } else {
            etalasePickerRadioButton.visibility = View.GONE
        }

        if (shopEtalaseViewModel.etalaseCount > 0) {
            tvCount.text = getString(R.string.x_product,
                    shopEtalaseViewModel.etalaseCount.toString())
            tvCount.visibility = View.VISIBLE
        } else {
            tvCount.visibility = View.GONE
        }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_shop_etalase
    }
}
