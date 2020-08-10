package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.utils.LabelUtils
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionDataModel
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.data.util.productThousandFormatted
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_discussion.view.*

class ProductDiscussionViewHolder(val view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDiscussionDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_discussion
        const val DISCUSSION_LABEL_SELLER = "Penjual"
        const val DISCUSSION_LABEL_BUYER = "Pengguna"
    }

    override fun bind(element: ProductDiscussionDataModel) {
        if (element.latestTalk == null) {
            hideComponent()
        } else {
            showComponent()
            element.latestTalk?.let {
                renderData(it, element.shopId.toInt(), element.talkCount, getComponentTrackData(element))
                view.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
            }
        }
    }

    private fun hideComponent() = with(view) {
        product_discussion_container.layoutParams.height = 0
    }

    private fun showComponent() = with(view) {
        product_discussion_container.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private fun renderData(talk: Talk, productShopId: Int, totalTalk: Int, componentTrackData: ComponentTrackDataModel) {
        with(view) {
            ImageHandler.loadImageRounded2(context, iv_talk_user_ava, talk.userImage)
            txt_talk_user_name.text = MethodChecker.fromHtml(talk.userName)
            txt_talk_date.text = talk.createTimeFmt
            txt_talk_message.text = MethodChecker.fromHtml(talk.message)

            if (talk.commentList.isNotEmpty()) {
                val resp = talk.commentList[0]
                ImageHandler.loadImageRounded2(context, iv_resp_user_ava,
                        if (resp.shopId.toInt() == productShopId) resp.shopImage else resp.userImage)
                txt_resp_user_name.text = MethodChecker.fromHtml(resp.userName)
                txt_resp_date.text = resp.createTimeFmt
                txt_resp_message.text = MethodChecker.fromHtml(resp.message)
                val labelUtils = LabelUtils.getInstance(context, txt_resp_date, 35)
                labelUtils.giveSquareLabel(if (resp.shopId.toInt() == productShopId) DISCUSSION_LABEL_SELLER else DISCUSSION_LABEL_BUYER)

                showCommentList()
            } else {
                removeCommentList()
            }
            txt_see_all_talk.text = context.getString(R.string.label_see_all_talk, totalTalk.productThousandFormatted())
            txt_see_all_talk.setOnClickListener { listener.onLastDiscussionClicked(talk.id, componentTrackData) }
            visible()
        }
    }

    private fun getComponentTrackData(element: ProductDiscussionDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition)

    private fun removeCommentList() {
        with(view) {
            iv_resp_user_ava.gone()
            txt_resp_user_name.gone()
            txt_resp_date.gone()
            txt_resp_message.gone()
        }
    }

    private fun showCommentList() {
        with(view) {
            iv_resp_user_ava.visible()
            txt_resp_user_name.visible()
            txt_resp_date.visible()
            txt_resp_message.visible()
        }
    }

}