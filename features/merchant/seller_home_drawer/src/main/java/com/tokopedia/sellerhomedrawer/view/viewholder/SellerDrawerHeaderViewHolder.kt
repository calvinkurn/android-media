package com.tokopedia.sellerhomedrawer.view.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.view.listener.SellerDrawerHeaderListener
import com.tokopedia.sellerhomedrawer.view.viewmodel.sellerheader.SellerDrawerHeader
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.*
import kotlinx.android.synthetic.main.sh_drawer_saldo.view.*

class SellerDrawerHeaderViewHolder(itemView: View,
                                   private val sellerDrawerHeaderListener: SellerDrawerHeaderListener,
                                   val context: Context)
    : AbstractViewHolder<SellerDrawerHeader>(itemView){

    companion object {
        val LAYOUT_RES = R.layout.sh_seller_drawer_header
    }

    private var oldUserAvatar = ""

    override fun bind(sellerDrawerHeader: SellerDrawerHeader?) {
        if (sellerDrawerHeader != null) {
            with(itemView) {
                drawer_points_layout.visibility = View.VISIBLE
                name_text.visibility = View.VISIBLE
                user_avatar.visibility = View.VISIBLE

                val sellerUserAvatar = sellerDrawerHeader.sellerDrawerProfile.userAvatar
                val isAvatarExistAndNotOld = oldUserAvatar != sellerUserAvatar
                if (isAvatarExistAndNotOld) {
                    loadAvatarImage(sellerUserAvatar)
                    oldUserAvatar = sellerDrawerHeader.sellerDrawerProfile.userAvatar
                }
                name_text.text = sellerDrawerHeader.sellerDrawerProfile.userName
                setDeposit(sellerDrawerHeader)
                setListener()
            }
        }
    }

    private fun loadAvatarImage(userAvatar: String) {
        ImageHandler.loadImage(context,
                itemView.user_avatar,
                userAvatar,
                R.drawable.ic_image_avatar_boy,
                R.drawable.ic_image_avatar_boy)
        oldUserAvatar = userAvatar
    }

    private fun setListener() {
        with(itemView) {
            drawer_saldo.setOnClickListener { sellerDrawerHeaderListener.onGoToDeposit() }
            name_text.setOnClickListener { sellerDrawerHeaderListener.onGoToProfile() }
            user_avatar.setOnClickListener { sellerDrawerHeaderListener.onGoToProfile() }
            complete_profile.setOnClickListener { sellerDrawerHeaderListener.onGoToProfileCompletion() }
        }
    }

    private fun setDeposit(sellerDrawerHeader: SellerDrawerHeader) {
        with(itemView) {
            val deposit = sellerDrawerHeader.sellerDrawerDeposit?.deposit
            if (deposit != null) {
                if (deposit.isEmpty()) {
                    loading_saldo.visibility = View.VISIBLE
                    deposit_text.visibility = View.GONE
                } else {
                    loading_saldo.visibility = View.GONE
                    deposit_text.apply {
                        text = deposit
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}