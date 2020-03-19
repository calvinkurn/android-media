package com.tokopedia.gamification.giftbox.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.helpers.doOnLayout
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer
import com.tokopedia.gamification.giftbox.presentation.views.StarsContainer
import com.tokopedia.unifycomponents.LoaderUnify

open class GiftBoxBaseFragment : Fragment() {

    lateinit var loader: LoaderUnify
    lateinit var viewFlipper: ViewFlipper
    lateinit var tvTapHint: AppCompatTextView
    lateinit var starsContainer: StarsContainer
    lateinit var rewardContainer: RewardContainer
    lateinit var giftBoxDailyView: GiftBoxDailyView
    lateinit var tvLoaderTitle: AppCompatTextView
    lateinit var tvLoaderMessage: AppCompatTextView

    val CONTAINER_LOADER = 0
    val CONTAINER_GIFT_BOX = 1

    var screenHeight = 0
    var screenWidth = 0

    val FADE_IN_DURATION = 500L

    open fun getLayout() = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = LayoutInflater.from(context).inflate(getLayout(), container, false)
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

        setInitialPositionOfViews()
        initialViewSetup()
    }

    open fun initialViewSetup() {
        loader.visibility = View.GONE
        giftBoxDailyView.alpha = 0f
        tvTapHint.alpha = 0f
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
        tvTapHint.translationY = screenHeight * 0.2f

        //todo Rahul later
        starsContainer.postDelayed({
            val array = IntArray(2)
            giftBoxDailyView.imageBoxFront.getLocationInWindow(array)
            val translationY = array[1].toFloat() - getStatusBarHeight(context) - dpToPx(40f)
            val translationX = array[0].toFloat()
            starsContainer.setStartPositionOfStars(starsContainer.width / 2f, translationY)

            rewardContainer.rvCoupons.translationY = array[1].toFloat() - (screenHeight * 0.15f) - dpToPx(148f)
        }, 300L)

        rewardContainer.doOnLayout {
            rewardContainer.setPositionOfViews(it.height.toFloat(), getStatusBarHeight(context).toFloat())
        }

    }

    fun showLoader() {
        viewFlipper.displayedChild = CONTAINER_LOADER
        loader.visibility = View.VISIBLE
    }

    fun hideLoader() {
        viewFlipper.displayedChild = CONTAINER_GIFT_BOX
        loader.visibility = View.GONE
    }

    //todo Rahul remove this method
    fun getStatusBarHeight(context: Context?): Int {
        context?.let {
            var result = (24 * it.resources.displayMetrics.density + 0.5f).toInt()
            val resourceId = it.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = it.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }
        return 0
    }

    //todo Rahul remove this method
    fun dpToPx(dp: Float): Float {
        context?.let {
            return dp * (it.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
        return 0f
    }

}