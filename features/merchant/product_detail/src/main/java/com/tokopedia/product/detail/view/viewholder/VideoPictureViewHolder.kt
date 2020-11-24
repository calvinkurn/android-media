package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.widget.ProductExoPlayer
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import kotlinx.android.synthetic.main.pdp_video_picture_view_holder.view.*

/**
 * Created by Yehezkiel on 23/11/20
 */
class VideoPictureViewHolder(val view: View, private val productVideoCoordinator: ProductVideoCoordinator?) : RecyclerView.ViewHolder(view), VideoPictureReceiver {

    private var mPlayer: ProductExoPlayer? = null
    private var mVideoUrl: String = ""
    private var mVideoId: String = ""

    companion object {
        val LAYOUT = R.layout.pdp_video_picture_view_holder
    }

    fun bind(data: MediaDataModel) {
        if (data.type == "video") {
            mVideoUrl = data.videoUrl
            mVideoId = data.id
            productVideoCoordinator?.configureVideoCoordinator(isVideoType(), view.context, data.id, data.videoUrl)
            setVideoType()
        } else {
            view.pdp_main_img.loadImageWithoutPlaceholder(data.urlOriginal)
            setImageType()
        }
    }

    private fun setVideoType() = with(view) {
        pdp_main_img.hide()
        pdp_main_video.show()
    }

    private fun setImageType() = with(view) {
        pdp_main_img.show()
        pdp_main_video.hide()
    }

    override fun setPlayer(player: ProductExoPlayer?) = with(view) {
        mPlayer = player
        if (player == null) {
            pdp_main_video.player = null
            pdp_main_video.gone()
        } else {
            pdp_main_video.show()
            pdp_main_video.player = player.getExoPlayer()
        }
    }

    override fun getPlayer(): ProductExoPlayer? = mPlayer

    override fun isVideoType(): Boolean = mVideoUrl.isNotEmpty()

    override fun getVideoId(): String = mVideoId
}

interface VideoPictureReceiver {
    fun setPlayer(player: ProductExoPlayer?)

    fun getPlayer(): ProductExoPlayer?

    fun isVideoType(): Boolean

    fun getVideoId(): String
}