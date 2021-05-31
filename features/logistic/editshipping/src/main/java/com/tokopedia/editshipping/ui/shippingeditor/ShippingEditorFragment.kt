package com.tokopedia.editshipping.ui.shippingeditor

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getColor
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.di.shippingeditor.DaggerShippingEditorComponent
import com.tokopedia.editshipping.di.shippingeditor.ShippingEditorComponent
import com.tokopedia.editshipping.domain.model.shippingEditor.*
import com.tokopedia.editshipping.ui.EditShippingActivity
import com.tokopedia.editshipping.ui.bottomsheet.ShipperDetailBottomSheet
import com.tokopedia.editshipping.ui.shippingeditor.adapter.*
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShippingEditorFragment: BaseDaggerFragment(), ShippingEditorOnDemandItemAdapter.ShippingEditorItemAdapterListener, ShippingEditorConventionalAdapter.ShippingEditorConventionalListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel: ShippingEditorViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShippingEditorViewModel::class.java)
    }

    private var shippingEditorLayout: ConstraintLayout? = null
    private var tickerShipperInfo: Ticker? = null
    private var shipperListOnDemand: RecyclerView? = null
    private var shipperListConventional: RecyclerView? = null
    private var btnSaveShipper: UnifyButton? = null
    private var tvDetailCourier: Typography? = null
    private var tickerOnDemand: Ticker? = null
    private var tickerHeader: Ticker? = null

    private var bottomSheetShipperInfo: BottomSheetUnify? = null
    private var bottomSheetImageInfo: ImageUnify? = null
    private var bottomSheetInfoCourier: Typography? = null
    private var bottomSheetInfoCourierDetail: Typography? = null
    private var btnShipperBottomSheet: UnifyButton? = null
    private var bottomSheetShipperInfoType: Int? = -1

    private var bottomSheetShipperDetailsRv: RecyclerView? = null
    private var bottomSheetShipperAdapter = ShippingEditorDetailsAdapter()
    private var bottomSheetCourierInactiveAdapter = WarehouseInactiveAdapter()
    private val bottomSheetFeatureInfoAdapter = FeatureInfoAdapter()

    private var bottomSheetCourierInactive: BottomSheetUnify? = null
    private var bottomSheetBOValidation: BottomSheetUnify? = null
    private var bottomSheetFeatureInfo: BottomSheetUnify? = null
    private var tvCourierInactive: Typography? = null
    private var warehouseListRv: RecyclerView? = null
    private var bottomSheetCourierInactiveState: Int = 0
    private var tickerChargeBoCourierInactive: Ticker? = null
    private var btnVerticalLayout: LinearLayout? = null
    private var btnHorizontalLayout: LinearLayout? = null
    private var btnPrimaryVertical: UnifyButton? = null
    private var btnPrimaryHorizontal: UnifyButton? = null
    private var btnSecondaryVertical: UnifyButton? = null
    private var btnSecondaryHorizontal: UnifyButton? = null

    private var tickerValidateBO: Ticker? = null
    private var textPointOne: Typography? = null
    private var textPointTwo: Typography? = null
    private var textPointThree: Typography? = null
    private var btnNonaktifkanValidationBO: UnifyButton? = null
    private var btnAktifkanValidateBO: UnifyButton? = null


    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var globalErrorLayout: GlobalError? = null

    private var shippingEditorOnDemandAdapter = ShippingEditorOnDemandItemAdapter(this)
    private var shippingEditorConventionalAdapter = ShippingEditorConventionalAdapter(this)

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerShippingEditorComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipping_editor_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkWhitelistedUser()
        initViews()
        initAdapter()
        initViewModel()
    }

    private fun checkWhitelistedUser() {
        viewModel.getWhitelistData(userSession.shopId.toLong())
    }

    private fun initViews() {
        shippingEditorLayout = view?.findViewById(R.id.shipping_editor_layout)
        tickerShipperInfo = view?.findViewById(R.id.ticker_shipper_info)
        shipperListOnDemand = view?.findViewById(R.id.rv_on_demand)
        shipperListConventional = view?.findViewById(R.id.rv_conventional)
        btnSaveShipper = view?.findViewById(R.id.btn_save_shipper)
        globalErrorLayout = view?.findViewById(R.id.global_error)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)
        tvDetailCourier = view?.findViewById(R.id.tv_detail_kurir)
        tickerOnDemand = view?.findViewById(R.id.ticker_dijemput_kurir)
        tickerHeader = view?.findViewById(R.id.ticker_header)

        renderTickerOnDemand()
        renderTextDetailCourier()
        btnSaveShipper?.setOnClickListener { saveButtonShippingEditor() }
    }

    private fun renderTickerOnDemand() {
        SpannableString(getString(R.string.awb_otomatis_list))
        tickerOnDemand?.setHtmlDescription(getString(R.string.ticker_dijemput_kurir_complete))
        tickerOnDemand?.setDescriptionClickEvent(object: TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl == STATE_AWB_VALIDATION){
                    bottomSheetShipperInfoType = 1
                    openBottomSheetShipperInfo()
                } else {
                    bottomSheetShipperInfoType = 2
                    openBottomSheetShipperInfo()
                }
            }
            override fun onDismiss() {
                //no-op
            }

        })
    }

    private fun renderTextDetailCourier() {
        val textDetailCourier = MethodChecker.fromHtml(getString(R.string.tv_detail_kurir))
        val selengkapnyaButton = getString(R.string.selengkapnya)
        val spannableString = SpannableString(textDetailCourier)
        val color = getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        spannableString.setSpan(ForegroundColorSpan(color), spannableString.length - selengkapnyaButton.length, spannableString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), spannableString.length - selengkapnyaButton.length, spannableString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tvDetailCourier?.text = spannableString
        tvDetailCourier?.setOnClickListener {
            viewModel.getShipperDetail()
        }

    }

    private fun initAdapter() {
        shipperListOnDemand?.adapter = shippingEditorOnDemandAdapter
        shipperListConventional?.adapter = shippingEditorConventionalAdapter
        shipperListOnDemand?.layoutManager = LinearLayoutManager(context)
        shipperListConventional?.layoutManager = LinearLayoutManager(context)
    }

    private fun initViewModel() {
        viewModel.shopWhitelist.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShippingEditorState.Success -> {
                    if (it.data.data.eligibilityState == 1) {
                        swipeRefreshLayout?.isRefreshing = false
                        fetchData()
                    } else {
                        activity?.finish()
                        val intent = context?.let { context -> EditShippingActivity.createIntent(context) }
                        startActivityForResult(intent, 1998)
                    }
                }

                is ShippingEditorState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                else -> {
                    swipeRefreshLayout?.isRefreshing = true
                }
            }
        })

        viewModel.shipperList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShippingEditorState.Success -> {
                    viewModel.getShipperTickerList(userSession.shopId.toLong())
                    updateData(it.data.shippers)
                    renderTicker(it.data.ticker)
                }

                is ShippingEditorState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                else -> {
                    shippingEditorLayout?.gone()
                    btnSaveShipper?.gone()
                    swipeRefreshLayout?.isRefreshing = true
                }
            }
        })

        viewModel.shipperTickerList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShippingEditorState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    shippingEditorLayout?.visible()
                    btnSaveShipper?.visible()
                    globalErrorLayout?.gone()
                    updateTickerData(it.data)
                    updateHeaderTickerData(it.data.headerTicker)
                }
            }
        })

        viewModel.shipperDetail.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShippingEditorState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    updateBottomsheetData(it.data)
                }
                is ShippingEditorState.Fail ->  swipeRefreshLayout?.isRefreshing = false
                else ->  swipeRefreshLayout?.isRefreshing = true
            }
        })

        viewModel.validateDataShipper.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShippingEditorState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    validateSaveData(it.data)
                }
                is ShippingEditorState.Fail ->  swipeRefreshLayout?.isRefreshing = false
                else ->  swipeRefreshLayout?.isRefreshing = true
            }
        })

        viewModel.saveShippingData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShippingEditorState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    fetchData()
                }
                is ShippingEditorState.Fail ->  swipeRefreshLayout?.isRefreshing = false
                else ->  swipeRefreshLayout?.isRefreshing = true
            }
        })
    }

    private fun fetchData() {
        viewModel.getShipperList(userSession.shopId.toLong())
    }

    private fun updateData(data: ShippersModel) {
        shippingEditorOnDemandAdapter.updateData(data.onDemand)
        shippingEditorConventionalAdapter.updateData(data.conventional)
    }

    private fun updateTickerData(data: ShipperTickerModel) {
        shippingEditorOnDemandAdapter.setTickerData(data)
        shippingEditorConventionalAdapter.setTickerData(data)
    }

    private fun updateBottomsheetData(data: ShipperDetailModel) {
        bottomSheetShipperAdapter.setShippingEditorDetailsData(data)
        ShipperDetailBottomSheet().show(this, bottomSheetShipperAdapter)
    }

    private fun setDataCourierNotCovered(data: ValidateShippingEditorModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE
        bottomSheetCourierInactiveAdapter.setData(data.uiContent.warehouses)
        openBottomSheetValidateCourierNotCovered(data)
    }

    private fun setDataBoAndCourierNotCovered(data: ValidateShippingEditorModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_BO_STATE
        bottomSheetCourierInactiveAdapter.setData(data.uiContent.warehouses)
        openBottomSheetValidateCourierNotCovered(data)
    }

    private fun updateHeaderTickerData(data: HeaderTickerModel) {
        if (data.isActive) {
            tickerHeader?.apply {
                visibility = View.VISIBLE
                tickerTitle = data.header
                setHtmlDescription(data.body + getString(R.string.ticker_header_clicked))
                setDescriptionClickEvent(object: TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        bottomSheetCourierInactiveState = BOTTOMSHEET_HEADER_WAREHOUSE_INACTIVE_STATE
                        bottomSheetCourierInactiveAdapter.setData(data.warehouseModel)
                        openBottomSheetWarehouseInactive(context, data.warehouseModel, "")
                    }

                    override fun onDismiss() {
                        //no-op
                    }

                })
            }
        } else tickerHeader?.gone()
    }

    private fun renderTicker(tickers: List<TickerModel>) {
        val messages = ArrayList<TickerData>()
        if (tickers.isNotEmpty()) {
            for (item in tickers) {
                val spannableString = SpannableString(item.body + " " + item.textLink)
                val color = getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                spannableString.setSpan(ForegroundColorSpan(color), spannableString.length - item.textLink.length, spannableString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                messages.add(TickerData(item.header, item.body + " " + item.textLink, Ticker.TYPE_ANNOUNCEMENT, true, item.urlLink))
            }
            val tickerPageAdapter = TickerPagerAdapter(context, messages)
            tickerShipperInfo?.addPagerView(tickerPageAdapter, messages)
            tickerPageAdapter.setPagerDescriptionClickEvent(object: TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    val appLink = itemData.toString()
                    if (appLink.startsWith("tokopedia")) {
                        startActivity(RouteManager.getIntent(context, appLink))
                    } else {
                        startActivity(RouteManager.getIntent(context, String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, appLink)))
                    }
                }

            })
            tickerShipperInfo?.visible()
        } else {
            tickerShipperInfo?.gone()
        }
    }

    private fun validateSaveData(data: ValidateShippingEditorModel) {
        if (data.state == VALIDATE_MULTIPLE_LOC_STATE) {
            setDataCourierNotCovered(data)
        } else if (data.state == VALIDATE_BO_MULTIPLE_LOC_STATE) {
            setDataBoAndCourierNotCovered(data)
        } else if (data.state == VALIDATE_BEBAS_ONGKIR_STATE) {
            openBottomSheetValidateBOData(data)
        } else {
            viewModel.saveShippingData(userSession.shopId.toLong(), getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds()), convertFeatureIdToString(data.featureId))
        }
    }

    private fun convertFeatureIdToString(featureId: List<Int>?): String? {
        return featureId?.joinToString(separator = ",")
    }

    private fun openBottomSheetWarehouseInactive(ctx: Context, data: List<WarehousesModel>, shipperName: String) {
        bottomSheetCourierInactive = BottomSheetUnify()
        val viewBottomSheetWarehouseInactive = View.inflate(ctx, R.layout.bottomsheet_courier_inactive, null)
        setupChildCourierInactive(viewBottomSheetWarehouseInactive, shipperName, data.size, null)

        bottomSheetCourierInactive?.apply {
            setTitle(ctx.getString(R.string.title_bottomsheet_courier_inactive, data.size))
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetWarehouseInactive)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetCourierInactive?.show(it, "show")
        }
    }

    private fun openBottomSheetValidateCourierNotCovered(data: ValidateShippingEditorModel) {
        val uiContentModel = data.uiContent
        bottomSheetCourierInactive = BottomSheetUnify()
        val viewBottomSheetWarehouseInactive = View.inflate(context, R.layout.bottomsheet_courier_inactive, null)
        setupChildCourierInactive(viewBottomSheetWarehouseInactive, uiContentModel.headerLocation, uiContentModel.warehouses.size, data)


        if (bottomSheetCourierInactiveState == BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE) {
            bottomSheetCourierInactive?.setTitle(data.uiContent.header)
        } else {
            bottomSheetCourierInactive?.setTitle(getString(R.string.bottomsheet_inactive_title))
        }

        bottomSheetCourierInactive?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetWarehouseInactive)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetCourierInactive?.show(it, "show")
        }
    }


    private fun openBottomSheetValidateBOData(data: ValidateShippingEditorModel) {
        bottomSheetBOValidation = BottomSheetUnify()
        bottomSheetBOValidation?.setTitle(getString(R.string.bottomsheet_validation_title))
        val viewBottomSheetBOValidation = View.inflate(activity, R.layout.popup_validation_bo, null)
        setUpChildBottomSheetValidateBOData(viewBottomSheetBOValidation, data)

        bottomSheetBOValidation?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetBOValidation)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetBOValidation?.show(it, "show")
        }
    }

    private fun setUpChildBottomSheetValidateBOData(child: View, data: ValidateShippingEditorModel) {
        val uiContentModel = data.uiContent
        tickerValidateBO = child.findViewById(R.id.ticker_validation_bo)
        textPointOne = child.findViewById(R.id.point_one)
        textPointTwo = child.findViewById(R.id.point_two)
        textPointThree = child.findViewById(R.id.point_three)
        btnAktifkanValidateBO = child.findViewById(R.id.btn_aktifkan)
        btnNonaktifkanValidationBO = child.findViewById(R.id.btn_nonaktifkan)

        tickerValidateBO?.apply {
            tickerTitle = uiContentModel.ticker.header
            setHtmlDescription(uiContentModel.ticker.body + HtmlLinkHelper(context, uiContentModel.ticker.textLink).spannedString.toString())
            setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.getIntent(activity, String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, uiContentModel.ticker.urlLink))
                }

                override fun onDismiss() {
                    //no-op
                }
            })
        }
        if (uiContentModel.body.isNotEmpty()) {
            context?.let {
                textPointOne?.text = HtmlLinkHelper(it, uiContentModel.body[0]).spannedString
                textPointTwo?.text = HtmlLinkHelper(it, uiContentModel.body[1]).spannedString
                textPointThree?.text = HtmlLinkHelper(it, uiContentModel.body[2]).spannedString
            }
        }
        btnNonaktifkanValidationBO?.setOnClickListener {
            viewModel.saveShippingData(userSession.shopId.toLong(), getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds()), convertFeatureIdToString(data.featureId))
            bottomSheetBOValidation?.dismiss()
        }
        btnAktifkanValidateBO?.setOnClickListener {
            bottomSheetBOValidation?.dismiss()
        }

    }

    private fun setupChildCourierInactive(child: View, header: String, courierCount: Int?, data: ValidateShippingEditorModel?) {
        tvCourierInactive = child.findViewById(R.id.tv_courier_inactive)
        warehouseListRv = child.findViewById(R.id.rv_warehouse_inactive)
        tickerChargeBoCourierInactive = child.findViewById(R.id.ticker_charge_bo)
        btnVerticalLayout = child.findViewById(R.id.btn_vertical_layout)
        btnPrimaryVertical = child.findViewById(R.id.btn_primary)
        btnSecondaryVertical = child.findViewById(R.id.btn_secondary)
        btnHorizontalLayout = child.findViewById(R.id.btn_horizonal_layout)
        btnPrimaryHorizontal = child.findViewById(R.id.btn_primary_horizontal)
        btnSecondaryHorizontal = child.findViewById(R.id.btn_secondary_horizonal)

        warehouseListRv?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = bottomSheetCourierInactiveAdapter
        }

        when (bottomSheetCourierInactiveState) {
            BOTTOMSHEET_SHIPPER_WAREHOUSE_INACTIVE_STATE -> {
                showBottomSheetShipperWarehouseInactive(header, courierCount)
            }
            BOTTOMSHEET_HEADER_WAREHOUSE_INACTIVE_STATE -> {
                showBottomSheetHeaderWarehouseInactive()
            }
            BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE -> {
                showBottomSheetValidateWarehouseInactive(data)
            }
            BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_BO_STATE -> {
                showBottomSheetValidateWarehouseInactiveBO(courierCount, data)
            }
        }
    }

    private fun showBottomSheetShipperWarehouseInactive(header: String, courierCount: Int?) {
        tvCourierInactive?.text = getString(R.string.text_header_courier_not_covered, header, courierCount)
        btnPrimaryVertical?.text = getString(R.string.button_understand)
        btnPrimaryVertical?.setOnClickListener {
            bottomSheetCourierInactive?.dismiss()
        }
        btnVerticalLayout?.visible()
        btnSecondaryVertical?.gone()
        btnHorizontalLayout?.gone()
        tickerChargeBoCourierInactive?.gone()
    }

    private fun showBottomSheetHeaderWarehouseInactive() {
        tvCourierInactive?.text = getString(R.string.text_header_courier_not_covered_all)
        btnPrimaryVertical?.text = getString(R.string.button_understand)
        btnPrimaryVertical?.setOnClickListener {
            bottomSheetCourierInactive?.dismiss()
        }
        btnVerticalLayout?.visible()
        btnSecondaryVertical?.gone()
        btnHorizontalLayout?.gone()
        tickerChargeBoCourierInactive?.gone()
    }

    private fun showBottomSheetValidateWarehouseInactive(data: ValidateShippingEditorModel?) {
        tvCourierInactive?.text = data?.uiContent?.headerLocation
        btnPrimaryVertical?.text = getString(R.string.button_cancel_reset)
        btnPrimaryVertical?.setOnClickListener {
            bottomSheetCourierInactive?.dismiss()
        }
        btnSecondaryVertical?.text = getString(R.string.button_save)
        btnSecondaryVertical?.setOnClickListener {
            viewModel.saveShippingData(userSession.shopId.toLong(), getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds()), convertFeatureIdToString(data?.featureId))
            bottomSheetCourierInactive?.dismiss()
        }
        btnVerticalLayout?.visible()
        btnSecondaryVertical?.visible()
        btnHorizontalLayout?.gone()
        tickerChargeBoCourierInactive?.gone()
    }

    private fun showBottomSheetValidateWarehouseInactiveBO(courierCount: Int?, data: ValidateShippingEditorModel?) {
        tvCourierInactive?.text = getString(R.string.text_header_validate_courier_not_covered, courierCount)
        btnPrimaryHorizontal?.text = getString(R.string.button_deactivate)
        btnPrimaryHorizontal?.setOnClickListener {
            viewModel.saveShippingData(userSession.shopId.toLong(),  getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds()), convertFeatureIdToString(data?.featureId))
            bottomSheetCourierInactive?.dismiss()
        }
        btnSecondaryHorizontal?.text = getString(R.string.button_activate)
        btnSecondaryHorizontal?.setOnClickListener {
            bottomSheetCourierInactive?.dismiss()
        }
        tickerChargeBoCourierInactive?.apply {
            tickerTitle = data?.uiContent?.ticker?.header
            setHtmlDescription(data?.uiContent?.ticker?.body + data?.uiContent?.ticker?.textLink)
            setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    goToWebView(data?.uiContent?.ticker?.urlLink)
                }

                override fun onDismiss() {
                    //no-op
                }

            })
        }
        btnVerticalLayout?.gone()
        btnHorizontalLayout?.visible()
        tickerChargeBoCourierInactive?.visible()
    }

    private fun goToWebView(url: String?) {
        if (activity != null) {
            startActivity(RouteManager.getIntent(activity, String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, url)))
        }
    }

    private fun openBottomSheetFeatureInfo() {
        bottomSheetFeatureInfo = BottomSheetUnify()
        val viewBottomSheetFeatureInfo = View.inflate(context, R.layout.bottomsheet_shipper_detail, null)
        setupFeatureChild(viewBottomSheetFeatureInfo)

        bottomSheetFeatureInfo?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetFeatureInfo)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetFeatureInfo?.show(it, "show")
        }
    }

    private fun setupFeatureChild(child: View) {
        bottomSheetShipperDetailsRv = child.findViewById(R.id.rv_shipper_detail)

        bottomSheetShipperDetailsRv?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = bottomSheetFeatureInfoAdapter
        }
    }

    private fun openBottomSheetShipperInfo() {
        bottomSheetShipperInfo = BottomSheetUnify()
        val viewBottomSheetShipperInfo = View.inflate(context, R.layout.bottomsheet_shipper_info, null)
        setupBottomSheetShipperInfoChild(viewBottomSheetShipperInfo)

        bottomSheetShipperInfo?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetShipperInfo)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetShipperInfo?.show(it, "show")
        }
    }

    private fun setupBottomSheetShipperInfoChild(child: View) {
        bottomSheetImageInfo = child.findViewById(R.id.img_info_courier)
        bottomSheetInfoCourier = child.findViewById(R.id.tv_info_courier)
        bottomSheetInfoCourierDetail = child.findViewById(R.id.tv_info_courier_detail)
        btnShipperBottomSheet = child.findViewById(R.id.btn_close)

        if(bottomSheetShipperInfoType == BOTTOMSHEET_AWB_OTOMATIS_INFO) {
            bottomSheetImageInfo?.setImageDrawable(ContextCompat.getDrawable(child.context, R.drawable.ic_awb_otomatis))
            bottomSheetInfoCourier?.text = getString(R.string.awb_otomatis_title)
            bottomSheetInfoCourierDetail?.text = getString(R.string.awb_otomatis_detail)

        } else {
            bottomSheetImageInfo?.setImageDrawable(ContextCompat.getDrawable(child.context, R.drawable.ic_non_tunai))
            bottomSheetInfoCourier?.text = getString(R.string.non_tunai_title)
            bottomSheetInfoCourierDetail?.text = getString(R.string.non_tunai_detail)
        }
        btnShipperBottomSheet?.setOnClickListener { bottomSheetShipperInfo?.dismiss() }
    }

    private fun saveButtonShippingEditor() {
        val activatedSpIds = getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds())
        if (activatedSpIds.isEmpty()) {
            view?.let { Toaster.build(it, EditShippingConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show() }
        } else viewModel.validateShippingEditor(userSession.shopId.toLong(), activatedSpIds)
    }

    private fun getListActivatedSpIds(onDemandList: List<String>, conventionalList: List<String>): String {
        val activatedListShipperIds = mutableListOf<String>()
        activatedListShipperIds.addAll(onDemandList)
        activatedListShipperIds.addAll(conventionalList)
        return activatedListShipperIds.joinToString(separator = ",")
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, EditShippingConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                        }!!
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    if (throwable.message?.contains(ERROR_CODE_NO_ACCESS) == true) {
                        Toaster.build(it, getString(R.string.txt_error_no_access), Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                    } else {
                        Toaster.build(it, throwable.message
                                ?: EditShippingConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                    }
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            fetchData()
        }
        shippingEditorLayout?.gone()
        btnSaveShipper?.gone()
        globalErrorLayout?.visible()
    }

    override fun onShipperTickerConventionalClicked(data: ConventionalModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_SHIPPER_WAREHOUSE_INACTIVE_STATE
        bottomSheetCourierInactiveAdapter.setData(data.warehouseModel)
        context?.let { openBottomSheetWarehouseInactive(it, data.warehouseModel, data.shipperName) }
    }

    override fun onShipperTickerOnDemandClicked(data: OnDemandModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_SHIPPER_WAREHOUSE_INACTIVE_STATE
        bottomSheetCourierInactiveAdapter.setData(data.warehouseModel)
        context?.let { openBottomSheetWarehouseInactive(it, data.warehouseModel, data.shipperName) }
    }

    override fun onFeatureInfoOnDemandClicked(data: List<FeatureInfoModel>) {
        bottomSheetFeatureInfoAdapter.setData(data)
        openBottomSheetFeatureInfo()
    }

    override fun onFeatureInfoConventionalClicked(data: List<FeatureInfoModel>) {
        bottomSheetFeatureInfoAdapter.setData(data)
        openBottomSheetFeatureInfo()
    }

    companion object {
        private const val BOTTOMSHEET_SHIPPER_WAREHOUSE_INACTIVE_STATE = 1
        private const val BOTTOMSHEET_HEADER_WAREHOUSE_INACTIVE_STATE = 2
        private const val BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE = 3
        private const val BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_BO_STATE = 4

        private const val VALIDATE_BEBAS_ONGKIR_STATE = 4
        private const val VALIDATE_MULTIPLE_LOC_STATE = 5
        private const val VALIDATE_BO_MULTIPLE_LOC_STATE = 6

        private const val BOTTOMSHEET_AWB_OTOMATIS_INFO = 1

        private const val STATE_AWB_VALIDATION = "awb_otomatis"
        private const val ERROR_CODE_NO_ACCESS = "555"
    }

}