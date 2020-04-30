package com.tokopedia.gamification.giftbox.presentation.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.gamification.R
import com.tokopedia.gamification.audio.AudioFactory
import com.tokopedia.gamification.audio.AudioManager
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.presentation.dialogs.NoInternetDialog
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer
import com.tokopedia.gamification.giftbox.presentation.views.StarsContainer
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession


open class GiftBoxBaseFragment : Fragment() {

    val GIFT_SOUND_PREF = "gami_gift_box_sound.pref"
    val GIFT_SOUND_ENABLE = "gami_gift_sound_enable"

    lateinit var loader: LoaderUnify
    lateinit var viewFlipper: ViewFlipper
    lateinit var tvTapHint: AppCompatTextView
    lateinit var starsContainer: StarsContainer
    lateinit var rewardContainer: RewardContainer
    lateinit var giftBoxDailyView: GiftBoxDailyView
    lateinit var tvLoaderTitle: AppCompatTextView
    lateinit var tvLoaderMessage: AppCompatTextView
    lateinit var toolbar: Toolbar
    lateinit var imageToolbarIcon: View
    lateinit var tvToolbarTitle: AppCompatTextView
    lateinit var fmParent: FrameLayout
    lateinit var imageSound: AppCompatImageView

    val CONTAINER_LOADER = 1
    val CONTAINER_GIFT_BOX = 0

    var screenHeight = 0
    var screenWidth = 0

    val FADE_IN_DURATION = 500L

    var bgSoundManager: AudioManager? = null
    var rewardSoundManager: AudioManager? = null
    var mAudiosManager: AudioManager? = null
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
        rewardContainer = v.findViewById(R.id.reward_container)
        viewFlipper = v.findViewById(R.id.viewFlipper)
        tvLoaderTitle = v.findViewById(R.id.tvLoaderTitle)
        tvLoaderMessage = v.findViewById(R.id.tvLoaderMessage)
        toolbar = v.findViewById(R.id.toolbar)
        tvToolbarTitle = v.findViewById(R.id.tvToolbarTitle)
        imageToolbarIcon = v.findViewById(R.id.imageToolbarIcon)
        fmParent = v.findViewById(R.id.fmParent)
        imageSound = v.findViewById(R.id.imageSound)

        context?.let {
            defaultErrorMessage = it.getString(R.string.gami_gift_default_error_msg)
            val tabletRes = it.resources?.getBoolean(R.bool.gami_is_tablet)
            tabletRes?.let { tablet ->
                isTablet = tablet
            }
        }

        setInitialPositionOfViews()
        initialViewSetup()
        toggleSound(isSoundEnabled())

        imageToolbarIcon.setOnClickListener {
            GtmEvents.clickBackButton(userSession?.userId)
            activity?.finish()
        }

        imageSound.setOnClickListener {
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
            tvToolbarTitle.text = activity?.getString(R.string.gami_gift_box_toolbar_title)
        }
    }

    fun getScreenDimens() {
        if (activity != null) {
            val displayMetrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gami_menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                performShareAction()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun performShareAction() {
        try {
            var userName = ""
            var shareText = ""
            context?.let {
                userName = UserSession(it).name.trim()
                shareText = String.format(it.getString(R.string.gami_gift_share), userName)
            }

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"

            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            GtmEvents.clickShareButton(userSession?.userId)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun showRedError(view: View, message: String, actionText: String, method: (() -> Unit)?) {
        Toaster.make(view,
                message,
                Snackbar.LENGTH_LONG,
                actionText = actionText,
                clickListener = View.OnClickListener {
                    GtmEvents.clickToaster(userSession?.userId)
                    method?.invoke()
                },
                type = Toaster.TYPE_ERROR)
    }

    fun showNoInterNetDialog(method: (() -> Unit), context: Context) {
        val dialog = NoInternetDialog()
        dialog.showDialog(context)
        dialog.btnRetry.setOnClickListener {
            dialog.closeAbleDialog.dismiss()
            method.invoke()
            GtmEvents.clickTryAgainButton(userSession?.userId)
        }
    }

    fun toggleSound(enable: Boolean) {
        val editor = getSharedPres()?.edit()
        editor?.putBoolean(GIFT_SOUND_ENABLE, enable)
        editor?.apply()
        if (enable) {
            imageSound.setImageResource(R.drawable.gf_ic_sound_on)
        } else {
            imageSound.setImageResource(R.drawable.gf_ic_sound_off)
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
                bgSoundManager?.playAudio(R.raw.gf_giftbox_bg, true)
            }
        }
    }

    fun playTapSound() {
        if (isSoundEnabled()) {
            context?.let { soundIt ->
                if (mAudiosManager == null) {
                    mAudiosManager = AudioFactory.createAudio(soundIt)
                }
                mAudiosManager?.playAudio(R.raw.gf_giftbox_tap)
            }
        }
    }

    fun playPrizeSound() {
        if (isSoundEnabled()) {
            context?.let { soundIt ->
                if (rewardSoundManager == null) {
                    rewardSoundManager = AudioFactory.createAudio(soundIt)
                }
                rewardSoundManager?.playAudio(R.raw.gf_giftbox_prize)
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

    fun fadeOutSoundIcon() {
        imageSound.animate().alpha(0f).setDuration(300L).start()
    }

    fun fadeInSoundIcon() {
        imageSound.animate().alpha(1f).setDuration(300L).start()
    }

    fun isConnectedToInternet(): Boolean {
        context?.let {
            return DeviceConnectionInfo.isConnectCellular(it) || DeviceConnectionInfo.isConnectWifi(it)
        }
        return false
    }
}