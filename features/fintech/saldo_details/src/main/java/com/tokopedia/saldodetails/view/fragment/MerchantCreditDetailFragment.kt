package com.tokopedia.saldodetails.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView

import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.response.model.GqlAnchorListResponse
import com.tokopedia.saldodetails.response.model.GqlInfoListResponse
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse
import com.tokopedia.saldodetails.view.activity.SaldoWebViewActivity
import com.tokopedia.cachemanager.SaveInstanceCacheManager

import javax.inject.Inject

import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID

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
    private var saveInstanceCachemanagerId: Int = 0

    @Inject
    lateinit var saldoDetailsAnalytics: SaldoDetailsAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_merchant_credit_details, container, false)
        val bundle = arguments
        saveInstanceCachemanagerId = bundle?.getInt(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID) ?: 0
        saveInstanceCacheManager = SaveInstanceCacheManager(context!!, saveInstanceCachemanagerId.toString())
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
                mclLogoIV!!.visibility = View.VISIBLE
                ImageHandler.loadImage(context, mclLogoIV, merchantCreditDetails!!.logoURL, com.tokopedia.design.R.drawable.ic_modal_toko)
            } else {
                mclLogoIV!!.setImageDrawable(resources.getDrawable(com.tokopedia.design.R.drawable.ic_modal_toko))
            }

            if (!TextUtils.isEmpty(merchantCreditDetails!!.title)) {
                mclTitleTV!!.text = Html.fromHtml(merchantCreditDetails!!.title)
            } else {
                mclTitleTV!!.text = getString(com.tokopedia.saldodetails.R.string.modal_toko)
            }

            if (merchantCreditDetails!!.anchorList != null) {
                populateAnchorListData()
            } else {
                mclActionItemTV!!.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(merchantCreditDetails!!.bodyDesc)) {
                mclDescTV!!.text = merchantCreditDetails!!.bodyDesc
                mclDescTV!!.visibility = View.VISIBLE
            } else {
                mclDescTV!!.visibility = View.GONE
            }

            if (merchantCreditDetails!!.infoList != null && merchantCreditDetails!!.infoList.size > 0) {
                mclInfoListLL!!.visibility = View.VISIBLE
                populateInfolistData()
            } else {
                mclInfoListLL!!.visibility = View.GONE
            }

            if (merchantCreditDetails!!.isShowBox && merchantCreditDetails!!.boxInfo != null) {
                mclBoxLayout!!.visibility = View.VISIBLE
                populateBoxData()
            } else {
                mclBoxLayout!!.visibility = View.GONE
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
            mclboxTitleTV!!.visibility = View.VISIBLE
            mclboxTitleTV!!.text = merchantCreditDetails!!.boxInfo!!.boxTitle
        } else {
            mclboxTitleTV!!.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(merchantCreditDetails!!.boxInfo!!.boxDesc)) {
            mclBoxDescTV!!.visibility = View.VISIBLE
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
                                ds.color = resources.getColor(com.tokopedia.design.R.color.tkpd_main_green)
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
            mclBoxDescTV!!.visibility = View.GONE
        }
    }

    private fun startWebView(linkUrl: String?) {
        startActivity(SaldoWebViewActivity.getWebViewIntent(context!!, linkUrl!!))
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
                mclActionItemTV!!.setTextColor(resources.getColor(com.tokopedia.design.R.color.tkpd_main_green))
            }

            mclActionItemTV!!.setOnClickListener { v ->

                saldoDetailsAnalytics.eventMCLActionItemClick(mclActionItemTV!!.text.toString(),
                        merchantCreditDetails!!.status.toString())

                if (gqlAnchorListResponse.isShowDialog && gqlAnchorListResponse.dialogInfo != null) {

                    val closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
                    val view = layoutInflater.inflate(com.tokopedia.saldodetails.R.layout.mcl_bottom_dialog, null)
                    (view.findViewById<View>(com.tokopedia.saldodetails.R.id.mcl_bottom_sheet_title) as TextView).text = gqlAnchorListResponse.dialogInfo!!.dialogTitle
                    (view.findViewById<View>(com.tokopedia.saldodetails.R.id.mcl_bottom_sheet_desc) as TextView).text = gqlAnchorListResponse.dialogInfo!!.dialogBody

                    closeableBottomSheetDialog.setContentView(view)
                    closeableBottomSheetDialog.show()
                    closeableBottomSheetDialog.setCanceledOnTouchOutside(true)
                } else {
                    if (!TextUtils.isEmpty(gqlAnchorListResponse.link)) {
                        startWebView(gqlAnchorListResponse.link)
                    }
                }
            }
        }
        mclActionItemTV!!.visibility = View.VISIBLE
    }

    override fun initInjector() {
        val saldoDetailsComponent = SaldoDetailsComponentInstance.getComponent(activity!!.application)
        saldoDetailsComponent!!.inject(this)
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
