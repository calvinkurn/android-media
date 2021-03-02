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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
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
class HotelSearchMapFragment : BaseDaggerFragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private lateinit var localCacheHandler: LocalCacheHandler

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun initInjector() {
        getComponent(HotelSearchMapComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            localCacheHandler = LocalCacheHandler(it, HOTEL_SEARCH_MAP_PREFERENCE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_search_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLocationMap()

        setupContentPeekSize(collapsingHeightSize = COLLAPSING_HALF_OF_SCREEN)
        showCoachMark()
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        setGoogleMap()
    }

    /**
     * Function to setup collapsing toolbar position
     *
     * use peekSize if you want to set the peekSize
     * use collapsingHeightSize if you want to set the collapsing toolbar by it's height multiplied by some number
     */
    private fun setupContentPeekSize(peekSize: Int = 0, collapsingHeightSize: Double = 1.0) {
        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            try {
                display.getRealSize(size)
            } catch (error: NoSuchMethodError) {
                display.getSize(size)
            }

            val height = size.y * collapsingHeightSize
            val titleBarHeight = getTitleBarHeight(it)

            val tmpHeight = height - peekSize - abs(titleBarHeight) - getSoftButtonsBarHeight()

            val layoutParams = CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tmpHeight.toInt())
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

    private fun setGoogleMap() {
        if (::googleMap.isInitialized) {
            googleMap.uiSettings.isMapToolbarEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isRotateGesturesEnabled = true
            googleMap.uiSettings.isScrollGesturesEnabled = true

            googleMap.setOnMapClickListener {
                // do nothing
            }
        }
    }

    private fun initLocationMap() {
        if (mapHotelSearchMap != null) {
            mapHotelSearchMap.onCreate(null)
            mapHotelSearchMap.onResume()
            mapHotelSearchMap.getMapAsync(this)
        }
        setGoogleMap()
    }

    private fun showCoachMark() {
        context?.let {
            if (!localCacheHandler.getBoolean(KEY_SEARCH_MAP_COACHMARK, false)) {
                val coachMarkItem = arrayListOf(
                        CoachMark2Item(
                                invisibleView,
                                getString(R.string.hotel_search_map_coach_mark_map_title),
                                getString(R.string.hotel_search_map_coach_mark_map_desc),
                                CoachMark2.POSITION_BOTTOM
                        ),
                        CoachMark2Item(
                                contentContainerHotelSearchMap,
                                getString(R.string.hotel_search_map_coach_mark_list_title),
                                getString(R.string.hotel_search_map_coach_mark_list_desc),
                                CoachMark2.POSITION_TOP
                        ),
                        CoachMark2Item(
                                headerHotelSearchMap, // need to change to filter view later
                                getString(R.string.hotel_search_map_coach_mark_filter_title),
                                getString(R.string.hotel_search_map_coach_mark_filter_desc),
                                CoachMark2.POSITION_BOTTOM
                        )
                )
                val coachmark = CoachMark2(it)
                coachmark.setStepListener(object : CoachMark2.OnStepListener {
                    override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                        if (currentIndex == COACHMARK_LIST_STEP_POSITION) {
                            setupContentPeekSize(collapsingHeightSize = COLLAPSING_NINE_TENTHS_OF_SCREEN)
                        } else if (currentIndex == COACHMARK_FILTER_STEP_POSITION) {
                            setupContentPeekSize(collapsingHeightSize = COLLAPSING_HALF_OF_SCREEN)
                        }
                    }

                })
                coachmark.onFinishListener = {
                    localCacheHandler.putBoolean(KEY_SEARCH_MAP_COACHMARK, true)
                }
                coachmark.showCoachMark(coachMarkItem, null, 0)
            }
        }
    }

    companion object {
        private const val HOTEL_SEARCH_MAP_PREFERENCE = "hotel_search_map_preference"
        private const val KEY_SEARCH_MAP_COACHMARK = "key_hotel_search_map_coachmark"
        private const val COACHMARK_LIST_STEP_POSITION = 1
        private const val COACHMARK_FILTER_STEP_POSITION = 2

        private const val COLLAPSING_HALF_OF_SCREEN = 1.0 / 2.0
        private const val COLLAPSING_NINE_TENTHS_OF_SCREEN = 9.0 / 10.0

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