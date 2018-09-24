package com.tokopedia.profile.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.design.widget.TabLayout
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.profile.R
import com.tokopedia.profile.view.adapter.PostImageAdapter
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.viewmodel.ProfilePostViewModel
import com.tokopedia.profile.view.widget.WrapContentViewPager
import kotlinx.android.synthetic.main.item_affiliate_post.view.*

/**
 * @author by milhamj on 9/20/18.
 */
class ProfilePostViewHolder(val v: View, val viewListener: ProfileContract.View) :
        AbstractViewHolder<ProfilePostViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_affiliate_post
    }

    override fun bind(element: ProfilePostViewModel) {
        ImageHandler.loadImageCircle2(itemView.context, itemView.avatar, element.avatar)
        itemView.kolBadge.visibility = if (element.isKol) View.VISIBLE else View.GONE
        itemView.name.text = element.name
        itemView.time.text = element.time

        if (!TextUtils.isEmpty(element.info)) {
            itemView.info.text = element.info
            itemView.info.visibility = View.VISIBLE
        } else {
            itemView.info.visibility = View.GONE
        }

        itemView.goToProductBtn.setOnClickListener {
            //TODO milhamj
            viewListener.goToProduct(element.productId)

        }

        if (element.isOwner) {
            itemView.addImageBtn.setOnClickListener {
                //TODO milhamj
                viewListener.addImages(element.productId)
            }
            itemView.addImageBtn.visibility = View.VISIBLE
        } else {
            itemView.addImageBtn.visibility = View.GONE
        }

        setUpViewPager(element)
    }

    private fun setUpViewPager(element: ProfilePostViewModel) {
        val adapter = PostImageAdapter()
        val viewPager = itemView.findViewById<WrapContentViewPager>(R.id.imageViewPager)
        adapter.setList(ArrayList(element.images))
        viewPager.setAdapter(adapter)
        viewPager.offscreenPageLimit = adapter.count
        itemView.tabLayout.setupWithViewPager(viewPager)
    }
}