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
import com.tokopedia.groupchat.room.view.adapter.OverflowMenuAdapter
import com.tokopedia.groupchat.room.view.viewmodel.OverflowMenuButtonViewModel
import java.util.*

/**
 * @author : Steven 28/05/19
 */
class OverflowMenuHelper(
        var context: Context,
        private var onInfoMenuClicked: () -> Unit
) {


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
        var list = ArrayList<OverflowMenuButtonViewModel>()

        var infoClickListener = {
            onInfoMenuClicked.invoke()
            dismissDialog()
        }
        list.add(OverflowMenuButtonViewModel(getStringResource(R.string.menu_info), infoClickListener, R.drawable.ic_info_play))

        var videoQualityText = String.format(getStringResource(R.string.menu_video_quality), 480)
        var changeVideoQualityListener:() -> Unit = {createVideoQualityMenu()}
        list.add(OverflowMenuButtonViewModel(videoQualityText, changeVideoQualityListener, R.drawable.ic_menu_quality_video))
        list.add(OverflowMenuButtonViewModel(getStringResource(R.string.menu_hide_video), infoClickListener, R.drawable.ic_menu_hide_video))

        menuAdapter.setDataList(list)
    }

    private fun createVideoQualityMenu(){
        var list = ArrayList<OverflowMenuButtonViewModel>()
        list.add(OverflowMenuButtonViewModel(getStringResource(R.string.video_quality_480), {dismissDialog()}, 0, true))
        list.add(OverflowMenuButtonViewModel(getStringResource(R.string.video_quality_720), {dismissDialog()}, 0))
        menuAdapter.setDataList(list)
    }

    private fun dismissDialog(){
        if (::overflowMenuDialog.isInitialized) {
            overflowMenuDialog?.dismiss()
        }
    }
}