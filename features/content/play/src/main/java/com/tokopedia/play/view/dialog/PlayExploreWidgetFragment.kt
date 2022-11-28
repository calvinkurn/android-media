package com.tokopedia.play.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.databinding.FragmentPlayExploreWidgetBinding
import javax.inject.Inject
import kotlin.math.roundToInt
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 24/11/22
 */
class PlayExploreWidgetFragment @Inject constructor() : DialogFragment() {

    private var _binding: FragmentPlayExploreWidgetBinding? = null
    private val binding: FragmentPlayExploreWidgetBinding get() = _binding!!

    private val EXPLORE_WIDTH: Int by lazy {
        (getScreenWidth() * 0.85).roundToInt()
    }

    private val EXPLORE_HEIGHT: Int by lazy {
        (getScreenHeight() * 0.95).roundToInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayExploreWidgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window ?: return
        window.setGravity(Gravity.END)
        window.setLayout(
            EXPLORE_WIDTH,
            EXPLORE_HEIGHT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setWindowAnimations(playR.style.ExploreWidgetWindowAnim)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showNow(manager: FragmentManager) {
        if (!isAdded) showNow(manager, TAG)
    }

    companion object {
        private const val TAG = "InteractiveDialogFragment"

        fun get(fragmentManager: FragmentManager): PlayExploreWidgetFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? PlayExploreWidgetFragment
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayExploreWidgetFragment {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayExploreWidgetFragment::class.java.name
            ) as PlayExploreWidgetFragment
        }
    }
}
