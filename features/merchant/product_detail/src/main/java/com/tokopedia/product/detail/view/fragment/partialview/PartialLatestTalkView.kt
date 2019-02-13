package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.utils.LabelUtils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.talk.ProductTalkQuery
import com.tokopedia.product.detail.data.util.thousandFormatted
import kotlinx.android.synthetic.main.partial_product_latest_talk.view.*

class PartialLatestTalkView  private constructor(private val view: View) {
    companion object {
        fun build(_view: View) = PartialLatestTalkView(_view)
    }

    fun renderData(latestTalk: ProductTalkQuery, totalTalk: Int, productShopId: Int){
        with(view){
            if (latestTalk.talk.count > 0){
                val talk = latestTalk.talk.talks[0]
                ImageHandler.loadImageRounded2(context, iv_talk_user_ava, talk.user.thumbnail)
                txt_talk_user_name.text = MethodChecker.fromHtml(talk.user.fullName)
                txt_talk_date.text = talk.createTime.formatted
                txt_talk_message.text = MethodChecker.fromHtml(talk.message)

                if (talk.id == latestTalk.talkResponse.talkId && latestTalk.talkResponse.talkComment.isNotEmpty()){
                    val resp = latestTalk.talkResponse.talkComment[0]
                    ImageHandler.loadImageRounded2(context, iv_resp_user_ava, resp.user.thumbnail)
                    txt_resp_user_name.text = MethodChecker.fromHtml(resp.user.fullName)
                    txt_resp_date.text = resp.createTime.formatted
                    txt_resp_message.text = MethodChecker.fromHtml(resp.message)
                    val labelUtils = LabelUtils.getInstance(context, txt_resp_date, 35)
                    labelUtils.giveSquareLabel(if (resp.user.shop.id == productShopId) "Penjual" else "Pengguna")

                    iv_resp_user_ava.visible()
                    txt_resp_user_name.visible()
                    txt_resp_date.visible()
                    txt_resp_message.visible()
                } else {
                    iv_resp_user_ava.gone()
                    txt_resp_user_name.gone()
                    txt_resp_date.gone()
                    txt_resp_message.gone()
                }
                txt_see_all_talk.text = context.getString(R.string.label_see_all_talk, totalTalk.thousandFormatted())
                visible()
            } else {
                gone()
            }
        }
    }
}