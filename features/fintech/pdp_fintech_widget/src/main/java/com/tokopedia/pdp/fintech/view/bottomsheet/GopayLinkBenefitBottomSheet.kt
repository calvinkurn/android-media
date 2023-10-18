package com.tokopedia.pdp.fintech.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pdp.fintech.adapter.GopayLinkBenefitAdapter
import com.tokopedia.pdp.fintech.analytics.FintechWidgetAnalyticsEvent
import com.tokopedia.pdp.fintech.domain.datamodel.ActivationBottomSheetDescriptions
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.view.activity.ActivationBottomSheetActivity
import com.tokopedia.pdp_fintech.R
import com.tokopedia.pdp_fintech.databinding.BottomSheetPdpWidgetGopayActivationBinding
import com.tokopedia.pdp_fintech.databinding.PdpFintechWidgetLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.resources.isDarkMode
import javax.inject.Inject

class GopayLinkBenefitBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var userSession: UserSessionInterface



    private var activationBottomSheetDetail: FintechRedirectionWidgetDataClass? = null
    private var webUrl: String? = null
    private var arrayOfFeatures: ArrayList<ActivationBottomSheetDescriptions> = ArrayList()
    private lateinit var gopayLinkBenefitAdapter: GopayLinkBenefitAdapter

    private lateinit var binding: BottomSheetPdpWidgetGopayActivationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initArgument()
        this.isDragable = true
        this.isHideable = true
        customPeekHeight = (getScreenHeight()).toDp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetPdpWidgetGopayActivationBinding.inflate(inflater,container,false)
        setChild(binding.root)
        return  super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            gopayLinkBenefitAdapter = GopayLinkBenefitAdapter(arrayOfFeatures, it)
        }
        binding.activationBenifitRecycler.adapter = gopayLinkBenefitAdapter
        binding.activationBenifitRecycler.layoutManager = LinearLayoutManager(context)
        setListener()
        setData()
    }


    private fun setListener() {
        binding.btnRegister.setOnClickListener {
            sendClickEvent()
            openRouteView(webUrl)
        }
    }

    private fun sendClickEvent() {
        activity?.let {
            (it as ActivationBottomSheetActivity).sendAnalytic(
                if (!activationBottomSheetDetail?.widgetBottomSheet?.buttons?.get(0)?.buttonText.isNullOrBlank())
                    FintechWidgetAnalyticsEvent.ActivationBottomSheetClick(
                        activationBottomSheetDetail?.userStatus ?: "",
                        activationBottomSheetDetail?.linkingStatus ?: "",
                        activationBottomSheetDetail?.gatewayCode ?: "",
                        activationBottomSheetDetail?.widgetBottomSheet?.buttons?.get(0)?.buttonText
                            ?: ""
                    )
                else
                    FintechWidgetAnalyticsEvent.ActivationBottomSheetClick(
                        activationBottomSheetDetail?.userStatus ?: "",
                        activationBottomSheetDetail?.linkingStatus ?: "",
                        activationBottomSheetDetail?.gatewayCode ?: "",
                        ""
                    )
            )
        }

    }


    private fun openRouteView(androidUrl: String?) {

        val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + androidUrl
        RouteManager.route(context, webViewAppLink)


    }

    private fun setData() {
        setHeaderIcon()
        setFooterIcon()
        binding.findyaFootNote.text = activationBottomSheetDetail?.widgetBottomSheet?.productFootnote
        binding.supervisedText.text = activationBottomSheetDetail?.widgetBottomSheet?.footnote
        binding.gopayActivationBottomSheet.text = activationBottomSheetDetail?.widgetBottomSheet?.title
        binding.btnRegister.text =
            activationBottomSheetDetail?.widgetBottomSheet?.buttons?.get(0)?.buttonText
        activationBottomSheetDetail?.widgetBottomSheet?.descriptions?.let { listOfBenefitDescription ->
            gopayLinkBenefitAdapter.updateData(
                listOfBenefitDescription
            )
        }

    }

    private fun setFooterIcon() {
        if (context?.isDarkMode() == true) {
            setlightMode()
        } else {
            setDarkMode()
        }
    }

    private fun setDarkMode() {
        activationBottomSheetDetail?.widgetBottomSheet?.productFootnoteIconLight?.let { findyaLightIcon ->
            binding.findayaIcon.setImageUrl(
                findyaLightIcon
            )
        }
        activationBottomSheetDetail?.widgetBottomSheet?.footnoteIconLight?.let { supervisedLightIcon ->
            binding.supervisedIcon.setImageUrl(
                supervisedLightIcon
            )
        }
    }

    private fun setlightMode() {
        activationBottomSheetDetail?.widgetBottomSheet?.productFootnoteIconDark?.let { findyaDarkIcon ->
            binding.findayaIcon.setImageUrl(
                findyaDarkIcon
            )
        }
        activationBottomSheetDetail?.widgetBottomSheet?.footnoteIconDark?.let { supervisedDarkIcon ->
            binding.supervisedIcon.setImageUrl(
                supervisedDarkIcon
            )
        }
    }

    private fun setHeaderIcon() {
        if (context?.isDarkMode() == true)
            activationBottomSheetDetail?.widgetBottomSheet?.productIconDark?.let {
                binding.imageDisplayer.setImageUrl(
                    it
                )
            }
        else
            activationBottomSheetDetail?.widgetBottomSheet?.productIconLight?.let {
                binding.imageDisplayer.setImageUrl(
                    it
                )
            }

    }


    private fun initArgument() {
        arguments?.let {
            activationBottomSheetDetail = it.getParcelable(ACTIVATION_BOTTOMSHEET_DETAIl)
            webUrl = it.getString(ACTIVATION_WEBVIEW_LINK)
        }
    }

    fun showBottomSheet(
        supportFragmentManager: FragmentManager,
        bundle: Bundle
    ): GopayLinkBenefitBottomSheet {
        val gopayLinkBenefitBottomSheet = GopayLinkBenefitBottomSheet()
        gopayLinkBenefitBottomSheet.arguments = bundle
        gopayLinkBenefitBottomSheet.show(supportFragmentManager, "GopayLinkBenefitBottomSheet")
        return gopayLinkBenefitBottomSheet


    }



    companion object {
        const val ACTIVATION_BOTTOMSHEET_DETAIl = "ActivationBottomSheetDetail"
        const val ACTIVATION_WEBVIEW_LINK = "ActivationWebViewLink"
    }
}
