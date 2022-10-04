package com.tokopedia.manageaddress.ui.shoplocation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.shoplocation.GeneralTickerModel
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.BottomsheetActionShopAddressBinding
import com.tokopedia.manageaddress.databinding.BottomsheetDeactivateLocationBinding
import com.tokopedia.manageaddress.databinding.BottomsheetMainAddressInformationBinding
import com.tokopedia.manageaddress.databinding.FragmentShopLocationBinding
import com.tokopedia.manageaddress.di.ShopLocationComponent
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
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
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

    private var binding by autoClearedNullable<FragmentShopLocationBinding>()

    private var bottomSheetAddressType: BottomSheetUnify? = null
    private var bottomSheetAddressConfirmation: BottomSheetUnify? = null
    private var bottomSheetMainLocationInfo: BottomSheetUnify? = null
    private var warehouseName: String = ""
    private var warehouseStatus: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShopLocationBinding.inflate(inflater, container, false)
        return binding?.root
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
        binding?.addressList?.adapter = adapter
        binding?.addressList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initViewModel() {
        viewModel.shopWhitelist.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopLocationState.Success -> {
                    if (it.data.data.eligibilityState == ELIGIBLE_USER_WHITELIST_STATE) {
                        binding?.swipeRefresh?.isRefreshing = false
                        fetchData()
                    } else {
                        activity?.finish()
                        context?.let { ctx ->
                            val intent = getSellerSettingsIntent(ctx)
                            startActivityForResult(intent, INTENT_SHOP_SETTING_ADDRESS_OLD)
                        }
                    }
                }

                is ShopLocationState.Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                else -> {
                    binding?.swipeRefresh?.isRefreshing = true
                }
            }
        })

        viewModel.shopLocation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopLocationState.Success -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    binding?.globalError?.gone()
                    updateData(it.data.listWarehouse)
                    setGeneralTicker(it.data.generalTicker)
                }

                is ShopLocationState.Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                else -> {
                    binding?.swipeRefresh?.isRefreshing = true
                }
            }
        })

        viewModel.result.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopLocationState.Success -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    if (warehouseStatus == STATE_WAREHOUSE_ACTIVE) {
                        view?.let { view -> Toaster.build(view, getString(R.string.text_deactivate_success, warehouseName), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
                    } else {
                        view?.let { view -> Toaster.build(view, getString(R.string.text_activate_success, warehouseName), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
                    }
                    viewModel.getShopLocationList(userSession.shopId.toLong())
                }

                is ShopLocationState.Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    view?.let { view -> Toaster.build(view, ManageAddressConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show() }
                }

                else -> {
                    binding?.swipeRefresh?.isRefreshing = true
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

    private fun getSellerSettingsIntent(context: Context) : Intent {
        return if (GlobalConfig.isSellerApp()) {
                RouteManager.getIntent(context, ApplinkConstInternalSellerapp.MENU_SETTING)
            } else {
                RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID, userSession.shopId)
            }

    }

    private fun setGeneralTicker(data: GeneralTickerModel) {
        if (data.header.isNotEmpty()) {
            binding?.tickerShopLocation?.apply {
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
            binding?.tickerShopLocation?.visibility = View.GONE
        }
    }

    private fun openBottomSheetAddressType(shopLocation: Warehouse) {
        bottomSheetAddressType = BottomSheetUnify()
        val viewBottomSheetAddressType = BottomsheetActionShopAddressBinding.inflate(LayoutInflater.from(context), null, false)
        setupChild(shopLocation, viewBottomSheetAddressType)

        bottomSheetAddressType?.apply {
            setTitle(BOTTOMSHEET_TITLE_ATUR_LOKASI)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetAddressType.root)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetAddressType?.show(it, "show")
        }
    }

    private fun setupChild(data: Warehouse, child: BottomsheetActionShopAddressBinding) {
        if (data.status == STATE_WAREHOUSE_ACTIVE) {
            child.btnSetLocationStatus.text = getString(R.string.deactivate_location)
        } else if (data.status == STATE_WAREHOUSE_INACTIVE)   {
            child.btnSetLocationStatus.text = getString(R.string.activate_location)
        }

        child.btnSetLocationStatus.setOnClickListener {
            if (data.status == STATE_WAREHOUSE_ACTIVE) {
                openBottomSheetAddressConfirmation(data)
            } else {
                warehouseStatus = data.status
                bottomSheetAddressType?.dismiss()
                viewModel.setShopLocationState(data.warehouseId, 1)
            }
        }
    }

    private fun openBottomSheetAddressConfirmation(shopLocation: Warehouse) {
        bottomSheetAddressConfirmation = BottomSheetUnify()
        val viewBottomSheetAddressConfirmation = BottomsheetDeactivateLocationBinding.inflate(LayoutInflater.from(context), null, false)
        setupChildAddressConfirmation(shopLocation, viewBottomSheetAddressConfirmation)

        bottomSheetAddressConfirmation?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetAddressConfirmation.root)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetAddressConfirmation?.show(it, "show")
        }
    }

    private fun setupChildAddressConfirmation(data: Warehouse, child: BottomsheetDeactivateLocationBinding) {
        child.tvDeactivateLocation.text = getString(R.string.text_deactivate_confirmation, data.warehouseName)
        child.btnKembali.setOnClickListener { bottomSheetAddressConfirmation?.dismiss() }
        child.btnNonaktifkan.setOnClickListener {
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
        binding?.globalError?.setType(type)
        binding?.globalError?.setActionClickListener {
            viewModel.getShopLocationList(userSession.shopId.toLong())
        }
        binding?.addressList?.gone()
        binding?.globalError?.visible()
    }

    override fun onShopLocationStateStatusClicked(data: Warehouse) {
        openBottomSheetAddressType(data)
    }

    override fun onShopEditAddress(data: Warehouse) {
        openFormShopEditAddress(data)
    }

    override fun onImageMainInfoIconClicked() {
        bottomSheetMainLocationInfo = BottomSheetUnify()
        val viewBottomSheetMainLocation = BottomsheetMainAddressInformationBinding.inflate(LayoutInflater.from(context), null, false)
        setupChildMainInfo(viewBottomSheetMainLocation)

        bottomSheetMainLocationInfo?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetMainLocation.root)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetMainLocationInfo?.show(it, "show")
        }
    }

    private fun setupChildMainInfo(child: BottomsheetMainAddressInformationBinding) {
        child.tickerAddressInfo.setHtmlDescription(getString(R.string.ticker_main_info))
        child.btnMengertiInfo.setOnClickListener {
            bottomSheetMainLocationInfo?.dismiss()
        }
    }

    private fun openFormShopEditAddress(data: Warehouse) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.SHOP_EDIT_ADDRESS)
        data.latLon.checkLatlon { latitude, longitude ->
            intent.putExtra(EXTRA_LAT, latitude)
            intent.putExtra(EXTRA_LONG, longitude)
        }
        intent.putExtra(EXTRA_WAREHOUSE_DATA, data)
        startActivityForResult(intent, EDIT_WAREHOUSE_REQUEST_CODE)
    }

    private fun String?.checkLatlon(latLon: (latitude: Double, longitude: Double) -> Unit) {
        this?.takeIf { isNotEmpty() } ?.apply {
            val latitude = substringBefore(",").toDoubleOrNull()
            val longitude = substringAfter(",").toDoubleOrNull()

            if (latitude != null && longitude != null) {
                latLon.invoke(latitude, longitude)
            }
        }
    }

    companion object {
        const val ELIGIBLE_USER_WHITELIST_STATE = 1

        const val STATE_WAREHOUSE_ACTIVE = 1
        const val STATE_WAREHOUSE_INACTIVE = 2
    }

}
