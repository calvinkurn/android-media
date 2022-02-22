package com.tokopedia.pdp.fintech.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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


    private var parentView: View? = null
    private val childLayoutRes = R.layout.bottom_sheet_pdp_widget_gopay_activation
    private var activationBottomSheetDetail: FintechRedirectionWidgetDataClass? = null
    private lateinit var headerIcon: ImageUnify
    private lateinit var recyclerBenifits: RecyclerView
    private lateinit var bottomsheetTitle: Typography
    private lateinit var proceedButton: UnifyButton
    private lateinit var findyaText: Typography
    private lateinit var findyaIcon: ImageUnify
    private lateinit var supervisedText: Typography
    private lateinit var supervisedIcon: ImageUnify
    private var webUrl: String? = null
    private var arrayOfFeatures: ArrayList<ActivationBottomSheetDescriptions> = ArrayList()
    private lateinit var gopayLinkBenefitAdapter: GopayLinkBenefitAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initArgument()
        this.isDragable = true
        this.isHideable = true
        customPeekHeight = (getScreenHeight()).toDp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            gopayLinkBenefitAdapter = GopayLinkBenefitAdapter(arrayOfFeatures, it)
        }

        headerIcon = view.findViewById(R.id.imageDisplayer)
        bottomsheetTitle = view.findViewById(R.id.gopayActivationBottomSheet)
        recyclerBenifits = view.findViewById(R.id.activationBenifitRecycler)
        proceedButton = view.findViewById(R.id.btnRegister)
        findyaText = view.findViewById(R.id.findyaFootNote)
        findyaIcon = view.findViewById(R.id.findayaIcon)
        supervisedText = view.findViewById(R.id.supervisedText)
        supervisedIcon = view.findViewById(R.id.supervisedIcon)
        recyclerBenifits.adapter = gopayLinkBenefitAdapter
        recyclerBenifits.layoutManager = LinearLayoutManager(context)
        setListener()
        setData()
    }


    private fun setListener() {
        proceedButton.setOnClickListener {
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
                        activationBottomSheetDetail?.gatewayPartnerName ?: "",
                        activationBottomSheetDetail?.widgetBottomSheet?.buttons?.get(0)?.buttonText
                            ?: ""
                    )
                else
                    FintechWidgetAnalyticsEvent.ActivationBottomSheetClick(
                        activationBottomSheetDetail?.userStatus ?: "",
                        activationBottomSheetDetail?.linkingStatus ?: "",
                        activationBottomSheetDetail?.gatewayPartnerName ?: "",
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
        findyaText.text = activationBottomSheetDetail?.widgetBottomSheet?.productFootnote
        supervisedText.text = activationBottomSheetDetail?.widgetBottomSheet?.footnote
        bottomsheetTitle.text = activationBottomSheetDetail?.widgetBottomSheet?.title
        proceedButton.text =
            activationBottomSheetDetail?.widgetBottomSheet?.buttons?.get(0)?.buttonText
        activationBottomSheetDetail?.widgetBottomSheet?.descriptions?.let { listOfBenefitDescription ->
            gopayLinkBenefitAdapter.updateData(
                listOfBenefitDescription
            )
        }

    }

    private fun setFooterIcon() {
        if (context?.isDarkMode() == true) {
            activationBottomSheetDetail?.widgetBottomSheet?.productFootnoteIconDark?.let { findyaDarkIcon ->
                findyaIcon.setImageUrl(
                    findyaDarkIcon
                )
            }
            activationBottomSheetDetail?.widgetBottomSheet?.footnoteIconDark?.let { supervisedDarkIcon ->
                supervisedIcon.setImageUrl(
                    supervisedDarkIcon
                )
            }
        } else {
            activationBottomSheetDetail?.widgetBottomSheet?.productFootnoteIconLight?.let { findyaLightIcon ->
                findyaIcon.setImageUrl(
                    findyaLightIcon
                )
            }
            activationBottomSheetDetail?.widgetBottomSheet?.footnoteIconLight?.let { supervisedLightIcon ->
                supervisedIcon.setImageUrl(
                    supervisedLightIcon
                )
            }
        }
    }

    private fun setHeaderIcon() {
        if (context?.isDarkMode() == true)
            activationBottomSheetDetail?.widgetBottomSheet?.productIconDark?.let {
                headerIcon.setImageUrl(
                    it
                )
            }
        else
            activationBottomSheetDetail?.widgetBottomSheet?.productIconLight?.let {
                headerIcon.setImageUrl(
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

    private fun initView() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)

    }


    companion object {
        const val ACTIVATION_BOTTOMSHEET_DETAIl = "ActivationBottomSheetDetail"
        const val ACTIVATION_WEBVIEW_LINK = "ActivationWebViewLink"
    }
}