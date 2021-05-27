package com.tokopedia.saldodetails.view.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse
import com.tokopedia.saldodetails.view.activity.SaldoWebViewActivity
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.Companion.BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.Companion.BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject


class MerchantCreditDetailFragment : BaseDaggerFragment() {

    private var merchantCreditDetails: GqlMerchantCreditResponse? = null

    private var mclLogoIV: ImageView? = null
    private var mclTitleTV: TextView? = null
    private var mclActionItemTV: TextView? = null
    private var mclDescTV: TextView? = null
    private var mclInfoListLL: LinearLayout? = null
    private var mclBoxLayout: RelativeLayout? = null
    private var mclboxTitleTV: TextView? = null
    private var mclBoxDescTV: TextView? = null
    private var mclParentCardView: CardView? = null

    private var mclBlockedStatusTV: TextView? = null
    private var saveInstanceCacheManager: SaveInstanceCacheManager? = null

    @Inject
    lateinit var saldoDetailsAnalytics: SaldoDetailsAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_merchant_credit_details, container, false)
        val bundle = arguments
        val saveInstanceCachemanagerId = bundle?.getString(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID)
                ?: ""
        saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), saveInstanceCachemanagerId)
        merchantCreditDetails = saveInstanceCacheManager!!.get<GqlMerchantCreditResponse>(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS, GqlMerchantCreditResponse::class.java)
        initViews(view)
        if (merchantCreditDetails != null) {
            saldoDetailsAnalytics.eventMCLImpression(merchantCreditDetails!!.status.toString())
        }
        return view
    }

    private fun initViews(view: View) {
        mclLogoIV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_logo)
        mclTitleTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_title)
        mclActionItemTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_action_item)
        mclDescTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_description)
        mclInfoListLL = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_info_list)
        mclBoxLayout = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_box_layout)
        mclboxTitleTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_box_title)
        mclBoxDescTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_box_Desc)
        mclBlockedStatusTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_blocked_status)
        mclParentCardView = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_card_view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
    }

    private fun populateData() {
        if (merchantCreditDetails != null) {

            if (!TextUtils.isEmpty(merchantCreditDetails!!.logoURL)) {
                mclLogoIV!!.show()
                ImageHandler.loadImage(context, mclLogoIV, merchantCreditDetails!!.logoURL, com.tokopedia.saldodetails.R.drawable.saldo_ic_modal_toko)
            } else {
                mclLogoIV!!.setImageDrawable(resources.getDrawable(com.tokopedia.saldodetails.R.drawable.saldo_ic_modal_toko))
            }

            if (!TextUtils.isEmpty(merchantCreditDetails!!.title)) {
                mclTitleTV!!.text = Html.fromHtml(merchantCreditDetails!!.title)
            } else {
                mclTitleTV!!.text = getString(com.tokopedia.saldodetails.R.string.modal_toko)
            }

            if (merchantCreditDetails!!.anchorList != null) {
                populateAnchorListData()
            } else {
                mclActionItemTV!!.gone()
            }

            if (!TextUtils.isEmpty(merchantCreditDetails!!.bodyDesc)) {
                mclDescTV!!.text = merchantCreditDetails!!.bodyDesc
                mclDescTV!!.show()
            } else {
                mclDescTV!!.gone()
            }

            if (merchantCreditDetails!!.infoList != null && merchantCreditDetails!!.infoList.size > 0) {
                mclInfoListLL!!.show()
                populateInfolistData()
            } else {
                mclInfoListLL!!.gone()
            }

            if (merchantCreditDetails!!.isShowBox && merchantCreditDetails!!.boxInfo != null) {
                mclBoxLayout!!.show()
                populateBoxData()
            } else {
                mclBoxLayout!!.gone()
            }

            mclParentCardView!!.setOnClickListener { v ->

                saldoDetailsAnalytics.eventMCLCardCLick(merchantCreditDetails!!.status.toString())
                if (!TextUtils.isEmpty(merchantCreditDetails!!.mainRedirectUrl)) {
                    startWebView(merchantCreditDetails!!.mainRedirectUrl)
                }
            }
        }
    }

    private fun populateBoxData() {

        val drawable = mclBoxLayout!!.background
        drawable.setColorFilter(Color.parseColor(merchantCreditDetails!!.boxInfo!!.boxBgColor), PorterDuff.Mode.SRC_ATOP)
        mclBoxLayout!!.background = drawable

        if (!TextUtils.isEmpty(merchantCreditDetails!!.boxInfo!!.boxTitle)) {
            mclboxTitleTV!!.show()
            mclboxTitleTV!!.text = merchantCreditDetails!!.boxInfo!!.boxTitle
        } else {
            mclboxTitleTV!!.gone()
        }

        if (!TextUtils.isEmpty(merchantCreditDetails!!.boxInfo!!.boxDesc)) {
            mclBoxDescTV!!.show()
            var descText = merchantCreditDetails!!.boxInfo!!.boxDesc
            if (!TextUtils.isEmpty(merchantCreditDetails!!.boxInfo!!.linkText)) {
                val linkText = merchantCreditDetails!!.boxInfo!!.linkText
                descText += " " + linkText!!

                val spannableString = SpannableString(descText)
                val startIndexOfLink = descText!!.indexOf(linkText)
                if (startIndexOfLink != -1) {
                    spannableString.setSpan(object : ClickableSpan() {
                        override fun onClick(view: View) {
                            if (!TextUtils.isEmpty(merchantCreditDetails!!.boxInfo!!.linkUrl)) {
                                startWebView(merchantCreditDetails!!.boxInfo!!.linkUrl)
                            }
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = false
                            try {
                                ds.color = Color.parseColor(merchantCreditDetails!!.boxInfo!!.linkTextColor)
                            } catch (e: Exception) {
                                ds.color = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
                            }

                        }
                    }, startIndexOfLink, startIndexOfLink + linkText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    mclBoxDescTV!!.movementMethod = LinkMovementMethod.getInstance()
                    mclBoxDescTV!!.text = spannableString
                } else {
                    mclBoxDescTV!!.text = Html.fromHtml(descText)
                }

            } else {
                mclBoxDescTV!!.text = Html.fromHtml(descText)
            }

        } else {
            mclBoxDescTV!!.gone()
        }
    }

    private fun startWebView(linkUrl: String?) {
        context?.let { context -> startActivity(SaldoWebViewActivity.getWebViewIntent(context, linkUrl)) }
    }

    private fun populateInfolistData() {
        val layoutInflater = layoutInflater
        mclInfoListLL!!.removeAllViews()

        for (infoList1 in merchantCreditDetails!!.infoList) {

            val view = layoutInflater.inflate(com.tokopedia.saldodetails.R.layout.layout_info_list, null)
            val infoLabel = view.findViewById<TextView>(com.tokopedia.saldodetails.R.id.info_label_text_view)
            val infoValue = view.findViewById<TextView>(com.tokopedia.saldodetails.R.id.info_value_text_view)

            infoLabel.text = infoList1.label
            infoValue.text = infoList1.value

            mclInfoListLL!!.addView(view)
        }
    }

    private fun populateAnchorListData() {
        val gqlAnchorListResponse = merchantCreditDetails!!.anchorList
        if (gqlAnchorListResponse != null) {
            mclActionItemTV!!.text = gqlAnchorListResponse.label
            try {
                mclActionItemTV!!.setTextColor(Color.parseColor(gqlAnchorListResponse.color))
            } catch (e: Exception) {
                mclActionItemTV!!.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400))
            }

            mclActionItemTV!!.setOnClickListener { v ->

                saldoDetailsAnalytics.eventMCLActionItemClick(mclActionItemTV!!.text.toString(),
                        merchantCreditDetails!!.status.toString())

                if (gqlAnchorListResponse.isShowDialog && gqlAnchorListResponse.dialogInfo != null) {
                    showCloseableBottomSheet(gqlAnchorListResponse.dialogInfo!!.dialogTitle, gqlAnchorListResponse.dialogInfo!!.dialogBody)
                } else {
                    if (!TextUtils.isEmpty(gqlAnchorListResponse.link)) {
                        startWebView(gqlAnchorListResponse.link)
                    }
                }
            }
        }
        mclActionItemTV!!.show()
    }

    private fun showCloseableBottomSheet(dialogTitle: String?, dialogBody: String?) {
        val closeableBottomSheetDialog = BottomSheetUnify()
        val childView = layoutInflater.inflate(com.tokopedia.saldodetails.R.layout.mcl_bottom_dialog, null)
        childFragmentManager.let {
            closeableBottomSheetDialog.apply {
                showCloseIcon = true
                setChild(childView)
                setTitle(dialogTitle ?: "")
                show(it, null)
            }
        }
        childView.findViewById<Typography>(com.tokopedia.saldodetails.R.id.mcl_bottom_sheet_desc).text = dialogBody
        closeableBottomSheetDialog.setCloseClickListener {
            closeableBottomSheetDialog.dismiss()
        }
    }

    override fun initInjector() {
        activity?.let {
            val saldoDetailsComponent = SaldoDetailsComponentInstance.getComponent(it)
            saldoDetailsComponent.inject(this)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {

        fun newInstance(bundle: Bundle): Fragment {
            val fragment = MerchantCreditDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
