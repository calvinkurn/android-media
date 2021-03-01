package com.tokopedia.hotel.search_map.presentation.fragment

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.ID_ANDROID_CONTENT
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search_map.di.HotelSearchMapComponent
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity.Companion.SEARCH_SCREEN_NAME
import kotlinx.android.synthetic.main.fragment_hotel_search_map.*
import kotlin.math.abs

/**
 * @author by furqan on 01/03/2021
 */
class HotelSearchMapFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun initInjector() {
        getComponent(HotelSearchMapComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_search_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupContentPeekSize()
    }

    private fun setupContentPeekSize() {
        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            try {
                display.getRealSize(size)
            } catch (error: NoSuchMethodError) {
                display.getSize(size)
            }

            val height = size.y
            val titleBarHeight = getTitleBarHeight(it)

            val tmpHeight = height - abs(titleBarHeight) - getSoftButtonsBarHeight()

            val layoutParams = CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tmpHeight)
            layoutParams.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
            layoutParams.parallaxMultiplier = 0.6f
            toolbarConstraintContainer.layoutParams = layoutParams
            toolbarConstraintContainer.requestLayout()
        }
    }

    private fun getTitleBarHeight(fragmentActivity: FragmentActivity): Int {
        val rect = Rect()
        val window = fragmentActivity.window
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = rect.top
        val contentViewTop = window.findViewById<View>(ID_ANDROID_CONTENT).top
        return contentViewTop - statusBarHeight
    }

    @SuppressLint("NewApi")
    private fun getSoftButtonsBarHeight(): Int {
        val metrics = DisplayMetrics()
        activity?.let {
            it.windowManager.defaultDisplay.getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            it.windowManager.defaultDisplay.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight) {
                realHeight - usableHeight
            } else {
                0
            }
        }
        return 0
    }

    companion object {
        const val ARG_HOTEL_SEARCH_MODEL = "arg_hotel_search_model"
        private const val ARG_FILTER_PARAM = "arg_hotel_filter_param"

        fun createInstance(hotelSearchModel: HotelSearchModel, selectedParam: ParamFilterV2): HotelSearchMapFragment =
                HotelSearchMapFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
                        putParcelable(ARG_FILTER_PARAM, selectedParam)
                    }
                }
    }
}