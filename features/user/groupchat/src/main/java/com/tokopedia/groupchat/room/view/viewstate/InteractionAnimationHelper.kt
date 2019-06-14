package com.tokopedia.groupchat.room.view.viewstate

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.animation.Particle
import com.tokopedia.groupchat.animation.ParticleSystem
import com.tokopedia.groupchat.animation.modifiers.MovementModifier
import com.tokopedia.groupchat.animation.modifiers.ScaleModifier
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 28/05/19
 */
class InteractionAnimationHelper(private var interactionGuideline: FrameLayout) {

    private lateinit var interactionSubscription: Subscription

    private val interactionDefaultIconIdList = PlayFragment.DEFAULT_ICON_LIST
    private val interactionDefaultIconList: ArrayList<Drawable> = arrayListOf()
    private val interactionIconList = arrayListOf<Drawable>()

    private var defaultParticle = arrayListOf<Particle>()

    init {
        for (id in interactionDefaultIconIdList) {
            interactionDefaultIconList.add(MethodChecker.getDrawable(interactionGuideline.context, id))
        }
    }

    fun shootInteractionButton(anchorView: View, numParticles: Int) {
        val interactionDirection = Random()
        try {
            interactionDefaultIconList.shuffle()
            ParticleSystem(interactionGuideline, getIconInteraction(), 2500)
                    .setSpeedModuleAndAngleRange(0.1f, 0.2f, 270, 270)
                    .setFadeOut(1500)
                    .addModifier(ScaleModifier(0.7f, 1.0f, 1200, 1500))
                    .addModifier(MovementModifier(2f, interactionDirection.nextBoolean()))
                    .oneShot(anchorView, numParticles)
        } catch (e: Exception) {
            if (GlobalConfig.isAllowDebuggingTools()) {
                Log.e(this.toString(), e.message)
            }
            e.printStackTrace()
        }
    }

    private fun getIconInteraction(): ArrayList<Drawable> {
        if(interactionIconList.size > 0){
            return interactionIconList
        }
        return interactionDefaultIconList
    }


    fun fakeShot(anchorView: View) {
        val time = Random().nextInt(4) + 2
        val numParticles = Random().nextInt(4) + 1
        destroy()
        interactionSubscription = Observable.timer(time.toLong(), TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    shootInteractionButton(anchorView, numParticles)
                    fakeShot(anchorView)
                }
    }

    fun destroy() {
        if (::interactionSubscription.isInitialized) {
            interactionSubscription?.unsubscribe()
        }
    }


    private fun getRandomHeart(iconList: ArrayList<Drawable>): Drawable? {
        when {
            iconList.isNotEmpty() -> return iconList.shuffled().take(1)[0]
            else -> {
                val temp = interactionDefaultIconIdList.shuffled().take(1)[0]
                return MethodChecker.getDrawable(interactionGuideline.context, temp)
            }
        }
    }

    fun updateInteractionIcon(balloonList: ArrayList<String>) {
        interactionIconList.clear()
        if(balloonList.isEmpty()){
            return
        }

        interactionGuideline.context?.let {
            for (s in balloonList) {
                ImageHandler.cacheFromUrl(it, s, interactionIconList)
            }
        }
    }
}