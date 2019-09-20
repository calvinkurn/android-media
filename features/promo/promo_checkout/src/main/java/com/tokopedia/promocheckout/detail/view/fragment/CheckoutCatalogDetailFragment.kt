package com.tokopedia.promocheckout.detail.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewFlipper
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.model.detailmodel.HachikoCatalogDetail
import com.tokopedia.promocheckout.detail.view.presenter.CheckoutCatalogDetailContract
import com.tokopedia.promocheckout.detail.view.presenter.CheckoutCatalogDetailPresenter
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class CheckoutCatalogDetailFragment : BottomSheetDialogFragment(), CheckoutCatalogDetailContract.View {
    private val mContainerMain: ViewFlipper? = null
    private val mRefreshRepeatCount = 0
    private var mCouponName: String? = null
    var mTimer: CountDownTimer? = null
    lateinit var progressBar: ProgressBar
    //private PerformanceMonitoring fpmDetailTokopoint;
    private val promoCheckoutDetailComponent: PromoCheckoutDetailComponent? = null

    @Inject
    lateinit var mPresenter: CheckoutCatalogDetailPresenter
    //    @Override
    protected//        return AnalyticsTrackerUtil.ScreenKeys.COUPON_CATALOG_SCREEN_NAME;
    val screenName: String
        get() = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        val view = inflater.inflate(R.layout.promo_content_coupon_catalog, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter!!.attachView(this)
        initListener()

        if (arguments == null) {
            if (activity != null) {
                activity!!.finish()
            }
            return
        }

        val code = arguments!!.getString("slug")
        val catalog_id = arguments!!.getInt("catalog_id")
        mPresenter!!.getCatalogDetail(code, catalog_id)
    }

    override fun onDestroy() {
        mPresenter!!.destroyView()

        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }

        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //    AnalyticsTrackerUtil.sendScreenEvent(getActivity(), getScreenName());
    }

    override fun getAppContext(): Context {
        return activity!!.applicationContext
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun showError() {
        //  mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    override fun onEmptyCatalog() {
        //mContainerMain.setDisplayedChild(CONTAINER_ERROR);
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    protected fun initInjector() {
        //        super.initInjector();
        //        getComponent(PromoCheckoutDetailComponent.class).inject(this)


        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initViews(view: View) {
        //mContainerMain = view.findViewById(R.id.container);
        progressBar = view.findViewById(R.id.progressbar)
    }

    private fun initListener() {
        if (view == null) {
            return
        }
    }

    override fun openWebView(url: String) {
        //((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    override fun showRedeemCouponDialog(cta: String, code: String, title: String) {
        //        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        //        adb.setTitle(R.string.tp_label_use_coupon);
        //        StringBuilder messageBuilder = new StringBuilder()
        //                .append(getString(R.string.tp_label_coupon))
        //                .append(" ")
        //                .append("<strong>")
        //                .append(title)
        //                .append("</strong>")
        //                .append(" ")
        //                .append(getString(R.string.tp_mes_coupon_part_2));
        //        adb.setMessage(MethodChecker.fromHtml(messageBuilder.toString()));
        //        adb.setPositiveButton(R.string.tp_label_use, (dialogInterface, i) -> {
        //            //Call api to validate the coupon
        //            mPresenter.redeemCoupon(code, cta);
        //
        //
        //        });
        //        adb.setNegativeButton(R.string.tp_label_later, (dialogInterface, i) -> {
        //
        //
        //        });
        //        AlertDialog dialog = adb.create();
        //        dialog.show();
        //        decorateDialog(dialog);
    }

    override fun showConfirmRedeemDialog(cta: String, code: String, title: String) {
        val promoCheckoutDetailMarketplaceFragment=PromoCheckoutDetailMarketplaceFragment.createInstance(code, false, oneClickShipment = false, pageTracking = 0, promo = arguments?.getParcelable("promos")!!)
        promoCheckoutDetailMarketplaceFragment.show(childFragmentManager, "")

    }

    override fun showValidationMessageDialog(item: HachikoCatalogDetail, title: String, message: String, resCode: Int) {
        val viewRedeemCoupon = View.inflate(this.context, R.layout.popup_redeem_coupon, null)
        val adb = AlertDialog.Builder(activityContext!!)
        adb.setView(viewRedeemCoupon)
        val labelPositive: String
        val labelNegative: String? = null


        val tvTitlePopup = viewRedeemCoupon.findViewById<TextView>(R.id.tv_titlepopup)
        val tvContentPopup = viewRedeemCoupon.findViewById<TextView>(R.id.tv_contentpopup)
        val button = viewRedeemCoupon.findViewById<TextView>(R.id.button2)
        val buttonDismiss = viewRedeemCoupon.findViewById<TextView>(R.id.button1)


        when (resCode) {
            42020 ->
                button.text = "OK"
            42021 -> {
                button.text = "Lengkapi Profil"
                buttonDismiss.text = "Nanti Saja"
            }

            200 -> {
                button.text = "Tukar"
                buttonDismiss.text = "Batal"
            }

            42022 -> {
                button.text = "OK"
            }
            else -> {
                button.text = "OK"
            }
        }

        if (title == null || title.isEmpty()) {
            tvTitlePopup.text = "Penukaran Gagal"
        } else {
            tvTitlePopup.text = title
        }

        tvContentPopup.text = MethodChecker.fromHtml(message)
        val dialog = adb.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        decorateDialog(dialog)

        buttonDismiss.setOnClickListener { view -> dialog.dismiss() }
        button.setOnClickListener { v ->
            dialog.dismiss()
            when (resCode) {
                200 -> {
                    mPresenter.startSaveCoupon(item)
                }
                42021 -> {
                   // startActivity(Intent(appContext, ProfileCompletionActivity::class.java))
                }
            }
        }
    }

    override fun showRedeemFullError(item: HachikoCatalogDetail, title: String, desc: String) {

    }

    override fun onSuccessPoints(point: String) {
        if (view == null) {
            return
        }
    }

    override fun onErrorPoint(errorMessage: String) {
        //TODO @lavekush need to handle it
    }

    override fun onPreValidateError(title: String, message: String) {
        //        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        //
        //        adb.setTitle(title);
        //        adb.setMessage(message);
        //
        //        adb.setPositiveButton(R.string.tp_label_ok, (dialogInterface, i) -> {
        //                }
        //        );
        //
        //        AlertDialog dialog = adb.create();
        //        dialog.show();
        //        decorateDialog(dialog);
    }

    private fun decorateDialog(dialog: AlertDialog) {
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activityContext!!,
                    R.color.tkpd_main_green))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        }

        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activityContext!!,
                    R.color.grey_warm))
        }
    }

    //setting catalog values to ui
    private fun setCatalogToUi(data: HachikoCatalogDetail) {
        /*     if (getView() == null) {
            return;
        }*/

        mCouponName = data.title
        val quota = view!!.findViewById<Typography>(R.id.text_quota_count)
        val description = view!!.findViewById<Typography>(R.id.text_description)
        val disabledError = view!!.findViewById<Typography>(R.id.text_disabled_error)
        //        ConstraintLayout giftSectionMainLayout = getView().findViewById(R.id.gift_section_main_layout);
        //        Typography giftImage = getView().findViewById(R.id.gift_image);
        //        Typography giftButton = getView().findViewById(R.id.gift_btn);
        val bottomSeparator = view!!.findViewById<View>(R.id.bottom_separator)
        val btnAction2 = view!!.findViewById<Typography>(R.id.button_action_2)
        val imgBanner = view!!.findViewById<ImageView>(R.id.img_banner)
        val labelPoint = view!!.findViewById<Typography>(R.id.text_point_label)
        val textDiscount = view!!.findViewById<Typography>(R.id.text_point_discount)
        val titleMinTrans = view!!.findViewById<Typography>(R.id.titleMinTrans)
        val textMinTrans = view!!.findViewById<Typography>(R.id.textMinTrans)

        btnAction2.visibility = View.VISIBLE
        description.text = data.title
        titleMinTrans.text = data.minimumUsageLabel
        textMinTrans.text = data.minimumUsage
        btnAction2.text = data.buttonStr
        btnAction2.setBackgroundResource(R.drawable.bg_button_orange_enabled)

        ImageHandler.loadImageFitCenter(imgBanner.context, imgBanner, data.imageUrlMobile)

        val tvTnc = view!!.findViewById<WebView>(R.id.tnc_content)
        val tncSeeMore = view!!.findViewById<Typography>(R.id.tnc_see_more)
        val howToUseSeeMore = view!!.findViewById<Typography>(R.id.how_to_use_see_more)

        if (data.tnc != null) {
            tvTnc.loadData(data.tnc, COUPON_MIME_TYPE, UTF_ENCODING)
        }
        /*   tncSeeMore.setOnClickListener(v -> {
            loadWebViewInBottomsheet(data.getTnc(), getString(R.string.tnc_coupon_catalog));
        });
        howToUseSeeMore.setOnClickListener(v -> {
            loadWebViewInBottomsheet(data.getHowToUse(), getString(R.string.how_to_use_coupon_catalog));
        });*/

        val pointValue = view!!.findViewById<Typography>(R.id.text_point_value_coupon)
        if (data.pointsStr == null || data.pointsStr.isEmpty()) {
            pointValue.visibility = View.GONE
        } else {
            pointValue.visibility = View.VISIBLE
            pointValue.text = data.pointsStr
        }


        val points = view!!.findViewById<Typography>(R.id.text_point_value)
        if (data.pointsStr == null || data.pointsStr.isEmpty()) {
            points.visibility = View.GONE
        } else {
            points.visibility = View.VISIBLE
            points.text = data.points.toString()
        }

        //Quota text handling
        /*    if (data.getUpperTextDesc() == null || data.getUpperTextDesc().isEmpty()) {
            quota.setVisibility(View.GONE);
        } else {
            quota.setVisibility(View.VISIBLE);
            StringBuilder upperText = new StringBuilder();
            for (int i = 0; i < data.getUpperTextDesc().size(); i++) {
                if (i == 1) {
                    //exclusive case for handling font color of second index.
                    upperText.append("<font color='#ff5722'>" + data.getUpperTextDesc().get(i) + "</font>");
                } else {
                    upperText.append(data.getUpperTextDesc().get(i)).append(" ");
                }
            }
            quota.setText(MethodChecker.fromHtml(upperText.toString()));
        }*/

        //Quota text handling
        /*   if (data.getDisableErrorMessage() == null || data.getDisableErrorMessage().isEmpty()) {
            disabledError.setVisibility(View.GONE);
        } else {
            disabledError.setVisibility(View.VISIBLE);
            disabledError.setText(data.getDisableErrorMessage());
        }*/

        //disabling the coupons if not eligible for current membership
        /*    if (data.isDisabled()) {
            ImageUtil.dimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), R.color.black_54));
        } else {
            ImageUtil.unDimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), R.color.orange_red));
        }

        if (data.isDisabledButton()) {
            giftSectionMainLayout.setVisibility(View.GONE);
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), R.color.black_12));
        } else {
            giftSectionMainLayout.setVisibility(View.VISIBLE);
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), R.color.white));
        }

        if (data.getPointsSlash() <= 0) {
            labelPoint.setVisibility(View.GONE);
        } else {
            labelPoint.setVisibility(View.VISIBLE);
            labelPoint.setText(data.getPointsSlashStr());
            labelPoint.setPaintFlags(labelPoint.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (data.getDiscountPercentage() <= 0) {
            textDiscount.setVisibility(View.GONE);
        } else {
            textDiscount.setVisibility(View.VISIBLE);
            textDiscount.setText(data.getDiscountPercentageStr());
        }*/


        btnAction2.setOnClickListener { v ->
            //call validate api the show dialog
            mPresenter.startValidateCoupon(data)

        }
    }

    private fun getLessDisplayData(data: String, seeMore: Typography): String {
        /*  String[] totalString = data.split(LIST_TAG_START);
        String displayString = totalString[0] + LIST_TAG_START;
        if (totalString.length > MAX_POINTS_TO_SHOW + 1) {
            for (int i = 1; i < MAX_POINTS_TO_SHOW; i++) {
                displayString = displayString.concat(totalString[i] + LIST_TAG_START);
            }
            displayString = displayString.concat(totalString[MAX_POINTS_TO_SHOW]);
            String lastString = totalString[totalString.length - 1];
            if (lastString.contains(LIST_TAG_END)) {
                displayString = displayString + LIST_TAG_END + totalString[totalString.length - 1].split(LIST_TAG_END)[1];
            }
        } else {
           displayString = data;
            seeMore.setVisibility(View.GONE);
        }
       return displayString;*/
        return " "
    }

    private fun loadWebViewInBottomsheet(data: String, title: String) {
        //        CloseableBottomSheetDialog bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        //        View view = getLayoutInflater().inflate(R.layout.catalog_bottomsheet, null, true);
        //        WebView webView = view.findViewById(R.id.catalog_webview);
        //        ImageView closeBtn = view.findViewById(R.id.close_button);
        //        Typography titleView = view.findViewById(R.id.title_closeable);
        //
        //        webView.loadData(data, COUPON_MIME_TYPE, UTF_ENCODING);
        //        closeBtn.setOnClickListener((v) -> bottomSheet.dismiss());
        //        titleView.setText(title);
        //        bottomSheet.setCustomContentView(view, title, false);
        //        bottomSheet.show();
    }

    companion object {
        private val FPM_DETAIL_TOKOPOINT = "ft_tokopoint_detail"
        private val CONTAINER_LOADER = 0
        private val CONTAINER_DATA = 1
        private val CONTAINER_ERROR = 2
        private val UTF_ENCODING = "UTF-8"
        private val COUPON_MIME_TYPE = "text/html"
        private val LIST_TAG_START = "<li>"
        private val LIST_TAG_END = "</li>"
        private val MAX_POINTS_TO_SHOW = 4

        fun newInstance(extras: Bundle): Fragment {
            val fragment = CheckoutCatalogDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }


}
