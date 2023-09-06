package com.tokopedia.editshipping.ui.shippingeditor

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getColor
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.data.preference.WhitelabelInstanCoachMarkSharePref
import com.tokopedia.editshipping.databinding.BottomsheetCourierInactiveBinding
import com.tokopedia.editshipping.databinding.BottomsheetShipperDetailBinding
import com.tokopedia.editshipping.databinding.BottomsheetShipperInfoBinding
import com.tokopedia.editshipping.databinding.FragmentShippingEditorNewBinding
import com.tokopedia.editshipping.databinding.PopupValidationBoBinding
import com.tokopedia.editshipping.di.shippingeditor.DaggerShippingEditorComponent
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureInfoModel
import com.tokopedia.editshipping.domain.model.shippingEditor.HeaderTickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperGroupModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShippingEditorState
import com.tokopedia.editshipping.domain.model.shippingEditor.TickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ValidateShippingEditorModel
import com.tokopedia.editshipping.domain.model.shippingEditor.WarehousesModel
import com.tokopedia.editshipping.ui.bottomsheet.ShipperDetailBottomSheet
import com.tokopedia.editshipping.ui.shippingeditor.adapter.FeatureInfoAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShipperProductItemAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShipperValidationBoAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorItemAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.WarehouseInactiveAdapter
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.DISABLE_DROPOFF_MAPS
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

// Edit shipping for multi location
class ShippingEditorFragment :
    BaseDaggerFragment(),
    ShippingEditorItemAdapter.ShippingEditorItemAdapterListener,
    ShipperProductItemAdapter.ShipperProductItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel: ShippingEditorViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShippingEditorViewModel::class.java)
    }

    private var bottomSheetShipperInfo: BottomSheetUnify? = null

    private var bottomSheetShipperInfoType: Int? = -1

    private var bottomSheetShipperAdapter = ShippingEditorDetailsAdapter()
    private var bottomSheetCourierInactiveAdapter = WarehouseInactiveAdapter()
    private val bottomSheetFeatureInfoAdapter = FeatureInfoAdapter()

    private var bottomSheetCourierInactive: BottomSheetUnify? = null
    private var bottomSheetBOValidation: BottomSheetUnify? = null
    private var bottomSheetFeatureInfo: BottomSheetUnify? = null

    private var bottomSheetCourierInactiveState: Int = 0

    private var whitelabelCoachmark: CoachMark2? = null

    private var shippingEditorOnDemandAdapter = ShippingEditorItemAdapter(this, this)
    private var shippingEditorConventionalAdapter = ShippingEditorItemAdapter(this, this)
    private var finishToastJob: Job? = null
    private var binding by autoClearedNullable<FragmentShippingEditorNewBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerShippingEditorComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShippingEditorNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        initViews()
        initAdapter()
        initObserver()
    }

    override fun onPause() {
        whitelabelCoachmark?.dismissCoachMark()
        whitelabelCoachmark = null

        super.onPause()
    }

    private fun initViews() {
        renderTickerOnDemand()
        renderTextDetailCourier()
        binding?.run {
            btnSaveShipper.setOnClickListener { saveButtonShippingEditor() }
        }
    }

    private fun showDropOffMaps(): Boolean {
        val dropoffMapsRollence = RemoteConfigInstance.getInstance().abTestPlatform.getString(
            DISABLE_DROPOFF_MAPS,
            ""
        )
        return dropoffMapsRollence != DISABLE_DROPOFF_MAPS
    }

    private fun renderTickerOnDemand() {
        SpannableString(getString(R.string.awb_otomatis_list))
        binding?.tickerDijemputKurir?.setHtmlDescription(getString(R.string.ticker_dijemput_kurir_complete))
        binding?.tickerDijemputKurir?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl == STATE_AWB_VALIDATION) {
                    bottomSheetShipperInfoType = 1
                    openBottomSheetShipperInfo()
                } else {
                    bottomSheetShipperInfoType = 2
                    openBottomSheetShipperInfo()
                }
            }

            override fun onDismiss() {
                // no-op
            }
        })
    }

    private fun renderTextDetailCourier() {
        val textDetailCourier = MethodChecker.fromHtml(getString(R.string.tv_detail_kurir))
        val selengkapnyaButton = getString(R.string.selengkapnya)
        val spannableString = SpannableString(textDetailCourier)
        val color = getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            spannableString.length - selengkapnyaButton.length,
            spannableString.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            spannableString.length - selengkapnyaButton.length,
            spannableString.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        binding?.tvDetailKurir?.text = spannableString
        binding?.tvDetailKurir?.setOnClickListener {
            viewModel.getShipperDetail()
        }
    }

    private fun initAdapter() {
        binding?.rvOnDemand?.adapter = shippingEditorOnDemandAdapter
        binding?.rvConventional?.adapter = shippingEditorConventionalAdapter
        binding?.rvOnDemand?.layoutManager = LinearLayoutManager(context)
        binding?.rvConventional?.layoutManager = LinearLayoutManager(context)
    }

    private fun initObserver() {
        viewModel.shipperList.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ShippingEditorState.Success -> {
                    updateData(it.data.shippers)
                    renderTicker(it.data.ticker)
                    checkWhitelabelCoachmarkState()
                    renderDropOffButton(it.data.dropOffMapsUrl)
                    updateHeaderTickerData(it.data.tickerHeader)
                }

                is ShippingEditorState.Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                else -> {
                    binding?.shippingEditorLayout?.gone()
                    binding?.btnSaveShipper?.gone()
                    binding?.swipeRefresh?.isRefreshing = true
                }
            }
        }

        viewModel.shipperTickerList.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ShippingEditorState.Success -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    binding?.shippingEditorLayout?.visible()
                    binding?.btnSaveShipper?.visible()
                    binding?.globalError?.gone()
                    updateHeaderTickerData(it.data.headerTicker)
                }

                else -> {
                    // no-op
                }
            }
        }

        viewModel.shipperDetail.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ShippingEditorState.Success -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    updateBottomsheetData(it.data)
                }

                is ShippingEditorState.Fail -> binding?.swipeRefresh?.isRefreshing = false
                else -> binding?.swipeRefresh?.isRefreshing = true
            }
        }

        viewModel.validateDataShipper.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ShippingEditorState.Success -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    validateSaveData(it.data)
                }

                is ShippingEditorState.Fail -> {
                    showToaster(it.errorMessage, Toaster.TYPE_ERROR)
                    binding?.swipeRefresh?.isRefreshing = false
                }
                else -> binding?.swipeRefresh?.isRefreshing = true
            }
        }

        viewModel.saveShippingData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ShippingEditorState.Success -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    finishToastJob?.cancel()
                    finishToastJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(FINISH_ACTIVITY_DELAY)
                        activity?.finish()
                    }
                    showToaster(getString(R.string.success_save_shipping), Toaster.TYPE_NORMAL)
                }

                is ShippingEditorState.Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    showToaster(it.errorMessage, Toaster.TYPE_ERROR)
                }
                else -> binding?.swipeRefresh?.isRefreshing = true
            }
        }
    }

    private fun renderDropOffButton(dropOffMapsUrl: String) {
        binding?.buttonDropOff?.run {
            if (dropOffMapsUrl.isNotEmpty()) {
                setDrawable(getIconUnifyDrawable(context, IconUnify.LOCATION))
                setOnClickListener {
                    RouteManager.route(context, generateWebviewApplink(dropOffMapsUrl))
                }
                visible()
            } else {
                gone()
            }
        }
    }

    private fun showToaster(error: String, type: Int) {
        view?.let { view ->
            Toaster.build(
                view,
                error,
                Toaster.LENGTH_SHORT,
                type = type
            ).show()
        }
    }

    private fun fetchData() {
        viewModel.getShipperList(userSession.shopId.toLong())
    }

    private fun updateData(data: ShipperGroupModel) {
        shippingEditorOnDemandAdapter.updateData(data.onDemand)
        shippingEditorConventionalAdapter.updateData(data.conventional)
    }

    private fun updateBottomsheetData(data: ShipperDetailModel) {
        bottomSheetShipperAdapter.setShippingEditorDetailsData(data)
        context?.let {
            ShipperDetailBottomSheet().show(it, this, bottomSheetShipperAdapter)
        }
    }

    private fun setDataCourierNotCovered(data: ValidateShippingEditorModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE
        bottomSheetCourierInactiveAdapter.setData(data.uiContent.warehouses)
        context?.let { openBottomSheetValidateCourierNotCovered(it, data) }
    }

    private fun setDataBoAndCourierNotCovered(data: ValidateShippingEditorModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_BO_STATE
        bottomSheetCourierInactiveAdapter.setData(data.uiContent.warehouses)
        context?.let { openBottomSheetValidateCourierNotCovered(it, data) }
    }

    private fun updateHeaderTickerData(data: HeaderTickerModel) {
        if (data.isActive) {
            binding?.tickerHeader?.apply {
                visibility = View.VISIBLE
                tickerTitle = data.header
                setHtmlDescription(data.body + getString(R.string.ticker_header_clicked))
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        bottomSheetCourierInactiveState =
                            BOTTOMSHEET_HEADER_WAREHOUSE_INACTIVE_STATE
                        bottomSheetCourierInactiveAdapter.setData(data.warehouseModel)
                        openBottomSheetWarehouseInactive(context, data.warehouseModel, "")
                    }

                    override fun onDismiss() {
                        // no-op
                    }
                })
            }
        } else {
            binding?.tickerHeader?.gone()
        }
    }

    private fun renderTicker(tickers: List<TickerModel>) {
        val messages = ArrayList<TickerData>()
        if (tickers.isNotEmpty()) {
            for (item in tickers) {
                val spannableString = SpannableString(item.body + " " + item.textLink)
                val color = getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                spannableString.setSpan(
                    ForegroundColorSpan(color),
                    spannableString.length - item.textLink.length,
                    spannableString.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
                messages.add(
                    TickerData(
                        item.header,
                        item.body + " " + item.textLink,
                        Ticker.TYPE_ANNOUNCEMENT,
                        true,
                        item.urlLink
                    )
                )
            }
            val tickerPageAdapter = TickerPagerAdapter(context, messages)
            binding?.tickerShipperInfo?.addPagerView(tickerPageAdapter, messages)
            tickerPageAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    val appLink = itemData.toString()
                    if (appLink.startsWith("tokopedia")) {
                        startActivity(RouteManager.getIntent(context, appLink))
                    } else {
                        goToWebView(appLink)
                    }
                }
            })
            binding?.tickerShipperInfo?.visible()
        } else {
            binding?.tickerShipperInfo?.gone()
        }
    }

    private fun validateSaveData(data: ValidateShippingEditorModel) {
        if (data.state == VALIDATE_MULTIPLE_LOC_STATE) {
            setDataCourierNotCovered(data)
        } else if (data.state == VALIDATE_BO_MULTIPLE_LOC_STATE) {
            setDataBoAndCourierNotCovered(data)
        } else if (data.state == VALIDATE_BEBAS_ONGKIR_STATE) {
            context?.let { openBottomSheetValidateBOData(it, data) }
        } else {
            viewModel.saveShippingData(
                userSession.shopId.toLong(),
                getListActivatedSpIds(
                    shippingEditorConventionalAdapter.getActiveSpIds(),
                    shippingEditorOnDemandAdapter.getActiveSpIds()
                ),
                convertFeatureIdToString(data.featureId)
            )
        }
    }

    private fun convertFeatureIdToString(featureId: List<Long>?): String? {
        return featureId?.joinToString(separator = ",")
    }

    private fun openBottomSheetWarehouseInactive(
        ctx: Context,
        data: List<WarehousesModel>,
        shipperName: String
    ) {
        bottomSheetCourierInactive = BottomSheetUnify()
        val viewBottomSheetWarehouseInactive =
            BottomsheetCourierInactiveBinding.inflate(LayoutInflater.from(ctx), null, false)
        setupChildCourierInactive(viewBottomSheetWarehouseInactive, shipperName, data.size, null)

        bottomSheetCourierInactive?.apply {
            setTitle(ctx.getString(R.string.title_bottomsheet_courier_inactive, data.size))
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetWarehouseInactive.root)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetCourierInactive?.show(it, "show")
        }
    }

    private fun openBottomSheetValidateCourierNotCovered(
        ctx: Context,
        data: ValidateShippingEditorModel
    ) {
        val uiContentModel = data.uiContent
        bottomSheetCourierInactive = BottomSheetUnify()
        val viewBottomSheetWarehouseInactive =
            BottomsheetCourierInactiveBinding.inflate(LayoutInflater.from(ctx), null, false)
        setupChildCourierInactive(
            viewBottomSheetWarehouseInactive,
            uiContentModel.headerLocation,
            uiContentModel.warehouses.size,
            data
        )

        if (bottomSheetCourierInactiveState == BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE) {
            bottomSheetCourierInactive?.setTitle(data.uiContent.header)
        } else {
            bottomSheetCourierInactive?.setTitle(getString(R.string.bottomsheet_inactive_title))
        }

        bottomSheetCourierInactive?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetWarehouseInactive.root)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetCourierInactive?.show(it, "show")
        }
    }

    private fun openBottomSheetValidateBOData(context: Context, data: ValidateShippingEditorModel) {
        bottomSheetBOValidation = BottomSheetUnify()
        bottomSheetBOValidation?.setTitle(getString(R.string.bottomsheet_validation_title))
        val viewBottomSheetBOValidation =
            PopupValidationBoBinding.inflate(LayoutInflater.from(context), null, false)
        setUpChildBottomSheetValidateBOData(viewBottomSheetBOValidation, data)

        bottomSheetBOValidation?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetBOValidation.root)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetBOValidation?.show(it, "show")
        }
    }

    private fun setUpChildBottomSheetValidateBOData(
        child: PopupValidationBoBinding,
        data: ValidateShippingEditorModel
    ) {
        val uiContentModel = data.uiContent

        child.tickerValidationBo.apply {
            tickerTitle = uiContentModel.ticker.header
            setHtmlDescription(
                uiContentModel.ticker.body + HtmlLinkHelper(
                    context,
                    uiContentModel.ticker.textLink
                ).spannedString.toString()
            )
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.getIntent(
                        activity,
                        generateWebviewApplink(
                            uiContentModel.ticker.urlLink
                        )
                    )
                }

                override fun onDismiss() {
                    // no-op
                }
            })
        }
        if (uiContentModel.body.isNotEmpty()) {
            child.rvValidationBo.run {
                val shipperAdapter = ShipperValidationBoAdapter()
                shipperAdapter.setData(uiContentModel.body)
                adapter = shipperAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
        child.btnNonaktifkan.setOnClickListener {
            viewModel.saveShippingData(
                userSession.shopId.toLong(),
                getListActivatedSpIds(
                    shippingEditorConventionalAdapter.getActiveSpIds(),
                    shippingEditorOnDemandAdapter.getActiveSpIds()
                ),
                convertFeatureIdToString(data.featureId)
            )
            bottomSheetBOValidation?.dismiss()
        }
        child.btnAktifkan.setOnClickListener {
            bottomSheetBOValidation?.dismiss()
        }
    }

    private fun generateWebviewApplink(url: String): String {
        return "${ApplinkConst.WEBVIEW}?titlebar=false&url=$url"
    }

    private fun setupChildCourierInactive(
        child: BottomsheetCourierInactiveBinding,
        header: String,
        courierCount: Int?,
        data: ValidateShippingEditorModel?
    ) {
        child.rvWarehouseInactive.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = bottomSheetCourierInactiveAdapter
        }

        when (bottomSheetCourierInactiveState) {
            BOTTOMSHEET_SHIPPER_WAREHOUSE_INACTIVE_STATE -> {
                showBottomSheetShipperWarehouseInactive(child, header, courierCount)
            }
            BOTTOMSHEET_HEADER_WAREHOUSE_INACTIVE_STATE -> {
                showBottomSheetHeaderWarehouseInactive(child)
            }
            BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_STATE -> {
                showBottomSheetValidateWarehouseInactive(child, data)
            }
            BOTTOMSHEET_VALIDATE_WAREHOUSE_INACTIVE_BO_STATE -> {
                showBottomSheetValidateWarehouseInactiveBO(child, courierCount, data)
            }
        }
    }

    private fun showBottomSheetShipperWarehouseInactive(
        child: BottomsheetCourierInactiveBinding,
        header: String,
        courierCount: Int?
    ) {
        child.tvCourierInactive.text =
            getString(R.string.text_header_courier_not_covered, header, courierCount)
        child.btnPrimary.text = getString(R.string.button_understand)
        child.btnPrimary.setOnClickListener {
            bottomSheetCourierInactive?.dismiss()
        }
        child.btnVerticalLayout.visible()
        child.btnSecondary.gone()
        child.btnHorizonalLayout.gone()
        child.tickerChargeBo.gone()
    }

    private fun showBottomSheetHeaderWarehouseInactive(child: BottomsheetCourierInactiveBinding) {
        child.tvCourierInactive.text = getString(R.string.text_header_courier_not_covered_all)
        child.btnPrimary.text = getString(R.string.button_understand)
        child.btnPrimary.setOnClickListener {
            bottomSheetCourierInactive?.dismiss()
        }
        child.btnVerticalLayout.visible()
        child.btnSecondary.gone()
        child.btnHorizonalLayout.gone()
        child.tickerChargeBo.gone()
    }

    private fun showBottomSheetValidateWarehouseInactive(
        child: BottomsheetCourierInactiveBinding,
        data: ValidateShippingEditorModel?
    ) {
        child.tvCourierInactive.text = data?.uiContent?.headerLocation
        child.btnPrimary.text = getString(R.string.button_cancel_reset)
        child.btnPrimary.setOnClickListener {
            bottomSheetCourierInactive?.dismiss()
        }
        child.btnSecondary.text = getString(R.string.button_save)
        child.btnSecondary.setOnClickListener {
            viewModel.saveShippingData(
                userSession.shopId.toLong(),
                getListActivatedSpIds(
                    shippingEditorConventionalAdapter.getActiveSpIds(),
                    shippingEditorOnDemandAdapter.getActiveSpIds()
                ),
                convertFeatureIdToString(data?.featureId)
            )
            bottomSheetCourierInactive?.dismiss()
        }
        child.btnVerticalLayout.visible()
        child.btnSecondary.visible()
        child.btnHorizonalLayout.gone()
        child.tickerChargeBo.gone()
    }

    private fun showBottomSheetValidateWarehouseInactiveBO(
        child: BottomsheetCourierInactiveBinding,
        courierCount: Int?,
        data: ValidateShippingEditorModel?
    ) {
        child.tvCourierInactive.text =
            getString(R.string.text_header_validate_courier_not_covered, courierCount)
        child.btnPrimaryHorizontal.text = getString(R.string.button_deactivate)
        child.btnPrimaryHorizontal.setOnClickListener {
            viewModel.saveShippingData(
                userSession.shopId.toLong(),
                getListActivatedSpIds(
                    shippingEditorConventionalAdapter.getActiveSpIds(),
                    shippingEditorOnDemandAdapter.getActiveSpIds()
                ),
                convertFeatureIdToString(data?.featureId)
            )
            bottomSheetCourierInactive?.dismiss()
        }
        child.btnSecondaryHorizonal.text = getString(R.string.button_activate)
        child.btnSecondaryHorizonal.setOnClickListener {
            bottomSheetCourierInactive?.dismiss()
        }
        child.tickerChargeBo.apply {
            tickerTitle = data?.uiContent?.ticker?.header
            setHtmlDescription(data?.uiContent?.ticker?.body + data?.uiContent?.ticker?.textLink)
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    goToWebView(data?.uiContent?.ticker?.urlLink)
                }

                override fun onDismiss() {
                    // no-op
                }
            })
        }
        child.btnVerticalLayout.gone()
        child.btnHorizonalLayout.visible()
        child.tickerChargeBo.visible()
    }

    private fun goToWebView(url: String?) {
        if (activity != null && url != null) {
            startActivity(
                RouteManager.getIntent(
                    activity,
                    generateWebviewApplink(url)
                )
            )
        }
    }

    private fun openBottomSheetFeatureInfo() {
        bottomSheetFeatureInfo = BottomSheetUnify()
        val viewBottomSheetFeatureInfo =
            BottomsheetShipperDetailBinding.inflate(LayoutInflater.from(context), null, false)
        setupFeatureChild(viewBottomSheetFeatureInfo)

        bottomSheetFeatureInfo?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetFeatureInfo.root)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetFeatureInfo?.show(it, "show")
        }
    }

    private fun setupFeatureChild(child: BottomsheetShipperDetailBinding) {
        child.rvShipperDetail.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = bottomSheetFeatureInfoAdapter
        }
    }

    private fun openBottomSheetShipperInfo() {
        bottomSheetShipperInfo = BottomSheetUnify()
        val viewBottomSheetShipperInfo =
            BottomsheetShipperInfoBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetShipperInfoChild(viewBottomSheetShipperInfo)

        bottomSheetShipperInfo?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetShipperInfo.root)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetShipperInfo?.show(it, "show")
        }
    }

    private fun setupBottomSheetShipperInfoChild(child: BottomsheetShipperInfoBinding) {
        if (bottomSheetShipperInfoType == BOTTOMSHEET_AWB_OTOMATIS_INFO) {
            child.imgInfoCourier.setImageUrl(TokopediaImageUrl.IMG_EDITSHIPPING_AWB_OTOMATIS)
            child.tvInfoCourier.text = getString(R.string.awb_otomatis_title)
            child.tvInfoCourierDetail.text = getString(R.string.awb_otomatis_detail)
        } else {
            child.imgInfoCourier.setImageUrl(TokopediaImageUrl.IMG_EDITSHIPPING_NON_TUNAI)
            child.tvInfoCourier.text = getString(R.string.non_tunai_title)
            child.tvInfoCourierDetail.text = getString(R.string.non_tunai_detail)
        }
        child.btnClose.setOnClickListener { bottomSheetShipperInfo?.dismiss() }
    }

    private fun saveButtonShippingEditor() {
        val activatedSpIds = getListActivatedSpIds(
            shippingEditorOnDemandAdapter.getActiveSpIds(),
            shippingEditorConventionalAdapter.getActiveSpIds()
        )
        if (activatedSpIds.isEmpty()) {
            showToaster(EditShippingConstant.DEFAULT_ERROR_SHIPPING_EDITOR, Toaster.TYPE_ERROR)
        } else {
            viewModel.validateShippingEditor(userSession.shopId.toLong(), activatedSpIds)
        }
    }

    private fun getListActivatedSpIds(
        onDemandList: List<String>,
        conventionalList: List<String>
    ): String {
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
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
                        GlobalError.NO_CONNECTION
                    )
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        showGlobalError(GlobalError.SERVER_ERROR)
                        showToaster(EditShippingConstant.DEFAULT_ERROR_MESSAGE, Toaster.TYPE_ERROR)
                    }
                }
            }
            else -> {
                showGlobalError(GlobalError.SERVER_ERROR)
                if (throwable.message?.contains(ERROR_CODE_NO_ACCESS) == true) {
                    showToaster(getString(R.string.txt_error_no_access), Toaster.TYPE_ERROR)
                } else {
                    showToaster(
                        throwable.message
                            ?: EditShippingConstant.DEFAULT_ERROR_MESSAGE,
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        binding?.globalError?.setType(type)
        binding?.globalError?.setActionClickListener {
            fetchData()
        }
        binding?.shippingEditorLayout?.gone()
        binding?.btnSaveShipper?.gone()
        binding?.globalError?.visible()
    }

    override fun onShipperTickerClicked(data: ShipperModel) {
        bottomSheetCourierInactiveState = BOTTOMSHEET_SHIPPER_WAREHOUSE_INACTIVE_STATE
        bottomSheetCourierInactiveAdapter.setData(data.warehouseModel)
        context?.let { openBottomSheetWarehouseInactive(it, data.warehouseModel, data.shipperName) }
    }

    private fun getWhitelabelView(): View? {
        val whitelabelServiceIndex = shippingEditorOnDemandAdapter.getWhitelabelServicePosition()
        return if (whitelabelServiceIndex != RecyclerView.NO_POSITION) {
            binding?.rvOnDemand?.findViewHolderForAdapterPosition(whitelabelServiceIndex)?.itemView
        } else {
            null
        }
    }

    private fun getNormalServiceView(): View? {
        val normalServiceIndex = shippingEditorOnDemandAdapter.getFirstNormalServicePosition()
        return if (normalServiceIndex != RecyclerView.NO_POSITION) {
            binding?.rvOnDemand?.findViewHolderForAdapterPosition(normalServiceIndex)?.itemView
        } else {
            null
        }
    }

    private fun checkWhitelabelCoachmarkState() {
        context?.let {
            val sharedPref = WhitelabelInstanCoachMarkSharePref(it)
            if (sharedPref.getCoachMarkState() == true) {
                Handler(Looper.getMainLooper()).postDelayed({
                    showOnBoardingCoachmark(sharedPref)
                }, COACHMARK_ON_BOARDING_DELAY)
            }
        }
    }

    private fun showOnBoardingCoachmark(sharedPref: WhitelabelInstanCoachMarkSharePref) {
        context?.let {
            val whitelabelView = getWhitelabelView()
            if (whitelabelView != null) {
                val normalServiceView = getNormalServiceView()
                val coachMarkItems =
                    generateOnBoardingCoachMark(it, normalServiceView, whitelabelView)

                CoachMark2(it).apply {
                    setOnBoardingListener(coachMarkItems)
                    setStateAfterOnBoardingShown(coachMarkItems, sharedPref)
                    manualScroll(coachMarkItems)
                }
            }
        }
    }

    private fun generateOnBoardingCoachMark(
        context: Context,
        normalService: View?,
        whitelabelService: View
    ): ArrayList<CoachMark2Item> {
        val coachMarkItems = ArrayList<CoachMark2Item>()
        normalService?.let { view ->
            coachMarkItems.add(
                CoachMark2Item(
                    view,
                    context.getString(R.string.whitelabel_onboarding_title_coachmark),
                    context.getString(R.string.whitelabel_onboarding_description_coachmark),
                    CoachMark2.POSITION_TOP
                )
            )
        }

        whitelabelService.let { view ->
            coachMarkItems.add(
                CoachMark2Item(
                    view,
                    context.getString(R.string.whitelabel_instan_title_coachmark),
                    context.getString(R.string.whitelabel_instan_description_coachmark),
                    CoachMark2.POSITION_TOP
                )
            )
        }
        return coachMarkItems
    }

    private fun CoachMark2.setOnBoardingListener(coachMarkItems: ArrayList<CoachMark2Item>) {
        this.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                this@setOnBoardingListener.hideCoachMark()
                manualScroll(coachMarkItems, currentIndex)
            }
        })
    }

    private fun CoachMark2.manualScroll(
        coachMarkItems: ArrayList<CoachMark2Item>,
        currentIndex: Int = 0
    ) {
        coachMarkItems.getOrNull(currentIndex)?.anchorView?.let { rv ->
            binding?.svShippingEditor?.smoothScrollTo(0, rv.top)
            this.showCoachMark(coachMarkItems, null, currentIndex)
        }
    }

    private fun CoachMark2.setStateAfterOnBoardingShown(
        coachMarkItems: ArrayList<CoachMark2Item>,
        sharedPref: WhitelabelInstanCoachMarkSharePref
    ) {
        if (coachMarkItems.size > 1) {
            this.onFinishListener = {
                sharedPref.setCoachMarkState(false)
            }
        } else if (coachMarkItems.isNotEmpty()) {
            sharedPref.setCoachMarkState(false)
        }
    }

    override fun onFeatureInfoClicked(data: List<FeatureInfoModel>) {
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

        private const val COACHMARK_ON_BOARDING_DELAY = 1000L
        private const val FINISH_ACTIVITY_DELAY = 2000L

        private const val STATE_AWB_VALIDATION = "awb_otomatis"
        private const val ERROR_CODE_NO_ACCESS = "555"
    }
}
