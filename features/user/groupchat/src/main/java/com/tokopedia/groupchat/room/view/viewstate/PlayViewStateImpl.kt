package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.common.util.TextFormatter
import com.tokopedia.groupchat.room.view.fragment.PlayWebviewFragment

/**
 * @author : Steven 13/02/19
 */
class PlayViewStateImpl(var view: View,
                        var activity: FragmentActivity) : PlayViewState {

    private var toolbar: Toolbar = view.findViewById(R.id.toolbar)
    private var channelBanner: ImageView = view.findViewById(R.id.channel_banner)
    var sponsorLayout = view.findViewById<View>(R.id.sponsor_layout)
    var sponsorImage = view.findViewById<ImageView>(R.id.sponsor_image)
    val dynamicIcon = view.findViewById<ImageView>(R.id.dynamic_icon)
    val webviewIcon = view.findViewById<ImageView>(R.id.webview_icon)

    var bottomSheetLayout = view.findViewById<ConstraintLayout>(R.id.bottom_sheet)
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    lateinit var bottomSheetWebviewFragment: PlayWebviewFragment

    lateinit var overlayBottomSheet : CloseableBottomSheetDialog

    override fun onSuccessGetInfoFirstTime(it: ChannelInfoViewModel) {
        setToolbarData(it.title, it.bannerUrl, it.totalView, it.blurredBannerUrl)
        setSponsorData(it.adsId, it.adsImageUrl)
        setDynamicIcon("https://www.tokopedia.com/play/trivia-quiz?campaign=nakamatest")
        setDynamicIconNotification(true)
        setDynamicBackground("")
        setFloatingIcon("https://www.tokopedia.com/play/trivia-quiz?campaign=nakamatest", "")
        setQuickReply()
        setPinnedMessage(it.pinnedMessageViewModel)
        setChannelInfoBottomSheet()
        setOverlayBottomSheet(it.overlayViewModel)
    }

    private fun setQuickReply() {
        //TODO inflate Quick Reply
    }

    private fun setChannelInfoBottomSheet() {
        //TODO channel Info
    }

    private fun setOverlayBottomSheet(overlayViewModel: OverlayViewModel?) {
        if(overlayViewModel == null){
            return
        }

        if(!::overlayBottomSheet.isInitialized){
            //TODO initialize overlay
        }

        //TODO show overlay
    }

    private fun setPinnedMessage(pinnedMessageViewModel: PinnedMessageViewModel?) {
        if (pinnedMessageViewModel == null) {
            return
        }

        if(pinnedMessageViewModel.title.isBlank()){
            //TODO pinnedMessage.visibility = View.GONE
        }

    }

    private fun setDynamicBackground(backgroundUrl: String) {
        if (backgroundUrl.isBlank()) {
            //TODO clear background
        } else {
            //TODO add background
        }
    }

    override fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?) {

        toolbar.title = title

        loadImageChannelBanner(view.context, bannerUrl, blurredBannerUrl)

        setToolbarParticipantCount(view.context, TextFormatter.format(totalView))

        when {
            title != null -> setVisibilityHeader(View.VISIBLE)
            else -> setVisibilityHeader(View.GONE)
        }
    }

    override fun loadImageChannelBanner(context: Context, bannerUrl: String?, blurredBannerUrl: String?) {
        if (TextUtils.isEmpty(blurredBannerUrl)) {
            ImageHandler.loadImageBlur(context, channelBanner, bannerUrl)
        } else {
            ImageHandler.LoadImage(channelBanner, blurredBannerUrl)
        }
    }

    private fun setToolbarParticipantCount(context: Context, totalParticipant: String) {
        val textParticipant = String.format("%s %s", totalParticipant, context.getString(R.string.view))
        toolbar.subtitle = textParticipant
    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    private fun setVisibilityHeader(visible: Int) {
        toolbar.visibility = visible
        channelBanner.visibility = visible
    }


    override fun setSponsorData(adsId: String?, adsImageUrl: String?) {
        if (adsId == null) {
            sponsorLayout.visibility = View.GONE
        } else {
            sponsorLayout.visibility = View.VISIBLE

            ImageHandler.loadImage2(sponsorImage,
                    adsImageUrl,
                    R.drawable.loading_page)
        }
    }

    private fun setFloatingIcon(redirectUrl: String, iconUrl: String) {
        if (iconUrl.isBlank()
                || redirectUrl.isBlank()
                || !RouteManager.isSupportApplink(view.context, redirectUrl)) {
            return
        }

        ImageHandler.LoadImage(webviewIcon, iconUrl)
        webviewIcon.setOnClickListener {
            RouteManager.route(view.context, redirectUrl)
        }

    }

    private fun setDynamicIcon(redirectUrl: String) {
        if (redirectUrl.isBlank()) {
            return
        }

        dynamicIcon.setOnClickListener {
            showWebviewBottomSheet(redirectUrl)
        }
    }

    private fun setDynamicIconNotification(hasNotification: Boolean) {

        if (hasNotification) {
            //TODO set red dot
        } else {
            //TODO clear notification
        }
    }

    private fun showWebviewBottomSheet(url: String) {
        if (!::bottomSheetBehavior.isInitialized) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.setBottomSheetCallback(getBottomSheetCallback())

            bottomSheetLayout.findViewById<ImageView>(R.id.close_button).setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        if (!::bottomSheetWebviewFragment.isInitialized) {
            bottomSheetWebviewFragment = PlayWebviewFragment.createInstance(url)
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.bottom_sheet_fragment_container,
                            bottomSheetWebviewFragment,
                            bottomSheetWebviewFragment.javaClass.simpleName)
                    .commit()
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    private fun getBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback? {
        return object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return if (::bottomSheetBehavior.isInitialized
                && bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            true
        } else false
    }

}
