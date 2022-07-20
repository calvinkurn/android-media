package com.tokopedia.broadcaster.log.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.broadcaster.R
import com.tokopedia.broadcaster.data.uimodel.LoggerUIModel
import com.tokopedia.broadcaster.databinding.DialogBroadcasterChuckerDetailBinding
import com.tokopedia.utils.htmltags.HtmlUtil

class NetworkLogDetailSheet : BottomSheetDialogFragment() {

    private var binding: DialogBroadcasterChuckerDetailBinding? = null
    private lateinit var detailModel: LoggerUIModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DialogBroadcasterChuckerDetailBinding
            .inflate(inflater)
            .also {
                binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheetStyle()
        initParam()
        initView()
    }

    private fun initParam() {
        arguments?.getParcelable<LoggerUIModel>(BUNDLE_CHUCKER_LOG_KEY)?.let {
            detailModel = it
        }
    }

    private fun initView() {
        binding?.txtLog?.text = HtmlUtil.fromHtml("""
            <b>url</b>: ${detailModel.url}<br>
            <b>connectionId</b>: ${detailModel.connectionId}<br>
            <b>startTime</b>: ${detailModel.startTime}<br>
            <b>endTime</b>: ${detailModel.endTime}<br>
            <b>videoWidth</b>: ${detailModel.videoWidth}<br>
            <b>videoHeight</b>: ${detailModel.videoHeight}<br>
            <b>videoBitrate</b>: ${detailModel.videoBitrate}<br>
            <b>audioType</b>: ${detailModel.audioType}<br>
            <b>audioRate</b>: ${detailModel.audioRate}<br>
            <b>audioBitrate</b>: ${detailModel.audioBitrate}<br>
            <b>bitrateMode</b>: ${detailModel.bitrateMode}<br>
            <b>appVersion</b>: ${detailModel.appVersion}<br>
            <b>userId</b>: ${detailModel.userId}<br>
            <b>fps</b>: ${detailModel.fps}<br>
            <b>bandwidth</b>: ${detailModel.bandwidth}<br>
            <b>traffic</b>: ${detailModel.traffic}<br>
        """.trimIndent())
    }

    private fun initBottomSheetStyle() {
        view?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                val dialog = dialog as BottomSheetDialog
                val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val BUNDLE_CHUCKER_LOG_KEY = "chucker_log"

        fun create(fragmentManager: FragmentManager, model: LoggerUIModel) {
            val bundle = Bundle().apply {
                putParcelable(BUNDLE_CHUCKER_LOG_KEY, model)
            }

            NetworkLogDetailSheet().apply {
                arguments = bundle
            }.show(
                fragmentManager,
                NetworkLogDetailSheet::class.java.canonicalName
            )
        }
    }

}