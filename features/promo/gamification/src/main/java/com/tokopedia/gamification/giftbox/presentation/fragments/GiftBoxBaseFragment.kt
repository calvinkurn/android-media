package com.tokopedia.gamification.giftbox.presentation.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.gamification.R
import com.tokopedia.gamification.audio.AudioFactory
import com.tokopedia.gamification.audio.AudioManager
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.analytics.GtmGiftTapTap
import com.tokopedia.gamification.giftbox.presentation.dialogs.NoInternetDialog
import com.tokopedia.gamification.giftbox.presentation.helpers.GiftBoxTapTapAudio
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import com.tokopedia.gamification.giftbox.presentation.views.StarsContainer
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSession


open class GiftBoxBaseFragment : Fragment() {

    val GIFT_SOUND_PREF = "gami_gift_box_sound.pref"
    val GIFT_SOUND_ENABLE = "gami_gift_sound_enable"

    lateinit var loader: LoaderUnify
    lateinit var viewFlipper: ViewFlipper
    lateinit var tvTapHint: AppCompatTextView
    lateinit var starsContainer: StarsContainer
    lateinit var giftBoxDailyView: GiftBoxDailyView
    lateinit var tvLoaderTitle: AppCompatTextView
    lateinit var tvLoaderMessage: AppCompatTextView
    lateinit var toolbar: Toolbar
    lateinit var imageToolbarIcon: AppCompatImageView
    lateinit var tvToolbarTitle: AppCompatTextView
    lateinit var fmParent: ViewGroup
    var imageSound: AppCompatImageView? = null

    val CONTAINER_LOADER = 1
    val CONTAINER_GIFT_BOX = 0

    var screenHeight = 0
    var screenWidth = 0

    val FADE_IN_DURATION = 500L
    val LARGE_PHONE_HEIGHT = 1900

    val giftBoxTapTapAudio = GiftBoxTapTapAudio()
    var bgSoundManager: AudioManager? = null
    var rewardSoundManager: AudioManager? = null
    var mAudiosManager: AudioManager? = null
    var timeoutAudioManager: AudioManager? = null
    var countDownAudioManager: AudioManager? = null
    var defaultErrorMessage = ""
    var userSession: UserSession? = null
    var isTablet = false
    open fun getLayout() = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        val v = LayoutInflater.from(context).inflate(getLayout(), container, false)
        userSession = UserSession(context)
        getScreenDimens()
        initViews(v)
        return v
    }

    open fun initViews(v: View) {
        giftBoxDailyView = v.findViewById(R.id.gift_box_view)
        loader = v.findViewById(R.id.loader)
        tvTapHint = v.findViewById(R.id.tvTapHint)
        starsContainer = v.findViewById(R.id.starsContainer)
        viewFlipper = v.findViewById(R.id.viewFlipper)
        tvLoaderTitle = v.findViewById(R.id.tvLoaderTitle)
        tvLoaderMessage = v.findViewById(R.id.tvLoaderMessage)
        toolbar = v.findViewById(R.id.toolbar)
        tvToolbarTitle = v.findViewById(R.id.tvToolbarTitle)
        imageToolbarIcon = v.findViewById(R.id.imageToolbarIcon)
        fmParent = v.findViewById(R.id.fmParent)
        imageSound = v.findViewById(R.id.imageSound)

        context?.let {
            defaultErrorMessage = it.getString(com.tokopedia.gamification.R.string.gami_gift_default_error_msg)
            val tabletRes = it.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet)
            tabletRes?.let { tablet ->
                isTablet = tablet
            }
        }

        setInitialPositionOfViews()
        initialViewSetup()
        toggleSound(isSoundEnabled())

        imageToolbarIcon.setOnClickListener {
            when (this) {
                is GiftBoxDailyFragment -> GtmEvents.clickBackButton(userSession?.userId)
            }
            activity?.onBackPressed()
        }

        imageSound?.setOnClickListener {
            val state = !isSoundEnabled()
            toggleSound(state)
            if (state) {
                playLoopSound()
            } else {
                stopBgSound()
            }
        }
    }

    open fun initialViewSetup() {
        loader.visibility = View.GONE
        giftBoxDailyView.alpha = 0f
        tvTapHint.alpha = 0f

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = ""
            tvToolbarTitle.text = activity?.getString(R.string.gami_gift_60_toolbar_title)
        }
        ImageViewCompat.setImageTintList(imageToolbarIcon, ColorStateList.valueOf(ContextCompat.getColor(imageToolbarIcon.context, android.R.color.white)))
        tvToolbarTitle.setTextColor(ContextCompat.getColor(tvToolbarTitle.context, android.R.color.white))
    }

    fun getScreenDimens() {
        if (activity != null) {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            screenHeight = displayMetrics.heightPixels
            screenWidth = displayMetrics.widthPixels
        }
    }

    fun setInitialPositionOfViews() {
        //Do nothing
    }

    fun showLoader() {
        viewFlipper.displayedChild = CONTAINER_LOADER
        loader.visibility = View.VISIBLE
    }

    fun hideLoader() {
        viewFlipper.displayedChild = CONTAINER_GIFT_BOX
        loader.visibility = View.GONE
    }

    open fun getMenu() = R.menu.gami_menu_share

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(getMenu(), menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                performShareAction()
            }
            R.id.action_info -> {
                handleInfoIconClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    open fun handleInfoIconClick() {}

    open fun performShareAction() {
        try {
            var userName = ""
            var shareText = ""
            context?.let {
                userName = UserSession(it).name.trim()
                shareText = when (this) {
                    is GiftBoxTapTapFragment -> String.format(it.getString(R.string.gami_gift_60_share), userName)
                    else -> String.format(it.getString(R.string.gami_gift_share), userName)
                }
            }

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"

            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            when (this) {
                is GiftBoxTapTapFragment -> GtmGiftTapTap.clickShareButton(userSession?.userId)
                is GiftBoxDailyFragment -> GtmEvents.clickShareButton(userSession?.userId)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun showRedError(view: View, message: String, actionText: String, method: (() -> Unit)?) {
        Toaster.toasterCustomBottomHeight = 30.toPx()
        val snackbar = Toaster.build(
                view,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                actionText = actionText,
                clickListener = View.OnClickListener {
                    GtmEvents.clickToaster(userSession?.userId)
                    method?.invoke()
                }
        )
        snackbar.show()
        val params = snackbar.view.layoutParams
        when (params) {
            is CoordinatorLayout.LayoutParams -> params.gravity = Gravity.BOTTOM
            is FrameLayout.LayoutParams -> params.gravity = Gravity.BOTTOM
        }
        params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT
        snackbar.view.layoutParams = params

        when (this) {
            is GiftBoxTapTapFragment -> GtmGiftTapTap.viewToastError(userSession?.userId, message)
            is GiftBoxDailyFragment -> GtmEvents.viewToastError(userSession?.userId, message)
        }
    }

    fun showNoInterNetDialog(method: (() -> Unit), context: Context) {
        val dialog = NoInternetDialog()
        dialog.showDialog(context)
        dialog.btnRetry.setOnClickListener {
            dialog.bottomSheet?.dismiss()
            method.invoke()
            when (this) {
                is GiftBoxTapTapFragment -> GtmGiftTapTap.clickTryAgain(userSession?.userId)
                is GiftBoxDailyFragment -> GtmEvents.clickTryAgainButton(userSession?.userId)
            }
        }

        when (this) {
            is GiftBoxTapTapFragment -> GtmGiftTapTap.viewNoInternetError(userSession?.userId)
            is GiftBoxDailyFragment -> GtmEvents.viewNoInternetError(userSession?.userId)
        }
    }

    fun toggleSound(enable: Boolean) {
        imageSound?.let {
            val editor = getSharedPres()?.edit()
            editor?.putBoolean(GIFT_SOUND_ENABLE, enable)
            editor?.apply()
            if (enable) {
                it.setImageResource(com.tokopedia.gamification.R.drawable.gf_ic_sound_on)
            } else {
                it.setImageResource(com.tokopedia.gamification.R.drawable.gf_ic_sound_off)
            }
        }
    }

    fun stopBgSound() {
        if (bgSoundManager != null) {
            bgSoundManager?.mPlayer?.pause()
        }
    }

    fun resumeBgSound() {
        if (bgSoundManager != null) {
            val pos = bgSoundManager?.mPlayer?.currentPosition
            if (pos != null) {
                bgSoundManager?.mPlayer?.seekTo(pos)
                bgSoundManager?.mPlayer?.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopBgSound()
    }

    override fun onResume() {
        super.onResume()
        if (isSoundEnabled()) {
            resumeBgSound()
        }
    }

    open fun playLoopSound() {
        if (isSoundEnabled()) {
            context?.let { it ->
                bgSoundManager = AudioFactory.createAudio(it)
                if (giftBoxTapTapAudio.bgSoundPath.isNullOrEmpty()) {
                    bgSoundManager?.playAudio(com.tokopedia.gamification.R.raw.gf_giftbox_bg_tap_60, true)
                } else {
                    bgSoundManager?.playAudio(giftBoxTapTapAudio.bgSoundPath!!, true)
                }
            }
        }
    }

    fun isBackgroundSoundPlaying(): Boolean {
        return bgSoundManager?.mPlayer?.isPlaying ?: false
    }

    fun playTapSound() {
        if (isSoundEnabled()) {
            context?.let { soundIt ->
                if (mAudiosManager == null) {
                    mAudiosManager = AudioFactory.createAudio(soundIt)
                }
                mAudiosManager?.playAudio(com.tokopedia.gamification.R.raw.gf_giftbox_tap)
            }
        }
    }

    fun playTimeOutSound() {
        if (isSoundEnabled()) {
            context?.let { soundIt ->
                if (timeoutAudioManager == null) {
                    timeoutAudioManager = AudioFactory.createAudio(soundIt)
                }
                if (giftBoxTapTapAudio.timeUpSoundPath.isNullOrEmpty()) {
                    timeoutAudioManager?.playAudio(com.tokopedia.gamification.R.raw.gami_timeout)
                } else {
                    timeoutAudioManager?.playAudio(giftBoxTapTapAudio.timeUpSoundPath!!)
                }

            }
        }
    }

    fun playCountDownSound() {
        if (isSoundEnabled()) {
            context?.let { soundIt ->
                if (countDownAudioManager == null) {
                    countDownAudioManager = AudioFactory.createAudio(soundIt)
                }
                if (giftBoxTapTapAudio.countDownSoundPath.isNullOrEmpty()) {
                    countDownAudioManager?.playAudio(com.tokopedia.gamification.R.raw.gami_count_down)
                } else {
                    countDownAudioManager?.playAudio(giftBoxTapTapAudio.countDownSoundPath!!)
                }
            }
        }
    }

    fun playPrizeSound() {
        if (isSoundEnabled()) {
            context?.let { soundIt ->
                if (rewardSoundManager == null) {
                    rewardSoundManager = AudioFactory.createAudio(soundIt)
                }
                rewardSoundManager?.playAudio(com.tokopedia.gamification.R.raw.gf_giftbox_prize)
            }
        }
    }

    fun isSoundEnabled(): Boolean {
        val prefs = getSharedPres()
        if (prefs != null) {
            return prefs.getBoolean(GIFT_SOUND_ENABLE, true)
        }
        return true
    }

    fun getSharedPres(): SharedPreferences? {
        return context?.getSharedPreferences(GIFT_SOUND_PREF, Context.MODE_PRIVATE)
    }

    fun isConnectedToInternet(): Boolean {
        context?.let {
            return DeviceConnectionInfo.isConnectCellular(it) ||
                    DeviceConnectionInfo.isConnectWifi(it)}
        return false
    }
}