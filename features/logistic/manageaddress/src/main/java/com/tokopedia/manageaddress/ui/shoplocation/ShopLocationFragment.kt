package com.tokopedia.manageaddress.ui.shoplocation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.shoplocation.GeneralTickerModel
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.shoplocation.ShopLocationComponent
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationState
import com.tokopedia.manageaddress.ui.shoplocation.shopaddress.ShopSettingsAddressActivity
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.util.ManageAddressConstant.BOTTOMSHEET_TITLE_ATUR_LOKASI
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_LAT
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_LONG
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_WAREHOUSE_DATA
import com.tokopedia.manageaddress.util.ShopLocationConstant.EDIT_WAREHOUSE_REQUEST_CODE
import com.tokopedia.manageaddress.util.ShopLocationConstant.ERROR_CODE_NO_ACCESS
import com.tokopedia.manageaddress.util.ShopLocationConstant.INTENT_SHOP_SETTING_ADDRESS_OLD
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShopLocationFragment : BaseDaggerFragment(), ShopLocationItemAdapter.ShopLocationItemAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface

    private val adapter = ShopLocationItemAdapter(this)

    private val viewModel: ShopLocationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopLocationViewModel::class.java)
    }

    private var tickerShopLocation: Ticker? = null
    private var addressList: RecyclerView? = null
    private var bottomSheetAddressType: BottomSheetUnify? = null
    private var bottomSheetAddressConfirmation: BottomSheetUnify? = null
    private var bottomSheetMainLocationInfo: BottomSheetUnify? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var globalErrorLayout: GlobalError? = null
    private var buttonSetLocationStatus: Typography? = null
    private var buttonBackAddressConfirm: UnifyButton? = null
    private var buttonDeactivateAddressConfirm: UnifyButton? = null
    private var buttonMengerti: UnifyButton? = null
    private var tickerMainLocationInfo: Ticker? = null
    private var warehouseName: String = ""
    private var warehouseStatus: Int? = null
    private var tvDeactivateConfirmation: Typography? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_location, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopLocationComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkWhitelistedUser()
        initViews()
        initViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_WAREHOUSE_REQUEST_CODE ) {
            viewModel.getShopLocationList(userSession?.shopId.toLong())
        }
    }

    private fun checkWhitelistedUser() {
        viewModel.getWhitelistData(userSession?.shopId.toLong())
    }

    private fun initViews() {
        tickerShopLocation = view?.findViewById(R.id.ticker_shop_location)
        addressList = view?.findViewById(R.id.address_list)
        globalErrorLayout = view?.findViewById(R.id.global_error)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)

        addressList?.adapter = adapter
        addressList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initViewModel() {
        viewModel.shopWhitelist.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopLocationState.Success -> {
                    if (it.data.data.eligibilityState == ELIGIBLE_USER_WHITELIST_STATE) {
                        swipeRefreshLayout?.isRefreshing = false
                        fetchData()
                    } else {
                        activity?.finish()
                        val intent = context?.let { context -> ShopSettingsAddressActivity.createIntent(context) }
                        startActivityForResult(intent, INTENT_SHOP_SETTING_ADDRESS_OLD)
                    }
                }

                is ShopLocationState.Fail -> {
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

        viewModel.shopLocation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopLocationState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalErrorLayout?.gone()
                    updateData(it.data.listWarehouse)
                    setGeneralTicker(it.data.generalTicker)
                }

                is ShopLocationState.Fail -> {
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

        viewModel.result.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopLocationState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (warehouseStatus == STATE_WAREHOUSE_ACTIVE) {
                        view?.let { view -> Toaster.build(view, getString(R.string.text_deactivate_success, warehouseName), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
                    } else {
                        view?.let { view -> Toaster.build(view, getString(R.string.text_activate_success, warehouseName), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
                    }
                    viewModel.getShopLocationList(userSession.shopId.toLong())
                }

                is ShopLocationState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    view?.let { view -> Toaster.build(view, ManageAddressConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show() }
                }

                else -> {
                    swipeRefreshLayout?.isRefreshing = true
                }
            }
        })
    }

    private fun fetchData() {
        viewModel.getShopLocationList(userSession.shopId.toLong())
    }

    private fun updateData(data: List<Warehouse>) {
        adapter.clearData()
        adapter.addList(data)
    }

    private fun setGeneralTicker(data: GeneralTickerModel) {
        if (data.header.isNotEmpty()) {
            tickerShopLocation?.apply {
                tickerTitle = data.header
                visibility = View.VISIBLE
                setHtmlDescription(data.body + getString(R.string.general_ticker_link))
                setDescriptionClickEvent(object: TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        startActivity(RouteManager.getIntent(activity, String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, data.bodyLinkUrl)))
                    }

                    override fun onDismiss() {
                        //no-op
                    }

                })
            }
        } else {
            tickerShopLocation?.visibility = View.GONE
        }
    }

    private fun openBottomSheetAddressType(shopLocation: Warehouse) {
        bottomSheetAddressType = BottomSheetUnify()
        val viewBottomSheetAddressType = View.inflate(context, R.layout.bottomsheet_action_shop_address, null)
        setupChild(shopLocation, viewBottomSheetAddressType)

        bottomSheetAddressType?.apply {
            setTitle(BOTTOMSHEET_TITLE_ATUR_LOKASI)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetAddressType)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetAddressType?.show(it, "show")
        }
    }

    private fun setupChild(data: Warehouse, child: View) {
        buttonSetLocationStatus = child.findViewById(R.id.btn_set_location_status)
        if (data.status == STATE_WAREHOUSE_ACTIVE) {
            buttonSetLocationStatus?.text = getString(R.string.deactivate_location)
        } else if (data.status == STATE_WAREHOUSE_INACTIVE)   {
            buttonSetLocationStatus?.text = getString(R.string.activate_location)
        }

        buttonSetLocationStatus?.setOnClickListener {
            if (data.status == STATE_WAREHOUSE_ACTIVE) {
                openBottomSheetAddressConfirmation(data)
            } else {
                bottomSheetAddressType?.dismiss()
                viewModel.setShopLocationState(data.warehouseId, 1)
            }
        }
    }

    private fun openBottomSheetAddressConfirmation(shopLocation: Warehouse) {
        bottomSheetAddressConfirmation = BottomSheetUnify()
        val viewBottomSheetAddressConfirmation = View.inflate(context, R.layout.bottomsheet_deactivate_location, null)
        setupChildAddressConfirmation(shopLocation, viewBottomSheetAddressConfirmation)

        bottomSheetAddressConfirmation?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetAddressConfirmation)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetAddressConfirmation?.show(it, "show")
        }
    }

    private fun setupChildAddressConfirmation(data: Warehouse, child: View) {
        buttonBackAddressConfirm = child.findViewById(R.id.btn_kembali)
        buttonDeactivateAddressConfirm = child.findViewById(R.id.btn_nonaktifkan)
        tvDeactivateConfirmation = child.findViewById(R.id.tv_deactivate_location)

        tvDeactivateConfirmation?.text = getString(R.string.text_deactivate_confirmation, data.warehouseName)
        buttonBackAddressConfirm?.setOnClickListener { bottomSheetAddressConfirmation?.dismiss() }
        buttonDeactivateAddressConfirm?.setOnClickListener {
            viewModel.setShopLocationState(data.warehouseId, STATE_WAREHOUSE_INACTIVE)
            warehouseStatus = data.status
            warehouseName = data.warehouseName
            bottomSheetAddressConfirmation?.dismiss()
            bottomSheetAddressType?.dismiss()
        }
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
                            Toaster.build(it, ManageAddressConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    if (throwable.message?.contains(ERROR_CODE_NO_ACCESS) == true) {
                        Toaster.build(it, getString(R.string.txt_error_no_access), Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                    } else {
                        Toaster.build(it, throwable.message
                                ?: ManageAddressConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                    }
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            viewModel.getShopLocationList(userSession.shopId.toLong())
        }
        addressList?.gone()
        globalErrorLayout?.visible()
    }

    override fun onShopLocationStateStatusClicked(data: Warehouse) {
        openBottomSheetAddressType(data)
    }

    override fun onShopEditAddress(data: Warehouse) {
        openFormShopEditAddress(data)
    }

    override fun onImageMainInfoIconClicked() {
        bottomSheetMainLocationInfo = BottomSheetUnify()
        val viewBottomSheetMainLocation = View.inflate(context, R.layout.bottomsheet_main_address_information, null)
        setupChildMainInfo(viewBottomSheetMainLocation)

        bottomSheetMainLocationInfo?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetMainLocation)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetMainLocationInfo?.show(it, "show")
        }
    }

    private fun setupChildMainInfo(child: View) {
        buttonMengerti = child.findViewById(R.id.btn_mengerti_info)
        tickerMainLocationInfo = child.findViewById(R.id.ticker_address_info)

        tickerMainLocationInfo?.setHtmlDescription(getString(R.string.ticker_main_info))
        buttonMengerti?.setOnClickListener {
            bottomSheetMainLocationInfo?.dismiss()
        }
    }

    private fun openFormShopEditAddress(data: Warehouse) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.SHOP_EDIT_ADDRESS)
        if (!data.latLon.isNullOrEmpty()) {
            intent.putExtra(EXTRA_LAT, data.latLon.substringBefore(",").toDouble())
            intent.putExtra(EXTRA_LONG, data.latLon.substringAfter(",").toDouble())
        }
        intent.putExtra(EXTRA_WAREHOUSE_DATA, data)
        startActivityForResult(intent, EDIT_WAREHOUSE_REQUEST_CODE)
    }

    companion object {
        const val ELIGIBLE_USER_WHITELIST_STATE = 1

        const val STATE_WAREHOUSE_ACTIVE = 1
        const val STATE_WAREHOUSE_INACTIVE = 2
    }

}