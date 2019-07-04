package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
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
        var videoContainer: View,
        var changeQualityVideoVertical: (Int) -> Unit
): PlayBaseHelper(model) {


    private var videoQuality = 0
    private lateinit var overflowMenuDialog: CloseableBottomSheetDialog
    private var menuAdapter = OverflowMenuAdapter()
    private lateinit var menuDialog: View


    fun showOverflowMenuBottomSheet() {
        if (!::overflowMenuDialog.isInitialized) {
            overflowMenuDialog = CloseableBottomSheetDialog.createInstanceRounded(context)

            overflowMenuDialog.setOnShowListener { dialog ->
                val d = dialog as BottomSheetDialog

                val bottomSheet = d.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            menuDialog = createMenuDialog()
            overflowMenuDialog.setCustomContentView(menuDialog, "", true)
        }
        createOverflowMenu()
        overflowMenuDialog.show()
    }

    private fun createMenuDialog(): View {
        val overflowMenuDialog = LayoutInflater.from(context).inflate(R.layout.layout_overflow_menu, null)
        var recyclerView = overflowMenuDialog.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = menuAdapter
        return overflowMenuDialog
    }

    private fun getStringResource(id: Int): String {
        return context.resources.getString(id)
    }

    private fun createOverflowMenu(){


        var infoClickListener = {
            onInfoMenuClicked.invoke()
            dismissDialog()
        }

        var videoQualityText = String.format(getStringResource(R.string.menu_video_quality), maxOf(videoQuality, 480))

        var changeVideoQualityListener:() -> Unit = {
            createVideoQualityMenu()
        }


        var dismissVideoListener:() -> Unit = {
            dismissVideo()
            dismissDialog()
        }

        var showVideoListener:() -> Unit = {
            showVideo()
            dismissDialog()
        }


        var infoMenu = OverflowMenuButtonViewModel(getStringResource(R.string.menu_info), infoClickListener, R.drawable.ic_info_play)
        var qualityMenu = OverflowMenuButtonViewModel(videoQualityText, changeVideoQualityListener, R.drawable.ic_menu_quality_video)
        var hideMenu = OverflowMenuButtonViewModel(getStringResource(R.string.menu_hide_video), dismissVideoListener, R.drawable.ic_menu_hide_video)
        var showMenu = OverflowMenuButtonViewModel(getStringResource(R.string.menu_show_video), showVideoListener, R.drawable.ic_menu_show_video)

        var list = ArrayList<OverflowMenuButtonViewModel>()

        list.add(infoMenu)
        list.add(qualityMenu)
        if(!viewModel?.videoId.isNullOrBlank()) {
            if (videoContainer.isVisible) {
                list.add(hideMenu)
            } else {
                list.add(showMenu)
            }
        }
        menuAdapter.setDataList(list)
    }

    private fun createVideoQualityMenu(){
        var list = ArrayList<OverflowMenuButtonViewModel>()
        var listener480 = {
            changeQualityVideoVertical.invoke(VideoVerticalHelper.VIDEO_480)
            setQualityVideo(VideoVerticalHelper.VIDEO_480)
            dismissDialog()
        }

        var listener720 = {
            changeQualityVideoVertical.invoke(VideoVerticalHelper.VIDEO_720)
            setQualityVideo(VideoVerticalHelper.VIDEO_720)
            dismissDialog()
        }
        list.add(OverflowMenuButtonViewModel(getStringResource(R.string.video_quality_480), listener480, 0, videoQuality == VideoVerticalHelper.VIDEO_480))
        list.add(OverflowMenuButtonViewModel(getStringResource(R.string.video_quality_720), listener720, 0, videoQuality == VideoVerticalHelper.VIDEO_720))
        menuAdapter.setDataList(list)
    }

    private fun dismissDialog(){
        if (::overflowMenuDialog.isInitialized) {
            overflowMenuDialog?.dismiss()
        }
    }

    private fun dismissVideo() {
        toggleHorizontalVideo.invoke(false)
    }

    private fun showVideo() {
        toggleHorizontalVideo.invoke(true)
    }

    override fun assignViewModel(model: ChannelInfoViewModel) {
        super.assignViewModel(model)
        createOverflowMenu()
    }

    fun setQualityVideo(videoQuality: Int) {
        this.videoQuality = videoQuality
    }
}
