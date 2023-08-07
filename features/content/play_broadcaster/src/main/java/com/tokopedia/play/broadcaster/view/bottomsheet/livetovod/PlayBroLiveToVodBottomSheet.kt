package com.tokopedia.play.broadcaster.view.bottomsheet.livetovod

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroDisableLiveToVodBinding
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetUiModel
import com.tokopedia.play.broadcaster.ui.model.livetovod.generateSpanText
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR

class PlayBroLiveToVodBottomSheet @Inject constructor(
    private val router: Router,
) : BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroDisableLiveToVodBinding? = null
    private val binding: BottomSheetPlayBroDisableLiveToVodBinding
        get() = _binding!!

    private var mData: TickerBottomSheetUiModel = TickerBottomSheetUiModel.Empty
    private var mListener: Listener? = null

    private fun generateInAppLink(appLink: String): String {
        return getString(
            com.tokopedia.content.common.R.string.up_webview_template,
            ApplinkConst.WEBVIEW,
            appLink,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(mData)
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroDisableLiveToVodBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)
        overlayClickDismiss = false
        showCloseIcon = false
        isCancelable = false
    }

    private fun initData(data: TickerBottomSheetUiModel) = with(binding) {
        val descriptionText = generateSpanText(
            fullText = data.mainText.first().description,
            action = data.mainText.first().action,
            color = ForegroundColorSpan(
                MethodChecker.getColor(
                    requireContext(),
                    unifyPrinciplesR.color.Unify_GN500
                )
            ),
            onTextCLick = { appLink ->
                router.route(
                    requireContext(),
                    generateInAppLink(appLink),
                )
            }
        )
        val bottomText = generateSpanText(
            fullText = data.bottomText.description,
            action = data.bottomText.action,
            color = ForegroundColorSpan(
                MethodChecker.getColor(
                    requireContext(),
                    unifyPrinciplesR.color.Unify_GN500
                )
            ),
            onTextCLick = { appLink ->
                router.route(
                    requireContext(),
                    generateInAppLink(appLink),
                )
            }
        )
        layoutDisableLiveToVodContent.apply {
            ivDisableLiveToVod.setImageUrl(url = data.imageURL)
            tvTitleDisableLiveToVod.text = data.mainText.first().title
            tvDescriptionDisableLiveToVod.apply {
                movementMethod = LinkMovementMethod.getInstance()
                text = descriptionText
            }
        }
        tvFooterDisableLiveToVod.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = bottomText
        }
        btnAction.setOnClickListener {
            mListener?.onButtonActionPressed()
            dismiss()
        }
    }

    fun setupData(data: TickerBottomSheetUiModel) {
        mData = data
    }

    fun setupListener(listener: Listener) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if (isAdded) return
        showNow(fragmentManager, TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mListener = null
        _binding = null
    }

    interface Listener {
        fun onButtonActionPressed()
    }

    companion object {
        private const val TAG = "PlayBroLiveToVodBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroLiveToVodBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? PlayBroLiveToVodBottomSheet
            return oldInstance ?: (fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroLiveToVodBottomSheet::class.java.name,
            ) as PlayBroLiveToVodBottomSheet)
        }
    }
}
