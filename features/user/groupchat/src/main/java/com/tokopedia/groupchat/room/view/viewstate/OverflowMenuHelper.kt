package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.common.design.OverflowMenuItemDecoration
import com.tokopedia.groupchat.room.view.adapter.OverflowMenuAdapter
import com.tokopedia.groupchat.room.view.viewmodel.OverflowMenuButtonViewModel
import com.tokopedia.kotlin.extensions.view.isVisible
import java.util.*

/**
 * @author : Steven 28/05/19
 */
class OverflowMenuHelper(
        model: ChannelInfoViewModel?,
        var context: Context,
        private var onInfoMenuClicked: () -> Unit,
        private var toggleHorizontalVideo: (Boolean) -> Unit,
        private var videoHorizontalContainer: View,
        private var changeQualityVideoVertical: (Int) -> Unit,
        private var videoVerticalContainer: View?,
        private var toggleVerticalVideo: (Boolean) -> Unit,
        private var analytics: GroupChatAnalytics
): PlayBaseHelper(model) {


    private var videoVerticalQuality = 0
    private lateinit var overflowMenuDialog: CloseableBottomSheetDialog
    private var menuAdapter = OverflowMenuAdapter()
    private lateinit var menuDialog: View

    fun showOverflowMenuBottomSheet() {
        if (!::overflowMenuDialog.isInitialized) {
            overflowMenuDialog = CloseableBottomSheetDialog.createInstanceRounded(context)

            overflowMenuDialog.setOnShowListener { dialog ->
                val d = dialog as BottomSheetDialog

                val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            menuDialog = createMenuDialog()
            overflowMenuDialog.setCustomContentView(menuDialog, "", false)
        }
        createOverflowMenu()
        overflowMenuDialog.show()
    }

    private fun createMenuDialog(): View {
        val overflowMenuDialog = LayoutInflater.from(context).inflate(R.layout.layout_overflow_menu, null)
        val recyclerView = overflowMenuDialog.findViewById<RecyclerView>(com.tokopedia.abstraction.R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = menuAdapter
        recyclerView.addItemDecoration(OverflowMenuItemDecoration(context))
        return overflowMenuDialog
    }

    private fun getStringResource(id: Int): String {
        return context.resources.getString(id)
    }

    private fun createOverflowMenu(){

        var infoClickListener = {
            onInfoMenuClicked.invoke()
            dismissDialog()
            analytics.eventClickInfoVerticalVideo(viewModel?.channelId)
        }

        var videoQualityText = String.format(getStringResource(R.string.menu_video_quality), maxOf(videoVerticalQuality, 480))

        var changeVideoQualityListener:() -> Unit = {
            createVideoQualityMenu()
        }

        var dismissVideoHorizontalListener:() -> Unit = {
            dismissVideoHorizontal()
            dismissDialog()
            analytics.eventClickHideVerticalVideo(viewModel?.channelId)
        }

        var showVideoHorizontalListener:() -> Unit = {
            showVideoHorizontal()
            dismissDialog()
            analytics.eventClickShowVerticalVideo(viewModel?.channelId)
        }

        var dismissVideoVerticalListener:() -> Unit = {
            dismissVideoVertical()
            dismissDialog()
            analytics.eventClickHideVerticalVideo(viewModel?.channelId)
        }

        var showVideoVerticalListener:() -> Unit = {
            showVideoVertical()
            dismissDialog()
            analytics.eventClickShowVerticalVideo(viewModel?.channelId)
        }

        val infoMenu = OverflowMenuButtonViewModel(getStringResource(R.string.menu_info), infoClickListener, R.drawable.ic_info_play)
        val qualityMenu = OverflowMenuButtonViewModel(videoQualityText, changeVideoQualityListener, R.drawable.ic_menu_quality_video)
        val hideMenuHorizontal = OverflowMenuButtonViewModel(getStringResource(R.string.menu_hide_video), dismissVideoHorizontalListener, R.drawable.ic_menu_hide_video)
        val showMenuHorizontal = OverflowMenuButtonViewModel(getStringResource(R.string.menu_show_video), showVideoHorizontalListener, R.drawable.ic_menu_show_video)
        val hideMenuVertical = OverflowMenuButtonViewModel(getStringResource(R.string.menu_hide_video), dismissVideoVerticalListener, R.drawable.ic_menu_hide_video)
        val showMenuVertical = OverflowMenuButtonViewModel(getStringResource(R.string.menu_show_video), showVideoVerticalListener, R.drawable.ic_menu_show_video)



        val list = ArrayList<OverflowMenuButtonViewModel>()

        list.add(infoMenu)

        if (videoVerticalQuality > 0) {
            val verticalVideoVisibility = videoVerticalContainer?.isVisible
            verticalVideoVisibility?.let {
                if(it) list.add(qualityMenu)
            }
        }

        if(!viewModel?.videoId.isNullOrBlank()) {
            if (videoHorizontalContainer.isVisible) {
                list.add(hideMenuHorizontal)
            } else {
                list.add(showMenuHorizontal)
            }
        } else if (videoVerticalQuality > 0) {
            videoVerticalContainer?.let {
                if(it.isVisible) {
                    list.add(hideMenuVertical)
                } else {
                    list.add(showMenuVertical)
                }
            }
        }

        menuAdapter.setDataList(list)
    }

    private fun createVideoQualityMenu(){
        val list = ArrayList<OverflowMenuButtonViewModel>()
        val listener480 = {
            changeQualityVideoVertical.invoke(VideoVerticalHelper.VIDEO_480)
            setQualityVideo(VideoVerticalHelper.VIDEO_480)
            dismissDialog()
            analytics.eventClickChangeQualityVerticalVideo(viewModel?.channelId, VideoVerticalHelper.VIDEO_480.toString())
        }

        val listener720 = {
            changeQualityVideoVertical.invoke(VideoVerticalHelper.VIDEO_720)
            setQualityVideo(VideoVerticalHelper.VIDEO_720)
            dismissDialog()
            analytics.eventClickChangeQualityVerticalVideo(viewModel?.channelId, VideoVerticalHelper.VIDEO_720.toString())
        }
        list.add(OverflowMenuButtonViewModel(
                getStringResource(R.string.video_quality_480),
                listener480,
                0,
                videoVerticalQuality == VideoVerticalHelper.VIDEO_480
        ))
        list.add(OverflowMenuButtonViewModel(
                getStringResource(R.string.video_quality_720),
                listener720,
                0,
                videoVerticalQuality == VideoVerticalHelper.VIDEO_720
        ))
        menuAdapter.setDataList(list)
    }

    private fun dismissDialog(){
        if (::overflowMenuDialog.isInitialized) {
            overflowMenuDialog?.dismiss()
        }
    }

    private fun dismissVideoHorizontal() {
        toggleHorizontalVideo.invoke(false)
    }

    private fun showVideoHorizontal() {
        toggleHorizontalVideo.invoke(true)
    }

    private fun dismissVideoVertical() {
        toggleVerticalVideo.invoke(false)
    }

    private fun showVideoVertical() {
        changeQualityVideoVertical.invoke(videoVerticalQuality)
        toggleVerticalVideo.invoke(true)
    }

    override fun assignViewModel(model: ChannelInfoViewModel) {
        super.assignViewModel(model)
        createOverflowMenu()
    }

    fun setQualityVideo(videoQuality: Int) {
        this.videoVerticalQuality = videoQuality
    }

    fun hideBottomSheet() {
        if(::overflowMenuDialog.isInitialized){
            overflowMenuDialog?.dismiss()
        }
    }
}
