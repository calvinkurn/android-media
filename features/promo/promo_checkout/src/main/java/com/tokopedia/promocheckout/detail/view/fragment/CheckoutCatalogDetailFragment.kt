package com.tokopedia.promocheckout.detail.view.fragment

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.analytics.PromoCheckoutAnalytics.Companion.promoCheckoutAnalytics
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.model.detailmodel.HachikoCatalogDetail
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailMarketplaceActivity
import com.tokopedia.promocheckout.detail.view.presenter.CheckoutCatalogDetailContract
import com.tokopedia.promocheckout.detail.view.presenter.CheckoutCatalogDetailPresenter
import com.tokopedia.promocheckout.util.ColorUtil
import com.tokopedia.promocheckout.widget.ImageUtil
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject


class CheckoutCatalogDetailFragment : BaseDaggerFragment(), CheckoutCatalogDetailContract.View {

    private var mCouponName: String? = null
    var mTimer: CountDownTimer? = null
    lateinit var progressBar: ProgressBar
    var slug: String? = null
    var catalogId: Int = 0
    var promo: Promo? = null
    val textColorIndex = 1
    private val couponRedemptionCode_LOW_POINT = 42020
    private val couponRedemptionCode_QUOTA_LIMIT_REACHED = 42022
    private val couponRedemptionCode_PROFILE_INCOMPLETE = 42021
    private val couponRedemptionCode_SUCCESS = 200

    @Inject
    lateinit var mPresenter: CheckoutCatalogDetailPresenter

    override fun getScreenName() = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            slug = it.getString(SLUG)
            catalogId = it.getInt(CATALOG_ID)
            promo = it.getParcelable(CHECK_PROMO_CODE_FIRST_STEP_PARAM)
        }
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        val view = inflater.inflate(R.layout.promo_content_coupon_catalog, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)

        if (arguments == null) {
            if (activity != null) {
                requireActivity().finish()
            }
            return
        }

        slug?.let { mPresenter.getCatalogDetail(it, catalogId) }
    }

    override fun onDestroy() {
        mPresenter.destroyView()
        mTimer?.let { countDownTimer ->
            countDownTimer.cancel()
            mTimer = null
        }
        super.onDestroy()
    }

    override fun getAppContext(): Context? {
        return activity?.applicationContext
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun populateDetail(data: HachikoCatalogDetail) {
        setCatalogToUi(data)
    }

    override fun getActivityContext(): Context? {
        return activity
    }

    override fun initInjector() {
        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((requireActivity().application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initViews(view: View) {
        progressBar = view.findViewById(R.id.progressbar)
    }

    override fun showCouponDetail(cta: String?, code: String?, title: String?) {
        activity?.startActivityForResult(PromoCheckoutDetailMarketplaceActivity.createIntent(
                activity, code, oneClickShipment = false, pageTracking = 0, promo = promo), REQUEST_CODE_DETAIL_PROMO)

    }

    override fun showValidationMessageDialog(item: HachikoCatalogDetail, title: String, message: String, resCode: Int) {
        val viewRedeemCoupon = View.inflate(this.context, R.layout.popup_redeem_coupon, null)
        val adb = getActivityContext()?.let { AlertDialog.Builder(it) }
        adb?.setView(viewRedeemCoupon)

        val tvTitlePopup = viewRedeemCoupon.findViewById<TextView>(R.id.tv_titlepopup)
        val tvContentPopup = viewRedeemCoupon.findViewById<TextView>(R.id.tv_contentpopup)
        val button = viewRedeemCoupon.findViewById<TextView>(R.id.button2)
        val buttonDismiss = viewRedeemCoupon.findViewById<TextView>(R.id.button1)

        when (resCode) {
            couponRedemptionCode_LOW_POINT -> {
                button.text = getString(R.string.promo_popup_ok)
                buttonDismiss.visibility = View.GONE
            }
            couponRedemptionCode_PROFILE_INCOMPLETE -> {
                button.text = getString(R.string.promo_popup_button_positive_one)
                buttonDismiss.text = getString(R.string.promo_popup_button_negative_one)
            }
            couponRedemptionCode_SUCCESS -> {
                button.text = getString(R.string.promo_popup_button_positive)
                buttonDismiss.text = getString(R.string.promo_popoup_batal)
            }
            couponRedemptionCode_QUOTA_LIMIT_REACHED -> {
                button.text = getString(R.string.promo_popup_ok)
                buttonDismiss.visibility = View.GONE
            }
            else -> {
                button.text = getString(R.string.promo_popup_ok)
                buttonDismiss.visibility = View.GONE
            }
        }

        if (title.isEmpty()) {
            tvTitlePopup.text = getString(R.string.promo_popup_title)
        } else {
            tvTitlePopup.text = title
        }

        tvContentPopup.text = MethodChecker.fromHtml(message)
        val dialog = adb?.create()
        dialog?.let { alertDialog ->
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        buttonDismiss.setOnClickListener {
            dialog?.dismiss()
            promoCheckoutAnalytics.clickBatalPopUp(slug ?: "")
        }
        button.setOnClickListener { v ->
            dialog?.dismiss()
            when (resCode) {
                couponRedemptionCode_SUCCESS -> {
                    mPresenter.startSaveCoupon(item)
                }
                couponRedemptionCode_PROFILE_INCOMPLETE -> {
                    //   startActivity(Intent(appContext, ProfileCompletionActivity::class.java))
                }
            }
            promoCheckoutAnalytics.clickTukarPopUp(slug)
        }
    }

    private fun setCatalogToUi(data: HachikoCatalogDetail) {
        if (view == null) {
            return
        }

        mCouponName = data.title
        val quota = view?.findViewById<Typography>(R.id.text_quota_count)
        val description = view?.findViewById<Typography>(R.id.text_description)
        val disabledError = view?.findViewById<Typography>(R.id.text_disabled_error)
        val btnAction2 = view?.findViewById<Typography>(R.id.button_action_2)
        val imgBanner = view?.findViewById<ImageView>(R.id.img_banner)
        val labelPoint = view?.findViewById<Typography>(R.id.text_point_label)
        val textDiscount = view?.findViewById<Typography>(R.id.text_point_discount)
        val titleMinTrans = view?.findViewById<Typography>(R.id.titleMinTrans)
        val textMinTrans = view?.findViewById<Typography>(R.id.textMinTrans)

        btnAction2?.visibility = View.VISIBLE
        description?.text = data.title
        titleMinTrans?.text = data.minimumUsageLabel
        textMinTrans?.text = data.minimumUsage
        btnAction2?.text = data.buttonStr
        btnAction2?.setBackgroundResource(com.tokopedia.design.R.drawable.bg_button_orange_enabled)

        ImageHandler.loadImageFitCenter(imgBanner?.context, imgBanner, data.imageUrlMobile)

        val tvTnc = requireView().findViewById<WebView>(R.id.tnc_content)
        if (data.tnc != null) {
            tvTnc.loadData(data.tnc, COUPON_MIME_TYPE, UTF_ENCODING)
        }

        val pointValue = requireView().findViewById<Typography>(R.id.text_point_value_coupon)
        if (data.pointsStr == null || data.pointsStr.isEmpty()) {
            pointValue.visibility = View.GONE
        } else {
            pointValue.visibility = View.VISIBLE
            pointValue.text = data.pointsStr
        }

        //Quota text handling
        if (data.upperTextDesc == null || data.upperTextDesc.isEmpty()) {
            quota?.visibility = View.GONE
        } else {
            quota?.visibility = View.VISIBLE
            var upperText = StringBuilder()
            for (i in 0 until data.upperTextDesc.size) {
                if (i == textColorIndex) {
                    //exclusive case for handling font color of second index.
                    context?.run {
                        upperText.append("<font color='${ColorUtil.getColorFromResToString(this, com.tokopedia.unifyprinciples.R.color.Unify_Y400)}'>" + data.upperTextDesc[i] + "</font>")
                    }
                } else {
                    upperText.append(data.upperTextDesc[i]).append(" ")
                }
            }
            quota?.text = MethodChecker.fromHtml(upperText.toString())
        }

        //Quota text handling
        if (data.disableErrorMessage == null || data.disableErrorMessage.isEmpty()) {
            disabledError?.visibility = View.GONE
        } else {
            disabledError?.visibility = View.VISIBLE
            disabledError?.text = data.disableErrorMessage
        }

        //disabling the coupons if not eligible for current membership
        if (data.isDisabled!!) {
            ImageUtil.dimImage(imgBanner)
            pointValue.setTextColor(ContextCompat.getColor(pointValue.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        } else {
            ImageUtil.unDimImage(imgBanner)
            pointValue.setTextColor(ContextCompat.getColor(pointValue.context, com.tokopedia.unifyprinciples.R.color.Unify_Y500))
        }

        if (data.pointsSlash!! <= 0) {
            labelPoint?.visibility = View.GONE
        } else {
            labelPoint?.visibility = View.VISIBLE
            labelPoint?.text = data.pointsSlashStr
            labelPoint?.paintFlags = labelPoint?.paintFlags?.or(Paint.STRIKE_THRU_TEXT_FLAG)!!
        }

        if (data.discountPercentage!! <= 0) {
            textDiscount?.visibility = View.GONE
        } else {
            textDiscount?.visibility = View.VISIBLE
            textDiscount?.text = data.discountPercentageStr
        }

        btnAction2?.setOnClickListener { v ->
            mPresenter.startValidateCoupon(data)
            promoCheckoutAnalytics.clickTukarButton(slug ?: " ")
        }
    }

    override fun onSuccessPoints(point: String) {
        if (view == null) {
            return
        }
        val textUserPoint = view?.findViewById<Typography>(R.id.text_point_value)
        textUserPoint?.text = (point)
    }

    companion object {
        private val UTF_ENCODING = "UTF-8"
        private val COUPON_MIME_TYPE = "text/html"
        val SLUG = "SLUG"
        val CATALOG_ID = "CATALOG_ID"
        val IS_USE = "IS_USE"
        val PROMOCODE = "PROMOCODE"
        val ONE_CLICK_SHIPMENT = "ONE_CLICK_SHIPMENT"
        val PAGE_TRACKING = "PAGE_TRACKING"
        val CHECK_PROMO_CODE_FIRST_STEP_PARAM = "CHECK_PROMO_CODE_FIRST_STEP_PARAM"
        val REQUEST_CODE_DETAIL_PROMO = 231
        fun newInstance(slug: String?, catalogId: Int?, promoCode: String?, oneClickShipment: Boolean?, pageTracking: Int,
                        promo: Promo?): CheckoutCatalogDetailFragment {
            val checkoutcatalogfragment = CheckoutCatalogDetailFragment()
            val bundle = Bundle()
            bundle.putString(SLUG, slug)
            catalogId?.let { bundle.putInt(CATALOG_ID, it) }
            bundle.putString(PROMOCODE, promoCode)
            bundle.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment ?: false)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            bundle.putParcelable(CHECK_PROMO_CODE_FIRST_STEP_PARAM, promo)
            checkoutcatalogfragment.arguments = bundle
            return checkoutcatalogfragment

        }
    }
}
