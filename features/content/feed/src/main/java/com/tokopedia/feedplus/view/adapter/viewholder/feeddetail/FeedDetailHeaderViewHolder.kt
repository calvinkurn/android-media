package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderModel
import com.tokopedia.kolcommon.util.TimeConverter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.feed_detail_header.view.*

/**
 * @author by nisie on 5/19/17.
 */
class FeedDetailHeaderViewHolder(itemView: View, private val viewListener: FeedPlusDetailListener) : AbstractViewHolder<FeedDetailHeaderModel>(itemView) {


    override fun bind(viewModel: FeedDetailHeaderModel) {
        itemView.run {
            val shopNameString = MethodChecker.fromHtml(viewModel.shopName).toString()
            ImageHandler.LoadImage(shopAvatar, viewModel.shopAvatar)
            if (viewModel.isOfficialStore) {
                goldMerchant.hide()
                officialStore.show()
            } else if (viewModel.isGoldMerchant) {
                goldMerchant.show()
                officialStore.hide()
            } else {
                goldMerchant.hide()
                officialStore.hide()
            }
            shopName.text = shopNameString
            shopName.movementMethod = LinkMovementMethod.getInstance()
            if (viewModel.actionText.isNotEmpty()) {
                shopSlogan.text = String.format(
                        getString(R.string.feed_header_time_format),
                        TimeConverter.generateTime(shopSlogan.context, viewModel.time),
                        viewModel.actionText)
            } else {
                shopSlogan.text = TimeConverter.generateTime(shopSlogan.context, viewModel.time)
            }
            shopAvatar.setOnClickListener { viewListener.onGoToShopDetail(viewModel.activityId, viewModel.shopId) }
            this.setOnClickListener { viewListener.onGoToShopDetail(viewModel.activityId, viewModel.shopId) }
        }

    }

    private fun setSpan(actionSpanString: SpannableString, `object`: Any,
                        titleText: StringBuilder, stringEdited: String) {
        if (`object` is ImageSpan) {
            actionSpanString.setSpan(`object`, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (titleText.toString().contains(stringEdited)) {
            actionSpanString.setSpan(
                    `object`,
                    titleText.indexOf(stringEdited),
                    titleText.indexOf(stringEdited) + stringEdited.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.feed_detail_header
    }
}