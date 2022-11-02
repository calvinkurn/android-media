package com.tokopedia.content.common.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.databinding.BottomsheetWarningInfoBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.content.common.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by fachrizalmrsln at 01/09/2022
 */
class WarningInfoBottomSheet : BottomSheetUnify() {

    private var _binding: BottomsheetWarningInfoBinding? = null
    private val binding: BottomsheetWarningInfoBinding
        get() = _binding!!

    private var mListener: Listener? = null
    private var mWarningType: WarningType = WarningType.UNKNOWN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private fun setupBottomSheet() {
        _binding = BottomsheetWarningInfoBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)

        isDragable = false
        isSkipCollapseState = true
        isHideable = false
        isCancelable = false
        overlayClickDismiss = false

        setCloseClickListener {
            dismiss()
            mListener?.clickCloseIcon()
        }
    }

    private fun setupView() = with(binding) {
        when (mWarningType) {
            WarningType.BANNED -> {
                icIconWarning.setImage(newIconId = IconUnify.LOCK)
                tvWarningTitle.text = getString(R.string.ugc_warning_account_banned_title)
                tvWarningSubtitle.text = getString(R.string.ugc_warning_account_banned_subtitle)
                tvCta.apply {
                    show()
                    setOnClickListener { routeToWebViewGetToKnowMore() }
                }
            }
            WarningType.LIVE -> {
                icIconWarning.setImage(newIconId = IconUnify.WARNING)
                tvWarningTitle.text = getString(R.string.ugc_warning_both_account_live_title)
                tvWarningSubtitle.text = getString(R.string.ugc_warning_both_account_live_subtitle)
                tvCta.hide()
            }
            WarningType.UNKNOWN -> {}
        }
    }

    fun setData(warningType: WarningType): WarningInfoBottomSheet {
        mWarningType = warningType
        return this
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    private fun routeToWebViewGetToKnowMore() {
        RouteManager.route(
            requireContext(),
            getString(R.string.up_webview_template, ApplinkConst.WEBVIEW, getString(R.string.ugc_get_to_know_more_link))
        )
    }

    companion object {
        private const val TAG = "WarningInfoBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): WarningInfoBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? WarningInfoBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                WarningInfoBottomSheet::class.java.name
            ) as WarningInfoBottomSheet
        }
    }

    enum class WarningType {
        BANNED, LIVE, UNKNOWN
    }

    interface Listener {
        fun clickCloseIcon()
    }
}
