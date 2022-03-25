package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokomember_seller_dashboard.R

class TokomemberDashIntroFragment : BaseDaggerFragment(){

    private var rootView: RelativeLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView  = view?.findViewById(R.id.rootView)
        hideStatusBar()
    }
    override fun getScreenName() =""

    override fun initInjector() {

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun hideStatusBar() {
        rootView?.fitsSystemWindows = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            rootView?.requestApplyInsets()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = rootView?.systemUiVisibility
            flags = flags?.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            if (flags != null) {
                rootView?.systemUiVisibility = flags
            }
            if (context != null) {
                activity?.window?.statusBarColor = androidx.core.content.ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
        }
        if (Build.VERSION.SDK_INT in 19..20) {
            activity?.let { setWindowFlag(it, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true) }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            activity?.let { setWindowFlag(it, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false) }
            activity?.window?.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(activity: Activity?, bits: Int, on: Boolean) {
        val win = activity?.window
        val winParams = win?.attributes
        if (on) {
            winParams?.flags = winParams?.flags?.or(bits)
        } else {
            winParams?.flags = winParams?.flags?.and(bits.inv())
        }
        win?.attributes = winParams
    }

    companion object{

        fun newInstance(): TokomemberDashIntroFragment {
            return TokomemberDashIntroFragment()
        }

    }
}