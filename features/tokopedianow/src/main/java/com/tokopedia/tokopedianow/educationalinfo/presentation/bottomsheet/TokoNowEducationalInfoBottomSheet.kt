package com.tokopedia.tokopedianow.educationalinfo.presentation.bottomsheet

import com.tokopedia.imageassets.TokopediaImageUrl
import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.EDU_BOTTOMSHEET_DURATION_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.EDU_BOTTOMSHEET_FAQ_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.EDU_BOTTOMSHEET_SK_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.EDU_BOTTOMSHEET_STOCK_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeRes
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowEducationalInformationBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoNowEducationalInfoBottomSheet :
    BottomSheetUnify(){

    companion object {
        private const val SOURCE_PLAY = "play"
        private const val BACKGROUND_BOTTOMSHEET = TokopediaImageUrl.BACKGROUND_BOTTOMSHEET
        private val TAG = TokoNowEducationalInfoBottomSheet::class.simpleName

        fun newInstance(): TokoNowEducationalInfoBottomSheet {
            return TokoNowEducationalInfoBottomSheet()
        }
    }

    var source: String? = null

    private var listener: EducationalInfoBottomSheetListener? = null

    private var binding by autoClearedNullable<BottomsheetTokopedianowEducationalInformationBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fm: FragmentManager, educationalInfoBottomSheetListener: EducationalInfoBottomSheetListener?) {
        show(fm, TAG)
        listener = educationalInfoBottomSheetListener
        listener?.impressUspBottomSheet()
    }

    private fun initView() {
        binding = BottomsheetTokopedianowEducationalInformationBinding.inflate(LayoutInflater.from(context))
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        isHideable = false
        setChild(binding?.root)
        setUi(binding)
        setTitle(getString(R.string.tokopedianow_home_educational_information_title))
    }

    private fun setUi(binding: BottomsheetTokopedianowEducationalInformationBinding?) {
        context?.let { context ->
            binding?.apply {
                val boldColor = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950).toString()

                setTimeText(boldColor)
                setStockText(boldColor)

                tpGuaranteedQuality.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_educational_information_guaranteed_quality_bottomsheet, boldColor))

                setTwentyFourHours(tpTwentyFourHours, context)
                setTermAndConditions(tpTermsAndConditions, context)

                ivBackgroundImage.loadImage(BACKGROUND_BOTTOMSHEET)

                setButton()
            }
        }
    }

    private fun setButton() {
        binding?.ubtnVisitNow?.shouldShowWithAction(
            shouldShow = source == SOURCE_PLAY,
            action = {
                binding?.ubtnVisitNow?.setOnClickListener {
                    RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
                    listener?.clickVisitNowBottomSheet()
                }
            }
        )
    }

    private fun setTimeText(boldColor: String) {
        getServiceTypeRes(EDU_BOTTOMSHEET_DURATION_RESOURCE_ID)?.let {
            binding?.tpTime?.text = MethodChecker.fromHtml(getString(it, boldColor))
        }
    }

    private fun setStockText(boldColor: String) {
        getServiceTypeRes(EDU_BOTTOMSHEET_STOCK_RESOURCE_ID)?.let {
            binding?.tpStockAvailable?.text = MethodChecker.fromHtml(getString(it, boldColor))
        }
    }

    private fun setTwentyFourHours(tpTwentyFourHours: Typography, context: Context) {
        getServiceTypeRes(EDU_BOTTOMSHEET_FAQ_RESOURCE_ID)?.let {
            convertStringToLink(tpTwentyFourHours, context, it, EDU_BOTTOMSHEET_FAQ_RESOURCE_ID)
        }
    }

    private fun setTermAndConditions(tpTermsAndConditions: Typography, context: Context) {
        getServiceTypeRes(EDU_BOTTOMSHEET_SK_RESOURCE_ID)?.let {
            convertStringToLink(tpTermsAndConditions, context, it, EDU_BOTTOMSHEET_SK_RESOURCE_ID)
        }
    }

    private fun convertStringToLink(typography: Typography, context: Context, stringRes: Int, keyRes: String) {
        val greenColor = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500).toString()
        val linkHelper = HtmlLinkHelper(context, getString(stringRes, greenColor))
        typography.text = linkHelper.spannedString
        typography.movementMethod = LinkMovementMethod.getInstance()
        linkHelper.urlList[0].let { link ->
            link.onClick = {
                goToInformationPage(link.linkUrl)
                listener?.clickVisitInformationPage(keyRes)
            }
        }
    }

    private fun goToInformationPage(linkUrl: String) {
        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
    }

    interface EducationalInfoBottomSheetListener {
        fun impressUspBottomSheet()
        fun clickVisitInformationPage(keyRes: String)
        fun clickVisitNowBottomSheet()
    }
}
