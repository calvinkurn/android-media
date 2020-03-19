package com.tokopedia.gamification.giftbox.presentation.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.presentation.activities.GiftLauncherActivity
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.ACTIVE
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.EMPTY
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.EXPIRED
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.INACTIVE
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.NON_LOGIN
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxDailyViewModel
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import com.tokopedia.gamification.giftbox.presentation.views.GiftPrizeLargeView
import com.tokopedia.gamification.giftbox.presentation.views.GiftPrizeSmallView
import com.tokopedia.gamification.pdp.data.LiveDataResult
import javax.inject.Inject

class GiftBoxDailyFragment : GiftBoxBaseFragment() {

    lateinit var tvBenefits: AppCompatTextView
    lateinit var llBenefits: LinearLayout
    lateinit var prizeViewSmallFirst: GiftPrizeSmallView
    lateinit var prizeViewSmallSecond: GiftPrizeSmallView
    lateinit var prizeViewLarge: GiftPrizeLargeView
    lateinit var llRewardMessage: LinearLayout

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GiftBoxDailyViewModel

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
//        v.postDelayed({
//            hideLoader()
////            dailyViewModel.renderActive()
//        }, 1000L)
        return v
    }

    override fun initViews(v: View) {
        tvBenefits = v.findViewById(R.id.tvBenefits)
        llBenefits = v.findViewById(R.id.ll_benefits)
        llRewardMessage = v.findViewById(R.id.ll_reward_message)
        super.initViews(v)

        setListeners()
    }

    private fun setListeners() {
        giftBoxDailyView.boxCallback = object : GiftBoxDailyView.BoxCallback {

            override fun onBoxOpenAnimationStart(startDelay: Long) {
                rewardContainer.setFinalTranslationOfCircles(giftBoxDailyView.fmGiftBox.top)
                val stageLightAnim = giftBoxDailyView.stageGlowAnimation()

                when (GiftLauncherActivity.uiType) {
                    GiftLauncherActivity.UiType.COUPON_POINTS -> {
                        val rewardAnim = rewardContainer.showCouponAndRewardAnimation(giftBoxDailyView.fmGiftBox.top)


                        val animatorSet = AnimatorSet()
                        animatorSet.playTogether(rewardAnim, stageLightAnim)
                        animatorSet.startDelay = startDelay
                        animatorSet.start()

                        val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimation()
                        ovoPointsTextAnim.startDelay = startDelay + 100L
                        ovoPointsTextAnim.start()
                    }
                    GiftLauncherActivity.UiType.POINTS -> {
                        val rewardAnim = rewardContainer.showSingleLargeRewardAnimation(giftBoxDailyView.fmGiftBox.top)

                        val animatorSet = AnimatorSet()
                        animatorSet.playTogether(stageLightAnim, rewardAnim)
                        animatorSet.startDelay = startDelay
                        animatorSet.start()
                    }
                }
            }

            override fun onBoxScaleDownAnimationStart() {
                fadeOutViews()
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
                    hideLoader()
                    if (it.data != null) {
                        val state = it.data.gamiLuckyHome.tokensUser.state
                        when (state) {
                            TokenUserState.ACTIVE -> {
                                renderGiftBoxActive(it.data)
                            }
                            TokenUserState.EMPTY -> {
                            }
                            TokenUserState.INACTIVE -> {
                            }
                            TokenUserState.EXPIRED -> {
                            }
                            TokenUserState.NON_LOGIN -> {
                            }
                            else -> {
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

        })
    }

    fun showRewardMessageDescription():Animator {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(llRewardMessage, alphaProp)
        alphaAnim.duration = 200L
        return alphaAnim
    }

    override fun initialViewSetup() {
        super.initialViewSetup()
        llBenefits.alpha = 0f
        tvBenefits.alpha = 0f
        llRewardMessage.alpha = 0f
    }

//    fun renderGiftBoxState(giftBoxState: GiftBoxState) {
//        when (giftBoxState) {
//            GiftBoxState.ACTIVE -> renderGiftBoxActive()
//            GiftBoxState.EMPTY -> renderGiftBoxEmpty()
//            GiftBoxState.ERROR -> renderGiftBoxError()
//            GiftBoxState.NO_INTERNET -> renderGiftBoxNoInternet()
//        }
//    }

    fun renderGiftBoxActive(entity: GiftBoxEntity) {
        tvTapHint.text = entity.gamiLuckyHome.tokensUser.title
        tvBenefits.text = entity.gamiLuckyHome.tokensUser.text

        //set prize list
        entity.gamiLuckyHome.prizeList?.forEach {
            if (it.isSpecial) {

            } else {

            }
        }

        fadeInActiveStateViews()
    }

    fun fadeOutViews() {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
        val prizeListContainerAnim = ObjectAnimator.ofPropertyValuesHolder(llBenefits, alphaProp)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapHintAnim, prizeListContainerAnim)
        animatorSet.duration = 300L

        animatorSet.start()
    }

    fun fadeInActiveStateViews() {
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