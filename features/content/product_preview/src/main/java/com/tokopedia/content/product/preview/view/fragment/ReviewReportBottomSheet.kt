package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.product.preview.view.components.ReportScreen
import com.tokopedia.content.product.preview.view.components.listOfReport
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by astidhiyaa on 28/11/23
 */
class ReviewReportBottomSheet : BottomSheetUnify() {
    private var mListener: Listener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                LaunchedEffect(Unit) {
                    //event ->
                }
                ReportScreen(reports = listOfReport, onSubmit = {})
            }
        }
        setChild(composeView)
        setTitle("Laporkan ulasan")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fg: FragmentManager) {
        if (isAdded) return
        super.show(fg, TAG)
    }

    override fun dismiss() {
        if (!isAdded) return
        super.dismiss()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mListener = null
    }

    interface Listener {
        fun onReasonClicked(report: ReportUiModel) //TODO: add onSubmit
    }

    companion object {
        const val TAG = "ReviewReportBottomSheet"

        fun get(fgManager: FragmentManager): ReviewReportBottomSheet? {
            return fgManager.findFragmentByTag(TAG) as? ReviewReportBottomSheet
        }

        fun getOrCreate(
            fgManager: FragmentManager,
            classLoader: ClassLoader,
        ): ReviewReportBottomSheet {
            return get(fgManager) ?: fgManager.fragmentFactory.instantiate(
                classLoader,
                ReviewReportBottomSheet::class.java.name
            ) as ReviewReportBottomSheet
        }
    }
}
