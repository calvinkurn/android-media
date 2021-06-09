package com.tokopedia.shop.feed.view.adapter.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.shop.R
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.WhitelistUiModel
import kotlinx.android.synthetic.main.item_post_entry_shop_page.view.*

/**
 * @author by yfsx on 16/05/19.
 */
class WhitelistViewHolder(v: View,
                          private val mainView: FeedShopContract.View)
    : AbstractViewHolder<WhitelistUiModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_post_entry_shop_page

        private const val FORMAT_NAME = "{{name}}"
    }

    override fun bind(element: WhitelistUiModel) {
        initView(element)
        initViewListener(element)
    }

    private fun initView(model: WhitelistUiModel) {
        itemView.tvCaption.text = MethodChecker.fromHtml(formatWhiteListTitle(
                model.whitelist.title))

        itemView.ivAvatar.loadImageCircle(model.whitelist.image)
    }

    private fun formatWhiteListTitle(title: String): String {
        return title
                .replace(FORMAT_NAME, bold(FORMAT_NAME))
                .replace(FORMAT_NAME, if (mainView.userSession.hasShop())
                    mainView.userSession.shopName
                else
                    mainView.userSession.name)
    }

    private fun bold(text: String): String {
        return String.format("<b>%s</b>", text)
    }

    private fun initViewListener(element: WhitelistUiModel) {
        itemView.btnCreatePost.setOnClickListener { mainView.onWhitelistClicked(element) }
    }
}