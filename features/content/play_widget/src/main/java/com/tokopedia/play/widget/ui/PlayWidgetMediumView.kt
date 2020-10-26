package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardMediumAdapter
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetMediumListener
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.snaphelper.PlayWidgetSnapHelper
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage
import com.tokopedia.play_common.widget.playBannerCarousel.extension.setGradientBackground
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.abs


/**
 * Created by mzennis on 06/10/20.
 */
class PlayWidgetMediumView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val background: LoaderImageView

    private val title: Typography
    private val actionTitle: TextView

    private val itemContainer: FrameLayout
    private val overlay: FrameLayout
    private val overlayBackground: AppCompatImageView
    private val overlayImage: AppCompatImageView

    private val recyclerViewItem: RecyclerView

    private val snapHelper: SnapHelper = PlayWidgetSnapHelper(context)

    private var mListener: PlayWidgetMediumListener? = null

    private val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    private val adapter = PlayWidgetCardMediumAdapter(channelCardListener = object : PlayWidgetCardMediumChannelViewHolder.Listener{
        override fun onCardChannelClick(appLink: String) {
            mListener?.onCardChannelClick(appLink)
        }

        override fun onToggleReminderClick(channelId: String, remind: Boolean, position: Int) {
            mListener?.onToggleReminderClicked(channelId, remind, position)
        }
    })

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_play_widget_medium, this)
        background = view.findViewById(R.id.play_widget_medium_bg_loader)

        title = view.findViewById(R.id.play_widget_medium_title)
        actionTitle = view.findViewById(R.id.play_widget_medium_action)

        itemContainer = view.findViewById(R.id.play_widget_container)
        overlay = view.findViewById(R.id.play_widget_overlay)
        overlayBackground = view.findViewById(R.id.play_widget_overlay_bg)
        overlayImage = view.findViewById(R.id.play_widget_overlay_image)

        recyclerViewItem = view.findViewById(R.id.play_widget_recycler_view)

        setupView(view)
    }

    private fun setupView(view: View) {
        recyclerViewItem.layoutManager = layoutManager
        recyclerViewItem.adapter = adapter
        recyclerViewItem.addOnScrollListener(configureParallax())

        snapHelper.attachToRecyclerView(recyclerViewItem)
    }

    fun setData(data: PlayWidgetUiModel.Medium) {
        title.text = data.title
        actionTitle.text = data.actionTitle
        actionTitle.setOnClickListener { RouteManager.route(context, data.actionAppLink) }

        configureBackgroundOverlay(data.background)

        adapter.setItemsAndAnimateChanges(data.items)
    }

    fun setListener(listener: PlayWidgetListener?) {
        if (listener is PlayWidgetMediumListener) {
            mListener = listener
        }
    }

    /**
     * Setup view
     */
    private fun configureBackgroundOverlay(data: PlayWidgetBackgroundUiModel) {
        if (data.overlayImageUrl.isEmpty() || data.overlayImageUrl.isBlank()) background.hide()
        else {
            background.show()
            overlayImage.loadImage(data.overlayImageUrl, object : ImageHandler.ImageLoaderStateListener {
                override fun successLoad() {
                    configureBackground(data)
                    background.hide()
                }

                override fun failedLoad() {
                    configureBackground(data)
                    background.hide()
                }
            })
        }
    }

    private fun configureBackground(data: PlayWidgetBackgroundUiModel) {
        if (data.gradientColors.isNotEmpty()) {
            overlayBackground.setGradientBackground(data.gradientColors)
        } else if (data.backgroundUrl.isNotBlank() && data.backgroundUrl.isNotEmpty()) {
            overlayBackground.loadImage(data.backgroundUrl)
        }
    }

    private fun configureParallax(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() != 0) return

                val firstView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
                firstView?.let {
                    val distanceFromLeft = it.left
                    val translateX = distanceFromLeft * 0.2f
                    overlay.translationX = translateX

                    if (distanceFromLeft <= 0) {
                        val itemSize = it.width.toFloat()
                        val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                        overlayImage.alpha = 1 - alpha
                    }
                }
            }
        }
    }
}