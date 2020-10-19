package com.tokopedia.home_account.view.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.home_account_item_profile.view.*

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ProfileViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {
    fun bind(profile: ProfileDataView) {
        with(itemView) {
            account_user_item_profile_name?.text = profile.name
            account_user_item_profile_phone?.text = profile.phone
            account_user_item_profile_email?.text = profile.email
            account_user_item_profile_edit?.setOnClickListener { listener.onEditProfileClicked() }
            loadImage(account_user_item_profile_avatar, profile.avatar)
        }
    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        ImageUtils.loadImageCircleWithPlaceHolder(imageView.context, imageView, imageUrl)
    }

//    private fun setupItemAdapter(itemView: View, brands: DealsBrandsDataView) {
//        val adapter = DealsCommonBrandAdapter(brandActionListener, DealsBrandViewHolder.LAYOUT_WIDE)
//        itemView.rv_brands?.adapter = adapter
//        itemView.rv_brands?.layoutManager = object : GridLayoutManager(itemView.context,4) {
//            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
//                lp?.width = (width / 4)
//                return true
//            }
//        }
//        adapter.brandList = brands.brands
//    }

    companion object {
        val LAYOUT = R.layout.home_account_item_profile
    }

}