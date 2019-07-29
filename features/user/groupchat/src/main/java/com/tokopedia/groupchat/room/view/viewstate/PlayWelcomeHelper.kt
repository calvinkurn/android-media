package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.common.util.TextFormatter

/**
 * @author : Steven 28/05/19
 */
class PlayWelcomeHelper(
        model: ChannelInfoViewModel?,
        var analytics: GroupChatAnalytics,
        var context: Context,
        var view: View
): PlayBaseHelper(model) {


    private lateinit var welcomeInfoDialog: CloseableBottomSheetDialog

    fun showInfoBottomSheet(channelInfoViewModel: ChannelInfoViewModel,
                                    onDismiss: () -> Unit) {
        if (!::welcomeInfoDialog.isInitialized) {
            welcomeInfoDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
        }

        welcomeInfoDialog.setOnDismissListener {
            onDismiss()
            analytics.eventClickJoin(channelInfoViewModel.channelId)
        }

        val welcomeInfoView = createWelcomeInfoView(welcomeInfoDialog, channelInfoViewModel)
        welcomeInfoDialog.setOnShowListener() { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        welcomeInfoDialog.setCustomContentView(welcomeInfoView, "", false)
        view.setOnClickListener(null)
        welcomeInfoDialog.show()

    }

    private fun createWelcomeInfoView(welcomeInfoDialog: CloseableBottomSheetDialog,
                                      channelInfoViewModel: ChannelInfoViewModel): View {
        val welcomeInfoView = LayoutInflater.from(context).inflate(R.layout
                .channel_info_bottom_sheet_dialog, null)

        val image = welcomeInfoView.findViewById<ImageView>(R.id.product_image)
        val profile = welcomeInfoView.findViewById<ImageView>(R.id.prof_pict)
        val title = welcomeInfoView.findViewById<TextView>(R.id.title)
        val subtitle = welcomeInfoView.findViewById<TextView>(R.id.subtitle)
        val name = welcomeInfoView.findViewById<TextView>(R.id.name)
        val participant = welcomeInfoView.findViewById<TextView>(R.id.participant)
        val ctaButton = welcomeInfoView.findViewById<TextView>(R.id.action_button)

        participant.text = TextFormatter.format(channelInfoViewModel.totalView.toString())
        name.text = channelInfoViewModel.adminName
        title.text = channelInfoViewModel.title
        subtitle.text = channelInfoViewModel.description

        ImageHandler.loadImage2(image, channelInfoViewModel.image, R.drawable.loading_page)
        ImageHandler.loadImageCircle2(profile.context,
                profile,
                channelInfoViewModel.adminPicture,
                R.drawable.loading_page)

        ctaButton.setOnClickListener {
            welcomeInfoDialog.dismiss()
            analytics.eventClickJoin(channelInfoViewModel.channelId)
        }

        return welcomeInfoView
    }

    fun isShowingDialog(): Boolean {
        return ::welcomeInfoDialog.isInitialized && welcomeInfoDialog.isShowing
    }

    fun setOnDismissListener(function: () -> Unit) {
        welcomeInfoDialog.setOnDismissListener {
            function.invoke()
        }
    }

    fun hideBottomSheet() {
        welcomeInfoDialog?.dismiss()
    }
}