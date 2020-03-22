package com.tokopedia.gamification.giftbox.presentation.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.gamification.R
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
    lateinit var toolbar: Toolbar
    lateinit var imageToolbarIcon: View
    lateinit var tvToolbarTitle: AppCompatTextView

    val CONTAINER_LOADER = 1
    val CONTAINER_GIFT_BOX = 0

    var screenHeight = 0
    var screenWidth = 0

    val FADE_IN_DURATION = 500L
    var statusBarHeight: Int = 0

    open fun getLayout() = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
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
        toolbar = v.findViewById(R.id.toolbar)
        tvToolbarTitle = v.findViewById(R.id.tvToolbarTitle)
        imageToolbarIcon = v.findViewById(R.id.imageToolbarIcon)

        statusBarHeight = getStatusBarHeight(context)
        setInitialPositionOfViews()
        initialViewSetup()

        imageToolbarIcon.setOnClickListener {
            activity?.finish()
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
        tvTapHint.translationY = screenHeight * 0.2f
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gami_menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                try {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                } catch (ex: Exception) {

                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

}