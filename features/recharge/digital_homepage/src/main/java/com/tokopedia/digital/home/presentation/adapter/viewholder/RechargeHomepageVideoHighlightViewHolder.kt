package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageVideoHighlightModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.VIDEO_HIGHLIGHT_IMPRESSION
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.*

class RechargeHomepageVideoHighlightViewHolder(
        val view: View,
        val listener: OnItemBindListener
): AbstractViewHolder<RechargeHomepageVideoHighlightModel>(view) {
    // TODO: Add subtitle

    internal val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val play = view.findViewById<ImageView>(R.id.play)
    private val thumbnailView = view.findViewById<ImageView>(R.id.thumbnail_image_play)
    private val imageViewer = view.findViewById<ImageView>(R.id.image_viewer)
    private val viewer = view.findViewById<TextView>(R.id.viewer)
    private val live = view.findViewById<View>(R.id.live)
    private val titlePlay = view.findViewById<TextView>(R.id.title_play)
    private val seeAll = view.findViewById<TextView>(R.id.play_txt_see_all)
    private val broadcasterName = view.findViewById<TextView>(R.id.title_description)
    private val title = view.findViewById<TextView>(R.id.title)
    private val subTitle = view.findViewById<TextView>(R.id.subtitle)

    companion object {
        @LayoutRes val LAYOUT = R.layout.view_recharge_home_video_highlight
    }

    override fun bind(element: RechargeHomepageVideoHighlightModel) {
//        if(element?.playCardHome == null){
//            container.hide()
//            listener.getPlayChannel(adapterPosition)
//        } else {
            onBind(element)
//        }
    }

    private fun onBind(element: RechargeHomepageVideoHighlightModel) {
        if (element.section.items.isNotEmpty()) container.show() else container.hide()
        initView(element.section)
//        initAutoPlayVideo(playCardViewModel)
    }

//    private fun initAutoPlayVideo(playCardDataModel: PlayCardDataModel) {
//        val videoStream = playCardDataModel.playCardHome?.videoStream
//        if (videoStream != null) {
//            helper?.isAutoPlay = videoStream.config.isAutoPlay
//            if (helper?.isAutoPlay == true && videoStream.config.streamUrl.isNotEmpty()) {
//                playChannel(videoStream.config.streamUrl)
//            }
//        }
//    }

    private fun initView(section: RechargeHomepageSections.Section){
//        model.playCardHome?.let{ playChannel ->
            handlingTracker(section)
            title.text = section.title
            subTitle.text = section.subTitle

//            if (model.channel.header.applink.isNotEmpty()) {
//                seeAll.visible()
//            } else {
                seeAll.gone()
//            }

            val item = section.items[0]
            thumbnailView.show()
            thumbnailView.loadImage(item.mediaUrl)

            broadcasterName.text = item.label2
            titlePlay.text = item.label1

//            if(playChannel.totalView.isNotEmpty() && playChannel.isShowTotalView){
                viewer.text = item.label3
                viewer.show()
                imageViewer.show()
//            } else {
//                viewer.hide()
//                imageViewer.hide()
//            }

//            if(playChannel.videoStream.isLive)
                live.show()
//            else live.hide()

            container.setOnClickListener {
                RouteManager.route(view.context, item.applink)
            }

//            seeAll.setOnClickListener {
//                goToChannelList(model.channel.header.applink)
//            }
//        }
    }

    private fun handlingTracker(model: RechargeHomepageSections.Section){
        container.addOnImpressionListener(model){
            listener.onRechargeSectionItemImpression(model.items, VIDEO_HIGHLIGHT_IMPRESSION)
        }
    }
}
