package com.tokopedia.quest_widget.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.quest_widget.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.quest_widget.constants.QuestUserStatus
import com.tokopedia.quest_widget.data.Config
import com.tokopedia.quest_widget.data.Progress
import com.tokopedia.quest_widget.data.QuestWidgetListItem
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.Timer
import java.util.TimerTask

const val LAGITEXT = "x lagi "

class QuestWidgetItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): CardUnify(context, attrs), ProgressCompletionListener {

    private var showBox = false
    private var tvBannerTitle: Typography
    private var tvBannerDesc: Typography
    private var ivBannerIcon: ImageUnify
    private var ivBannerIconSecond: ImageUnify
    private var progressBar: QuestProgressBar
    private var progressBarContainer : FrameLayout
    private var iconContainer : ImageUnify

    init {

        View.inflate(context, R.layout.quest_widget_item_view, this)

        tvBannerTitle = findViewById(R.id.tv_banner_title)
        tvBannerDesc = findViewById(R.id.tv_banner_desc)
        ivBannerIcon = findViewById(R.id.iv_banner_icon)
        ivBannerIconSecond = findViewById(R.id.iv_banner_icon_second)
        progressBar = findViewById(R.id.progressBar)
        progressBarContainer = findViewById(R.id.progressBarContainer)
        iconContainer = findViewById(R.id.iconContainer)
        progressBar.setProgressCompletionListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun setData(item: QuestWidgetListItem, config: Config) {

        this.setOnClickListener {
            RouteManager.route(context, item.actionButton?.cta?.url)
        }

        tvBannerTitle.text = config.banner_title
        ivBannerIcon.loadImage(config.banner_icon_url)
        ivBannerIconSecond.loadImage(item.prize?.get(0)?.iconUrl)
        val progress = calculateProgress((item.task?.get(0)?.progress))

        when(item.questUser?.status){
            QuestUserStatus.ON_PROGRESS ->{
                scaleDownIcon(ivBannerIcon, iconContainer, false) { setProgressBarvalue(progress) }
                item.questUser.status = QuestUserStatus.ANIMATED
            }
            QuestUserStatus.COMPLETED -> {
                scaleDownIcon(ivBannerIcon, iconContainer, true) { setProgressBarvalue(progress) }
                scaleUpIcon(ivBannerIcon , iconContainer, true)
                item.questUser.status = QuestUserStatus.ANIMATED
            }
            QuestUserStatus.CLAIMED -> {

            }
            QuestUserStatus.IDLE -> {
                startTranslationAnimation()
                item.questUser.status = QuestUserStatus.ANIMATED
            }
            else->{
            }
        }
        val desc = item.actionButton?.shortText + " " + (item.task?.get(0)?.progress?.current?.let {
            item.task[0]?.progress?.target?.minus(
                it
            )
        })

        tvBannerDesc.text =
            desc + LAGITEXT + context.resources.getString(R.string.str_dot) + " " + item.label?.title

    }

    private fun calculateProgress(progress:Progress?):Float{
        val start:Float = progress?.current?.toFloat()?:0F
        val target:Float = progress?.target?.toFloat()?:1F

       return 100 - (((target-start) / target) * 100)
    }

    private fun setProgressBarvalue(progress: Float) {
        iconContainer.hide()
        progressBar.show()
        progressBar.apply {
            setProgress(progress)
            setProgressColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
            setRounded(true)
            setProgressWidth(8F)
            setProgressBackgroundColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N75
                )
            )
        }
    }


    @SuppressLint("Recycle")
    private fun scaleUpIcon(
        icon: ImageUnify, iconContainer: ImageUnify, completed: Boolean,
        completion: (() -> Unit)? = null
    ) {
        progressBar.hide()
        icon.show()
        iconContainer.show()
        val animator = AnimatorSet()

        val animatorContainerY = ObjectAnimator.ofFloat(iconContainer, View.SCALE_Y, this.scaleY, 1.05F)
        val animatorContainerX = ObjectAnimator.ofFloat(iconContainer ,View.SCALE_X, this.scaleX, 1.05F)
        animatorContainerY.duration = 2000
        animatorContainerX.duration = 2000
        val animatorIconY = ObjectAnimator.ofFloat(icon, View.SCALE_Y, this.scaleY, 1.05F)
        val animatorIconX = ObjectAnimator.ofFloat(icon ,View.SCALE_X, this.scaleX, 1.05F)
        animatorIconY.duration = 2000
        animatorIconX.duration = 2000

        animator.playTogether(animatorContainerY,animatorContainerX,animatorIconY,animatorIconX)
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {
            }
            override fun onAnimationEnd(p0: Animator?) {
                completion?.let {
                    it()
                }
                if(completed){}

            }
            override fun onAnimationCancel(p0: Animator?) {
            }
            override fun onAnimationRepeat(p0: Animator?) {
            }

        })
        animator.start()
    }

    @SuppressLint("Recycle")
    private fun scaleDownIcon(
        icon: ImageUnify, iconContainer: ImageUnify, progress: Boolean,
        completion: (() -> Unit)? = null
    ) {
        icon.show()
        iconContainer.show()
        val animator = AnimatorSet()

        val animatorContainerY = ObjectAnimator.ofFloat(iconContainer, View.SCALE_Y, this.scaleY, 1.05F)
        val animatorContainerX = ObjectAnimator.ofFloat(iconContainer ,View.SCALE_X, this.scaleX, 1.05F)
        animatorContainerY.duration = 2000
        animatorContainerX.duration = 2000
        val animatorIconY = ObjectAnimator.ofFloat(icon, View.SCALE_Y, this.scaleY, 1.05F)
        val animatorIconX = ObjectAnimator.ofFloat(icon ,View.SCALE_X, this.scaleX, 1.05F)
        animatorIconY.duration = 2000
        animatorIconX.duration = 2000

        animator.playTogether(animatorIconY,animatorContainerX,animatorIconY,animatorIconX)
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {
            }
            override fun onAnimationEnd(p0: Animator?) {
                scaleUpIcon(icon,iconContainer, progress, completion)
            }
            override fun onAnimationCancel(p0: Animator?) {
            }
            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
        animator.start()
    }

    private fun translationAnimation(viewOne: ImageUnify, viewTwo: ImageUnify){

        val animator = AnimatorSet()
        val alphaAnimPropOne = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
        val alphaAnimObjOne: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(viewOne, alphaAnimPropOne)

        val animationCenterToBottom = ObjectAnimator.ofFloat(viewOne, View.TRANSLATION_Y, 0f, dpToPx(56))

        val alphaAnimPropTwo = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnimObjTwo: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(viewTwo, alphaAnimPropTwo)

        val animationTopToCenter = ObjectAnimator.ofFloat(viewTwo, View.TRANSLATION_Y, -dpToPx(56), 0f)
        animationCenterToBottom.duration = 600
        animationTopToCenter.duration = 600
        animator.playTogether(alphaAnimObjOne, animationCenterToBottom, alphaAnimObjTwo, animationTopToCenter)
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
        animator.start()
    }

    private fun startTranslationAnimation(){
        val START_DELAY = 3000L
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                ivBannerIcon.post {
                    if (showBox) {
                        showBox = false
                        translationAnimation(ivBannerIconSecond, ivBannerIcon)
                        timer.cancel()
                    }
                    else{
                        showBox = true
                        translationAnimation(ivBannerIcon, ivBannerIconSecond)
                    }
                }
            }
        }, START_DELAY, START_DELAY)

    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    override fun showCompletionAnimation() {
        iconContainer.show()
        translationAnimation(ivBannerIcon, ivBannerIconSecond)
    }
}

