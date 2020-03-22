package com.tokopedia.gamification.giftbox.presentation.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.data.entities.Reminder
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.ACTIVE
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.EMPTY
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.EXPIRED
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.INACTIVE
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.NON_LOGIN
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.helpers.doOnLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.updateLayoutParams
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxDailyViewModel
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import com.tokopedia.gamification.giftbox.presentation.views.GiftPrizeLargeView
import com.tokopedia.gamification.giftbox.presentation.views.GiftPrizeSmallView
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_gift_box_daily.*
import javax.inject.Inject

class GiftBoxDailyFragment : GiftBoxBaseFragment() {

    lateinit var tvBenefits: Typography
    lateinit var llBenefits: LinearLayout
    lateinit var prizeViewSmallFirst: GiftPrizeSmallView
    lateinit var prizeViewSmallSecond: GiftPrizeSmallView
    lateinit var prizeViewLarge: GiftPrizeLargeView
    lateinit var llRewardMessage: LinearLayout
    lateinit var tvRewardFirstLine: AppCompatTextView
    lateinit var tvRewardSecondLine: AppCompatTextView
    lateinit var btnAction: AppCompatTextView
    lateinit var tvReminderBtn: AppCompatTextView
    lateinit var tvReminderMessage: AppCompatTextView
    lateinit var loaderReminder: LoaderUnify

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GiftBoxDailyViewModel
    var giftBoxRewardEntity: GiftBoxRewardEntity? = null
    var isReminderSet = false
    var reminder: Reminder? = null

    override fun getLayout() = R.layout.fragment_gift_box_daily

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val component = DaggerGiftBoxComponent.builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
            component.inject(this)

            if (it is AppCompatActivity) {
                val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
                viewModel = viewModelProvider[GiftBoxDailyViewModel::class.java]
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        viewModel.getGiftBox()
        return v
    }

    override fun initViews(v: View) {
        tvBenefits = v.findViewById(R.id.tvBenefits)
        llBenefits = v.findViewById(R.id.ll_benefits)
        llRewardMessage = v.findViewById(R.id.ll_reward_message)
        prizeViewSmallFirst = v.findViewById(R.id.giftPrizeSmallViewFirst)
        prizeViewSmallSecond = v.findViewById(R.id.giftPrizeSmallViewFirst)
        prizeViewLarge = v.findViewById(R.id.giftPrizeLargeView)
        tvRewardFirstLine = v.findViewById(R.id.tvRewardFirstLine)
        tvRewardSecondLine = v.findViewById(R.id.tvRewardSecondLine)
        btnAction = v.findViewById(R.id.btnAction)
        tvLoaderMessage = v.findViewById(R.id.tvLoaderMessage)
        tvReminderBtn = v.findViewById(R.id.tvReminderBtn)
        tvReminderMessage = v.findViewById(R.id.tvReminderMessage)
        loaderReminder = v.findViewById(R.id.loaderReminder)
        super.initViews(v)

        setListeners()
    }

    private fun setListeners() {
        giftBoxDailyView.boxCallback = object : GiftBoxDailyView.BoxCallback {

            override fun onBoxOpenAnimationStart(startDelay: Long) {
                giftBoxDailyView.adjustGlowImagePosition()

                rewardContainer.setFinalTranslationOfCircles(giftBoxDailyView.fmGiftBox.top)
                val stageLightAnim = giftBoxDailyView.stageGlowAnimation()

                //new code===
                giftBoxRewardEntity?.let {
                    rewardContainer.setRewards(it, asyncCallback = { rewardState ->
                        when (rewardState) {
                            RewardContainer.RewardState.COUPON_WITH_POINTS -> {

                                val rewardAnim = rewardContainer.showCouponAndRewardAnimation(giftBoxDailyView.fmGiftBox.top)

                                val animatorSet = AnimatorSet()
                                animatorSet.playTogether(rewardAnim, stageLightAnim)
                                animatorSet.startDelay = startDelay
                                animatorSet.start()

                                val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimation()
                                ovoPointsTextAnim.startDelay = startDelay + 100L
                                ovoPointsTextAnim.start()
                            }
                            RewardContainer.RewardState.POINTS_ONLY -> {
                                val rewardAnim = rewardContainer.showSingleLargeRewardAnimation(giftBoxDailyView.fmGiftBox.top)

                                val animatorSet = AnimatorSet()
                                animatorSet.playTogether(stageLightAnim, rewardAnim)
                                animatorSet.startDelay = startDelay
                                animatorSet.start()
                            }

                            RewardContainer.RewardState.COUPON_ONLY -> {
                                val rewardAnim = rewardContainer.showCouponAndRewardAnimation(giftBoxDailyView.fmGiftBox.top)

                                val animatorSet = AnimatorSet()
                                animatorSet.playTogether(rewardAnim, stageLightAnim)
                                animatorSet.startDelay = startDelay
                                animatorSet.start()
                            }
                        }
                    })

                }
                //new code ends===


            }

            override fun onBoxScaleDownAnimationStart() {
//                fadeOutViews()
            }

            override fun onBoxOpened() {

                val startsAnimatorList = starsContainer.getStarsAnimationList()
                startsAnimatorList.forEach {
                    it.start()
                }
                showRewardMessageDescription().start()
            }
        }

        viewModel.giftBoxLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    if (it.data != null) {
                        val state = it.data.gamiLuckyHome.tokensUser.state
                        when (state) {
                            TokenUserState.ACTIVE -> {
                                renderGiftBoxActive(it.data, state)
                            }
                            TokenUserState.EMPTY -> {
                                renderGiftBoxActive(it.data, state)
                            }
                            TokenUserState.INACTIVE -> {
                                hideLoader()
                            }
                            TokenUserState.EXPIRED -> {
                                hideLoader()
                            }
                            TokenUserState.NON_LOGIN -> {
                                hideLoader()
                            }
                            else -> {
                                hideLoader()
                                renderGiftBoxError()
                            }
                        }
                    }
                }

                LiveDataResult.STATUS.LOADING -> showLoader()

                LiveDataResult.STATUS.ERROR -> {
                    hideLoader()
                    renderGiftBoxError()
                }
            }
        })
        viewModel.rewardLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {

                    if (it.data == null) {
                        //todo Rahul need to handle
                    } else {
                        //set data in rewards first and then animate

                        giftBoxRewardEntity = it.data
                        giftBoxDailyView.handleTapOnGiftBox()
                        fadeOutViews()

                        val benefitText = giftBoxRewardEntity?.gamiCrack?.benefitText
                        if (benefitText != null && benefitText.isNotEmpty()) {
                            tvRewardFirstLine.text = benefitText[0]
                            if (benefitText.size > 1) {
                                val indexOfAnd = benefitText[1].indexOf("&")
                                if (indexOfAnd > 0) {
                                    val array = benefitText[1].split("&")
                                    val sb = StringBuilder()
                                    sb.append(array[0])
                                    sb.append(" &")
                                    sb.append("\n")
                                    sb.append(array[1])
                                    tvRewardSecondLine.text = sb.toString()
                                } else {
                                    tvRewardSecondLine.text = benefitText[1]
                                }

                            }
                        }

                        val actionButtonList = giftBoxRewardEntity?.gamiCrack?.actionButton
                        if (actionButtonList != null && actionButtonList.isNotEmpty()) {
                            btnAction.text = actionButtonList[0].text
                            btnAction.setOnClickListener {
                                val applink = actionButtonList[0].applink
                                if (!applink.isNullOrEmpty()) {
                                    RouteManager.route(context, applink)
                                }
                            }
                        }

                        //set reminder action
                        tvReminderBtn.setOnClickListener {
                            if (!isReminderSet) {
                                viewModel.setReminder()
                            }
                        }
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                }
            }
        })

        viewModel.reminderLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    loaderReminder.visibility = View.VISIBLE
                    tvReminderBtn.visibility = View.INVISIBLE
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    loaderReminder.visibility = View.GONE
                    tvReminderBtn.visibility = View.VISIBLE

                    val code = it.data?.gameRemindMe?.resultStatus?.code
                    val reason = it.data?.gameRemindMe?.resultStatus?.reason

                    if (code == 200) {
                        reminderSuccess(true)
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    loaderReminder.visibility = View.GONE
                    tvReminderBtn.visibility = View.VISIBLE
                }
            }
        })


        viewModel.reminderCheckLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {

                }
                LiveDataResult.STATUS.SUCCESS -> {
                    loaderReminder.visibility = View.GONE
                    tvReminderBtn.visibility = View.VISIBLE

                    val code = it.data?.gameRemindMeCheck?.resultStatus?.code
                    val isRemindMe = it.data?.gameRemindMeCheck?.isRemindMe
                    reminder = it.data?.reminder
                    if (code == 200 && isRemindMe != null && isRemindMe) {
                        reminderSuccess(isRemindMe)
                    }
                }
                LiveDataResult.STATUS.ERROR -> {

                }
            }
        })

        giftBoxDailyView.fmGiftBox.setOnClickListener {
            viewModel.getRewards()
        }
    }

    fun reminderSuccess(isUserReminded: Boolean) {
        context?.let {
            if (isUserReminded) {
                fmReminder.background = ContextCompat.getDrawable(it, R.drawable.gf_bg_disabled_3d)
                tvReminderBtn.text = reminder?.disableText
                isReminderSet = true
            } else {
                tvReminderBtn.text = reminder?.enableText
                fmReminder.background = ContextCompat.getDrawable(it, R.drawable.gf_bg_green_3d)
                isReminderSet = false
            }
            tvReminderMessage.text = reminder?.text
        }
    }


    fun setPositionOfViewsAtBoxOpen(@TokenUserState state: String) {
        rewardContainer.setFinalTranslationOfCircles(giftBoxDailyView.fmGiftBox.top)

        giftBoxDailyView.imageBoxFront.doOnLayout { imageBoxFront ->
            val array = IntArray(2)
            imageBoxFront.getLocationInWindow(array)
            val translationY = array[1].toFloat() - getStatusBarHeight(context) - dpToPx(40f)
            starsContainer.setStartPositionOfStars(starsContainer.width / 2f, translationY)

            val tranY = (screenHeight * 0.385f) - statusBarHeight
            rewardContainer.llRewardTextLayout.translationY = tranY
            rewardContainer.rvCoupons.translationY = array[1].toFloat() - (screenHeight * 0.15f) - dpToPx(158f) - statusBarHeight
//            rewardContainer.rvCoupons.translationY = tranY - dpToPx(20f)
            if (state == TokenUserState.EMPTY) {
                llBenefits.gravity = Gravity.NO_GRAVITY
                llBenefits.translationY = array[1].toFloat() + imageBoxFront.height - getStatusBarHeight(context) + dpToPx(18f)
            }

        }
    }

    fun showRewardMessageDescription(): Animator {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(llRewardMessage, alphaProp)
        alphaAnim.duration = 200L
        return alphaAnim
    }

    override fun initialViewSetup() {
        super.initialViewSetup()
        llBenefits.alpha = 0f
        llRewardMessage.alpha = 0f
        loaderReminder.visibility = View.GONE
        setDynamicMargin()
    }

    fun setDynamicMargin() {
        val dpi = resources.displayMetrics.densityDpi
        when (dpi) {
            DisplayMetrics.DENSITY_MEDIUM, DisplayMetrics.DENSITY_HIGH -> {
                tvTapHint.updateLayoutParams<FrameLayout.LayoutParams> {
                    val margin = dpToPx(40f)
                    setMargins(40, 0, 40, 0)
                }
            }
            else -> {
            }
        }
    }


    fun renderGiftBoxActive(entity: GiftBoxEntity, @TokenUserState state: String) {
        tvTapHint.text = entity.gamiLuckyHome.tokensUser.title
        tvBenefits.text = entity.gamiLuckyHome.tokensUser.text

        if (state == TokenUserState.EMPTY) {
            tvBenefits.setType(Typography.HEADING_2)
            tvBenefits.setWeight(Typography.BOLD)
        }

        //set prize list
        entity.gamiLuckyHome.prizeList?.forEach {
            if (it.isSpecial) {
                prizeViewLarge.setData(it.imageURL, it.text)
                prizeViewLarge.visibility = View.VISIBLE
            } else {
                if (prizeViewSmallFirst.tvTitle.text.isNullOrEmpty()) {
                    prizeViewSmallFirst.setData(it.imageURL, it.text)
                    prizeViewSmallFirst.visibility = View.VISIBLE
                } else {
                    prizeViewSmallSecond.setData(it.imageURL, it.text)
                    prizeViewSmallSecond.visibility = View.VISIBLE
                }
            }
        }

        val imageUrlList = entity.gamiLuckyHome.tokenAsset.imageV2URLs
        var frontImageUrl = ""
        var bgUrl = entity.gamiLuckyHome.tokenAsset.backgroundImgURL
        if (!imageUrlList.isNullOrEmpty()) {
            frontImageUrl = imageUrlList[0]
            if (frontImageUrl.isEmpty()) {
                frontImageUrl = ""
            }
        }

        fadeInActiveStateViews(frontImageUrl, bgUrl, state)
    }

    fun fadeOutViews() {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
        val tvBenefitsAnim = ObjectAnimator.ofPropertyValuesHolder(tvBenefits, alphaProp)
        val prizeListContainerAnim = ObjectAnimator.ofPropertyValuesHolder(llBenefits, alphaProp)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapHintAnim, prizeListContainerAnim, tvBenefitsAnim)
        animatorSet.duration = 300L

        animatorSet.start()
    }

    fun fadeInActiveStateViews(frontImageUrl: String, imageBgUrl: String, @TokenUserState state: String) {
        giftBoxDailyView.loadFiles(frontImageUrl, imageBgUrl, imageCallback = {
            if (it) {
                setPositionOfViewsAtBoxOpen(state)
                hideLoader()

                val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
                val giftBoxAnim = ObjectAnimator.ofPropertyValuesHolder(giftBoxDailyView, alphaProp)
                val prizeListContainerAnim = ObjectAnimator.ofPropertyValuesHolder(llBenefits, alphaProp)

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(tapHintAnim, giftBoxAnim, prizeListContainerAnim)
                animatorSet.duration = FADE_IN_DURATION


                animatorSet.addListener(onEnd = {
                    giftBoxDailyView.startInitialAnimation()
                })
                animatorSet.start()
            } else {
                //todo Rahul Show some error because resources are not loaded
            }
        })


    }

    fun renderGiftBoxEmpty() {}
    fun renderGiftBoxError() {}
    fun renderGiftBoxNoInternet() {}

    enum class GiftBoxState {
        ACTIVE, EMPTY, ERROR, NO_INTERNET
    }

    companion object {
        val TOKEN_USER_STATE = arrayListOf<String>("active", "empty", "inactive", "expired", "nonlogin")
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(ACTIVE, EMPTY, INACTIVE, EXPIRED, NON_LOGIN)
annotation class TokenUserState {
    companion object {
        const val ACTIVE = "active"
        const val EMPTY = "empty"
        const val INACTIVE = "inactive"
        const val EXPIRED = "expired"
        const val NON_LOGIN = "nonlogin"
    }
}