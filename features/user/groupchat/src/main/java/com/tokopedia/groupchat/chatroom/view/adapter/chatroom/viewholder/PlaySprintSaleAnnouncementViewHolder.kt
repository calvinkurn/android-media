package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel

/**
 * @author : Steven 18/02/19
 */
class PlaySprintSaleAnnouncementViewHolder(itemView: View, var listener: ChatroomContract.ChatItem.SprintSaleViewHolderListener) : AbstractViewHolder<SprintSaleAnnouncementViewModel>(itemView) {


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.play_sprint_sale_announcement_view_holder
    }

    private val icon: ImageView
    private val title: TextView
    private val contentImage: ImageView
    private val price: TextView
    private val priceBefore: TextView
    private val stockProgress: ProgressBar

    init {
        icon = itemView.findViewById(R.id.icon)
        title = itemView.findViewById(R.id.title)
        contentImage = itemView.findViewById(R.id.content_image)
        price = itemView.findViewById(R.id.price)
        priceBefore = itemView.findViewById(R.id.price_before_discount)
        stockProgress = itemView.findViewById(R.id.stock_progress)
    }

    override fun bind(element: SprintSaleAnnouncementViewModel) {
        val type = element.sprintSaleType.toLowerCase()
        ImageHandler.loadImageWithId(icon, getIcon(type))

        title.text = getTitle(type)
        title.setTextColor(getTitleTextColor(type))

        if(element.listProducts.size == 0) {
            return
        }

        var item = element.listProducts[0]
        ImageHandler.loadImage(contentImage.context, contentImage, item.productImage, R.drawable.ic_loading_toped_new)
        price.text = item.productPrice
        priceBefore.text = item.productPriceBeforeDiscount
        stockProgress.progress = item.stockPercentage.toInt()
        itemView.setOnClickListener { listener.onSprintSaleComponentClicked(element) }
    }

    private fun getIcon(voteType: String?): Int {
        return when (voteType){
            SprintSaleViewModel.TYPE_ACTIVE -> R.drawable.ic_sprint_sale_active
            SprintSaleViewModel.TYPE_FINISHED -> R.drawable.ic_sprint_sale_inactive
            else -> 0
        }
    }

    private fun getTitle(voteType: String?): CharSequence? {
        return when (voteType){
            SprintSaleViewModel.TYPE_ACTIVE -> itemView.context.getString(R.string.title_sprintsale_started)
            SprintSaleViewModel.TYPE_FINISHED-> itemView.context.getString(R.string.title_sprintsale_finished)
            else -> null
        }
    }

    private fun getTitleTextColor(voteType: String?): Int {
        return when (voteType){
            SprintSaleViewModel.TYPE_ACTIVE -> ContextCompat.getColor(itemView.context, R.color.sprint_sale_start)
            SprintSaleViewModel.TYPE_FINISHED-> ContextCompat.getColor(itemView.context, R.color.sprint_sale_end)
            else -> 0
        }
    }
}