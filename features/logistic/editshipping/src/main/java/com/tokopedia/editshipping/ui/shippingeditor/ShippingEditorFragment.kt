package com.tokopedia.editshipping.ui.shippingeditor

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getColor
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.di.shippingeditor.ShippingEditorComponent
import com.tokopedia.editshipping.domain.model.shippingEditor.*
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorConventionalAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorOnDemandItemAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.WarehouseInactiveAdapter
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.unifyprinciples.Typography
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
    private var bottomSheetImageInfo: DeferredImageView? = null
    private var bottomSheetInfoCourier: Typography? = null
    private var bottomSheetInfoCourierDetail: Typography? = null
    private var btnShipperBottomSheet: UnifyButton? = null
    private var bottomSheetShipperInfoType: Int? = -1

    private var bottomSheetShipperDetails: BottomSheetUnify? = null
    private var bottomSheetShipperDetailsRv: RecyclerView? = null
    private var bottomSheetShipperAdapter = ShippingEditorDetailsAdapter()
    private var bottomSheetCourierInactiveAdapter = WarehouseInactiveAdapter()

    private var bottomSheetCourierInactive: BottomSheetUnify? = null
    private var bottomSheetBOValidation: BottomSheetUnify? = null
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

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var globalErrorLayout: GlobalError? = null

    private var shippingEditorOnDemandAdapter = ShippingEditorOnDemandItemAdapter(this)
    private var shippingEditorConventionalAdapter = ShippingEditorConventionalAdapter(this)
//    private var shippingEditorAdapter = ShippingEditorAdapter()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShippingEditorComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipping_editor_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initAdapter()
        initViewModel()
        fetchData()
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
    }

    private fun renderTextDetailCourier() {
        val textDetailCourier = MethodChecker.fromHtml(getString(R.string.tv_detail_kurir))
        val selengkapnyaButton = "Selengkapnya"
        val spannableString = SpannableString(textDetailCourier)
        val color = getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500)
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
        viewModel.shipperList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShippingEditorState.Success -> {
                    viewModel.getShipperTickerList(userSession?.shopId.toInt())
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
        viewModel.getShipperList(userSession?.shopId.toInt())
    }

    private fun updateData(data: ShippersModel) {
//        shippingEditorAdapter.setData(data.onDemand, data.conventional)
        shippingEditorOnDemandAdapter.updateData(data.onDemand)
        shippingEditorConventionalAdapter.updateData(data.conventional)
    }

    private fun updateTickerData(data: ShipperTickerModel) {
        shippingEditorOnDemandAdapter.setTickerData(data)
        shippingEditorConventionalAdapter.setTickerData(data)
    }

    private fun updateBottomsheetData(data: ShipperDetailModel) {
        bottomSheetShipperAdapter.setShippingEditorDetailsData(data)
        openBottomSheetDetails()
    }

    private fun setValidateBOData(data: ValidateShippingEditorModel) {

    }

    private fun setDataCourierNotCovered(data: ValidateShippingEditorModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE
        bottomSheetCourierInactiveAdapter.setData(data.uiContent.warehouses)
//        bottomSheetCourierInactiveAdapter.setInactiveWarehouseValidate(data.uiContent)
        openBottomSheetValidateCoureirNotCovered(data)
    }

    private fun setDataBoAndCourierNotCovered(data: ValidateShippingEditorModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_BO_STATE
        bottomSheetCourierInactiveAdapter.setData(data.uiContent.warehouses)
//        bottomSheetCourierInactiveAdapter.setInactiveWarehouseValidate(data.uiContent)
        openBottomSheetValidateCoureirNotCovered(data)
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
                val color = getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500)
                spannableString.setSpan(ForegroundColorSpan(color), spannableString.length - item.textLink.length, spannableString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                messages.add(TickerData(item.header, item.body + " " + item.textLink, Ticker.TYPE_ANNOUNCEMENT, true, item.urlLink))
            }
            val tickerPageAdapter = TickerPagerAdapter(context, messages)
            tickerShipperInfo?.addPagerView(tickerPageAdapter, messages)
            tickerPageAdapter?.setPagerDescriptionClickEvent(object: TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    val applink = itemData.toString()
                    if(!TextUtils.isEmpty(applink)) {
                        RouteManager.route(activity, applink)
                    }
                }

            })
            tickerShipperInfo?.visible()
        } else {
            tickerShipperInfo?.gone()
        }
    }

    private fun validateSaveData(data: ValidateShippingEditorModel) {
        if (data.state == 5) {
            setDataCourierNotCovered(data)
        } else if (data.state == 6) {
            setDataBoAndCourierNotCovered(data)
        } else if (data.state == 0) {
            viewModel.saveShippingData(userSession?.shopId.toInt(), getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds()), data.featureId.toString())
        }
    }

    override fun onShipperInfoClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun openBottomSheetWarehouseInactive(ctx: Context, data: List<WarehousesModel>, shipperName: String) {
        bottomSheetCourierInactive = BottomSheetUnify()
        val viewBottomSheetWarehouseInactive = View.inflate(ctx, R.layout.bottomsheet_courier_inactive, null)
        setupChildCourierInactive(viewBottomSheetWarehouseInactive, shipperName, data?.size, null)

        bottomSheetCourierInactive?.apply {
            setTitle(ctx.getString(R.string.title_bottomsheet_courier_inactive, data?.size))
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetWarehouseInactive)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetCourierInactive?.show(it, "show")
        }
    }

    private fun openBottomSheetValidateCoureirNotCovered(data: ValidateShippingEditorModel) {
        val uiContentModel = data.uiContent
        bottomSheetCourierInactive = BottomSheetUnify()
        val viewBottomSheetWarehouseInactive = View.inflate(context, R.layout.bottomsheet_courier_inactive, null)
        setupChildCourierInactive(viewBottomSheetWarehouseInactive, uiContentModel.headerLocation, uiContentModel.warehouses.size, data)

        bottomSheetCourierInactive?.apply {
            setTitle("Lokasi toko yang tidak memiliki kurir")
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetWarehouseInactive)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetCourierInactive?.show(it, "show")
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
                tvCourierInactive?.text = getString(R.string.text_header_courier_not_covered, header, courierCount)
                btnPrimaryVertical?.text = "Mengerti"
                btnPrimaryVertical?.setOnClickListener {
                    bottomSheetCourierInactive?.dismiss()
                }
                btnVerticalLayout?.visible()
                btnSecondaryVertical?.gone()
                btnHorizontalLayout?.gone()
                tickerChargeBoCourierInactive?.gone()
            }
            BOTTOMSHEET_HEADER_WAREHOUSE_INACTIVE_STATE -> {
                tvCourierInactive?.text = getString(R.string.text_header_courier_not_covered_all)
                btnPrimaryVertical?.text = "Mengerti"
                btnPrimaryVertical?.setOnClickListener {
                    bottomSheetCourierInactive?.dismiss()
                }
                btnVerticalLayout?.visible()
                btnSecondaryVertical?.gone()
                btnHorizontalLayout?.gone()
                tickerChargeBoCourierInactive?.gone()
            }
            BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE -> {
                tvCourierInactive?.text = getString(R.string.text_header_validate_courier_not_covered, courierCount)
                btnPrimaryVertical?.text = "Batalkan & Atur Ulang"
                btnPrimaryVertical?.setOnClickListener {
                    bottomSheetCourierInactive?.dismiss()
                }
                btnSecondaryVertical?.text = "Simpan"
                btnSecondaryVertical?.setOnClickListener {
                    viewModel.saveShippingData(userSession?.shopId.toInt(), getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds()), data?.featureId.toString())
                    bottomSheetCourierInactive?.dismiss()

                }
                btnVerticalLayout?.visible()
                btnSecondaryVertical?.visible()
                btnHorizontalLayout?.gone()
                tickerChargeBoCourierInactive?.gone()
            }
            BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_BO_STATE -> {
                tvCourierInactive?.text = getString(R.string.text_header_validate_courier_not_covered, courierCount)
                btnPrimaryHorizontal?.text = "Nonaktifkan"
                btnPrimaryHorizontal?.setOnClickListener {
                    bottomSheetCourierInactive?.dismiss()
                }
                btnSecondaryHorizontal?.text = "Tetap Aktifkan"
                btnSecondaryHorizontal?.setOnClickListener {
                    viewModel.saveShippingData(userSession?.shopId.toInt(),  getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds()), data?.featureId.toString())
                    bottomSheetCourierInactive?.dismiss()
                }
                tickerChargeBoCourierInactive?.apply {
                    tickerTitle = data?.uiContent?.ticker?.header
                    data?.uiContent?.ticker?.body?.let { setHtmlDescription(it + getString(R.string.text_bo_link)) }
                    setDescriptionClickEvent(object: TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            startActivity(RouteManager.getIntent(context, String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, data?.uiContent?.ticker?.urlLink)))
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
        }
    }

    private fun openBottomSheetDetails() {
        bottomSheetShipperDetails = BottomSheetUnify()
        val viewBottomSheetShipperDetails = View.inflate(context, R.layout.bottomsheet_shipper_detail, null)
        setupChild(viewBottomSheetShipperDetails)

        bottomSheetShipperDetails?.apply {
            setTitle("Detail Kurir Pengiriman")
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetShipperDetails)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetShipperDetails?.show(it, "show")
        }
    }

    private fun setupChild(child: View) {
        bottomSheetShipperDetailsRv = child.findViewById(R.id.rv_shipper_detail)

        bottomSheetShipperDetailsRv?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = bottomSheetShipperAdapter
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

        if(bottomSheetShipperInfoType == 1) {
            bottomSheetImageInfo?.loadRemoteImageDrawable("AWB")
            bottomSheetInfoCourier?.text = getString(R.string.awb_otomatis_title)
            bottomSheetInfoCourierDetail?.text = getString(R.string.awb_otomatis_detail)

        } else {
            bottomSheetImageInfo?.loadRemoteImageDrawable("Non Tunai")
            bottomSheetInfoCourier?.text = getString(R.string.non_tunai_title)
            bottomSheetInfoCourierDetail?.text = getString(R.string.non_tunai_detail)
        }
        btnShipperBottomSheet?.setOnClickListener { bottomSheetShipperInfo?.dismiss() }
    }

    private fun saveButtonShippingEditor() {
        val activatedSpIds = getListActivatedSpIds(shippingEditorConventionalAdapter.getActiveSpIds(), shippingEditorOnDemandAdapter.getActiveSpIds())
        if (activatedSpIds.isEmpty()) {
            view?.let { Toaster.build(it, EditShippingConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show() }
        } else viewModel.validateShippingEditor(userSession?.shopId.toInt(), activatedSpIds)
    }

    private fun getListActivatedSpIds(onDemandList: List<String>, conventionalList: List<String>): String {
        val activatedListShipperIds = mutableListOf<String>()
        activatedListShipperIds.addAll(onDemandList)
        activatedListShipperIds.addAll(conventionalList)
        return activatedListShipperIds.joinToString().replace(" ", "")
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
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
                    Toaster.build(it, throwable.message
                            ?: EditShippingConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
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

    companion object {
        private const val BOTTOMSHEET_SHIPPER_WAREHOUSE_INACTIVE_STATE = 1
        private const val BOTTOMSHEET_HEADER_WAREHOUSE_INACTIVE_STATE = 2
        private const val BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE = 3
        private const val BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_BO_STATE = 4
    }

}