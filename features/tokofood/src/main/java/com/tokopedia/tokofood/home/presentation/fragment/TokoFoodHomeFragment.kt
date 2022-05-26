package com.tokopedia.tokofood.home.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet.Companion.SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentTokofoodHomeBinding
import com.tokopedia.tokofood.home.di.DaggerTokoFoodHomeComponent
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.home.domain.data.USPResponse
import com.tokopedia.tokofood.home.presentation.adapter.CustomLinearLayoutManager
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapter
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapterTypeFactory
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodListDiffer
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeChooseAddressViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeEmptyStateLocationViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeIconsViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeUSPViewHolder
import com.tokopedia.tokofood.home.presentation.bottomsheet.TokoFoodUSPBottomSheet
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeBannerComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeLegoComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodView
import com.tokopedia.tokofood.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoFoodHomeFragment : BaseDaggerFragment(),
        IBaseMultiFragment,
        TokoFoodView,
        TokoFoodHomeUSPViewHolder.TokoFoodUSPListener,
        TokoFoodHomeChooseAddressViewHolder.TokoFoodChooseAddressWidgetListener,
        TokoFoodHomeEmptyStateLocationViewHolder.TokoFoodHomeEmptyStateLocationListener,
        TokoFoodHomeIconsViewHolder.TokoFoodHomeIconsListener,
        ChooseAddressBottomSheet.ChooseAddressBottomSheetListener
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentTokofoodHomeBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodHomeViewModel::class.java)
    }

    private val adapter by lazy {
        TokoFoodHomeAdapter(
            typeFactory = TokoFoodHomeAdapterTypeFactory(
                this,
                dynamicLegoBannerCallback = createLegoBannerCallback(),
                bannerComponentCallback = createBannerCallback(),
                categoryWidgetCallback = createCategoryWidgetCallback(),
                uspListener = this,
                chooseAddressWidgetListener = this,
                emptyStateLocationListener = this,
                homeIconListener = this,
            ),
            differ = TokoFoodListDiffer(),
        )
    }

    private val loadMoreListener by lazy { createLoadMoreListener() }

    companion object {
        private const val ITEM_VIEW_CACHE_SIZE = 20
        private const val REQUEST_CODE_CHANGE_ADDRESS = 111
        private const val REQUEST_CODE_SET_PINPOINT = 112
        private const val REQUEST_CODE_ADD_ADDRESS = 113
        private const val NEW_ADDRESS_PARCELABLE = "EXTRA_ADDRESS_NEW"

        private const val TOTO_LATITUDE = "-6.2216771"
        private const val TOTO_LONGITUDE = "106.8184023"
        private const val EMPTY_LOCATION = "0.0"

        const val SOURCE = "tokofood"

        fun createInstance(): TokoFoodHomeFragment {
            return TokoFoodHomeFragment()
        }
    }

    private var navToolbar: NavToolbar? = null
    private var rvHome: RecyclerView? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var localCacheModel: LocalCacheModel? = null
    private var movingPosition = 0
    private val spaceZero: Int
        get() = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0).toInt()

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodHomeComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun getFragmentTitle(): String? {
        return null
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodHomeBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupNavToolbar()
        setupRecycleView()
        setupSwipeRefreshLayout()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()

        loadLayout()
    }

    override fun onResume() {
        super.onResume()
        if (isChooseAddressWidgetDataUpdated()) {
            onRefreshLayout()
        }
    }

    override fun getFragmentPage(): Fragment = this

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() = onRefreshLayout()

    private fun createLegoBannerCallback(): TokoFoodHomeLegoComponentCallback {
        return TokoFoodHomeLegoComponentCallback(this)
    }

    private fun createBannerCallback(): TokoFoodHomeBannerComponentCallback {
        return TokoFoodHomeBannerComponentCallback(this)
    }

    private fun createCategoryWidgetCallback(): TokoFoodHomeCategoryWidgetV2ComponentCallback {
        return TokoFoodHomeCategoryWidgetV2ComponentCallback(this)
    }

    override fun onUSPClicked(uspResponse: USPResponse) {
        showUSPBottomSheet(uspResponse)
    }

    override fun onClickChooseAddressWidgetTracker() {

    }

    override fun onChooseAddressWidgetRemoved() {
        chooseAddressWidgetRemoved()
    }

    override fun onClickSetPinPoin() {
        navigateToSetPinpoint()
    }

    override fun onClickBackToHome() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    override fun onClickSetAddress() {
        checkUserEligibilityForAnaRevamp()
    }

    override fun onClickSetAddressInCoverage() {
        showChooseAddressBottomSheet()
    }

    override fun onAddressDataChanged() {
        refreshLayoutPage()
    }

    override fun getLocalizingAddressHostSourceBottomSheet(): String {
        return SOURCE
    }

    override fun onDismissChooseAddressBottomSheet() {}

    override fun onLocalizingAddressLoginSuccessBottomSheet() {}

    override fun onLocalizingAddressServerDown() {}

    override fun onClickHomeIcon(applink: String) {
        RouteManager.route(context, applink)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_ADD_ADDRESS -> onResultFromAddAddress(resultCode, data)
            REQUEST_CODE_SET_PINPOINT -> onResultFromSetPinpoint(resultCode, data)
        }
    }

    private fun showLayout() {
        getHomeLayout()
    }

    private fun getHomeLayout() {
        localCacheModel?.let {
            viewModel.getHomeLayout(it)
        }
    }

    private fun getLayoutComponentData() {
        viewModel.getLayoutComponentData()
    }

    private fun loadLayout() {
        viewModel.getLoadingState()
    }

    private fun showNoPinPoin() {
        viewModel.getNoPinPoinState()
    }

    private fun showNoAddress() {
        viewModel.getNoAddressState()
    }

    private fun getChooseAddress() {
        viewModel.getChooseAddress(SOURCE)
    }

    private fun checkUserEligibilityForAnaRevamp() {
        viewModel.checkUserEligibilityForAnaRevamp()
    }

    private fun setupUi() {
        view?.apply {
            navToolbar = binding?.navToolbar
            rvHome = binding?.rvHome
            swipeLayout = binding?.swipeRefreshLayout
        }
    }

    private fun setupRecycleView() {
        context?.let {
            rvHome?.apply {
                adapter = this@TokoFoodHomeFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }
        }

        rvHome?.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
    }

    private fun setupNavToolbar() {
        setupTopNavigation()
        setIconNavigation()
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            activity?.let {
                toolbar.showShadow(true)
                toolbar.setupToolbarWithStatusBar(it, applyPadding = false, applyPaddingNegative = true)
                toolbar.setToolbarTitle(getString(R.string.tokofood_title))
            }
        }
    }

    private fun setIconNavigation() {
        val icons =
            IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton)
                .addIcon(IconList.ID_LIST_TRANSACTION, onClick = ::onClickListTransactionButton)
                .addIcon(IconList.ID_NAV_GLOBAL, onClick = {})
        navToolbar?.setIcon(icons)
    }

    private fun onClickShareButton() {
        //TODO SHARE FUNC
    }

    private fun onClickListTransactionButton() {
        RouteManager.route(context, ApplinkConst.ORDER_LIST)
    }

    private fun observeLiveData() {
        observe(viewModel.layoutList) {
            removeAllScrollListener()
            when (it) {
                is Success -> onSuccessGetHomeLayout(it.data)
            }

            rvHome?.post {
                addScrollListener()
                resetSwipeLayout()
            }
        }

        observe(viewModel.updatePinPointState) { isSuccess ->
            if (isSuccess) {
                getChooseAddress()
            }
        }

        observe(viewModel.errorMessage) { message ->
            showToaster(message)
        }

        observe(viewModel.chooseAddress) {
            when(it) {
                is Success -> {
                    setupChooseAddress(it.data)
                }

                is Fail -> {
                    showToaster(it.throwable.message)
                }
            }
        }

        observe(viewModel.eligibleForAnaRevamp) {
            when(it) {
                is Success -> {
                    if (it.data.eligible) {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_REF, SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
                        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
                    } else {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_REF, SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
                        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
                    }
                }

                is Fail -> {
                    showToaster(it.throwable.message)
                }
            }
        }
    }

    private fun onSuccessGetHomeLayout(data: TokoFoodListUiModel) {
        when (data.state) {
            TokoFoodLayoutState.SHOW -> onShowHomeLayout(data)
            TokoFoodLayoutState.HIDE -> onHideHomeLayout(data)
            TokoFoodLayoutState.LOADING -> onLoadingHomelayout(data)
            else -> showHomeLayout(data)
        }
    }

    private fun onHideHomeLayout(data: TokoFoodListUiModel) {
        showHomeLayout(data)
    }

    private fun onShowHomeLayout(data: TokoFoodListUiModel) {
        showHomeLayout(data)
        getLayoutComponentData()
    }

    private fun onLoadingHomelayout(data: TokoFoodListUiModel) {
        showHomeLayout(data)
        checkAddressDataAndServiceArea()
    }

    private fun showHomeLayout(data: TokoFoodListUiModel) {
        rvHome?.post {
            adapter.submitList(data.items)
        }
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            swipeLayout?.setMargin(
                spaceZero,
                NavToolbarExt.getToolbarHeight(it),
                spaceZero,
                spaceZero
            )
            swipeLayout?.setOnRefreshListener {
                onRefreshLayout()
            }
        }
    }

    private fun resetSwipeLayout() {
        swipeLayout?.isEnabled = true
        swipeLayout?.isRefreshing = false
    }

    private fun resetMovingPosition() {
        movingPosition = 0
    }

    private fun removeAllScrollListener() {
        rvHome?.removeOnScrollListener(loadMoreListener)
    }

    private fun addScrollListener() {
        rvHome?.addOnScrollListener(loadMoreListener)
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollProductList()
            }
        }
    }

    private fun onScrollProductList() {
        val layoutManager = rvHome?.layoutManager as? LinearLayoutManager
        val index = layoutManager?.findLastVisibleItemPosition().orZero()
        val itemCount = layoutManager?.itemCount.orZero()
        localCacheModel?.let {
            viewModel.onScrollProductList(
                index,
                itemCount,
                localCacheModel = it
            )
        }
    }

    private fun onRefreshLayout() {
        resetMovingPosition()
        removeAllScrollListener()
        rvLayoutManager?.setScrollEnabled(true)
        loadLayout()
    }

    private fun showUSPBottomSheet(uspResponse: USPResponse) {
        val tokoFoodUSPBottomSheet = TokoFoodUSPBottomSheet.getInstance()
        tokoFoodUSPBottomSheet.setUSP(uspResponse, getString(com.tokopedia.tokofood.R.string.home_usp_bottom_sheet_title))
        tokoFoodUSPBottomSheet.show(parentFragmentManager, "")
    }

    private fun showChooseAddressBottomSheet() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(this)
        chooseAddressBottomSheet.show(childFragmentManager, "")
    }

    private fun checkAddressDataAndServiceArea(){
        checkIfChooseAddressWidgetDataUpdated()

        if (hasNoAddress()) {
            showNoAddress()
        } else if (hasNoPinPoin()){
            showNoPinPoin()
        } else {
            showLayout()
        }
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        if (isChooseAddressWidgetDataUpdated()) {
            updateCurrentPageLocalCacheModelData()
        }
    }

    private fun isChooseAddressWidgetDataUpdated(): Boolean {
        localCacheModel?.let {
            context?.apply {
                return ChooseAddressUtils.isLocalizingAddressHasUpdated(
                    this,
                    it
                )
            }
        }
        return false
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun hasNoAddress(): Boolean {
        return userSession.isLoggedIn && localCacheModel?.address_id.isNullOrEmpty()
    }

    private fun hasNoPinPoin(): Boolean {
        return userSession.isLoggedIn && !localCacheModel?.address_id.isNullOrEmpty()
                && (localCacheModel?.lat.isNullOrEmpty() || localCacheModel?.long.isNullOrEmpty() ||
                localCacheModel?.lat.equals(EMPTY_LOCATION) || localCacheModel?.long.equals(EMPTY_LOCATION))
    }

    private fun navigateToSetPinpoint() {
        val locationPass =  LocationPass().apply {
            latitude = TOTO_LATITUDE
            longitude = TOTO_LONGITUDE
        }
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        val bundle = Bundle().apply {
            putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
        }
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_SET_PINPOINT)
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val locationPass = it.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
                locationPass?.let { locationPass ->
                    localCacheModel?.address_id?.let { addressId ->
                        viewModel.updatePinPoin(addressId, locationPass.latitude, locationPass.longitude)
                    }
                }
            }
        }
    }

    private fun onResultFromAddAddress(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val addressDataModel = data?.getParcelableExtra<SaveAddressDataModel>(NEW_ADDRESS_PARCELABLE)
                addressDataModel?.let {
                    setupChooseAddress(it)
                }
            }
        }
    }

    private fun setupChooseAddress(data: GetStateChosenAddressResponse) {
        data.let { chooseAddressData ->
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = requireContext(),
                addressId = chooseAddressData.data.addressId.toString(),
                cityId = chooseAddressData.data.cityId.toString(),
                districtId = chooseAddressData.data.districtId.toString(),
                lat = chooseAddressData.data.latitude,
                long = chooseAddressData.data.longitude,
                label = String.format(
                    "%s %s",
                    chooseAddressData.data.addressName,
                    chooseAddressData.data.receiverName
                ),
                postalCode = chooseAddressData.data.postalCode,
                warehouseId = chooseAddressData.tokonow.warehouseId.toString(),
                shopId = chooseAddressData.tokonow.shopId.toString(),
                warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(chooseAddressData.tokonow.warehouses),
                serviceType = chooseAddressData.tokonow.serviceType
            )
        }
        checkIfChooseAddressWidgetDataUpdated()
        loadLayout()
    }

    private fun setupChooseAddress(addressDataModel: SaveAddressDataModel) {
        context?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(it,
                addressDataModel.id.toString(), addressDataModel.cityId.toString(), addressDataModel.districtId.toString(),
                addressDataModel.latitude, addressDataModel.longitude, "${addressDataModel.addressName} ${addressDataModel.receiverName}",
                addressDataModel.postalCode, addressDataModel.shopId.toString(), addressDataModel.warehouseId.toString(),
                TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(addressDataModel.warehouses), addressDataModel.serviceType)
        }
        checkIfChooseAddressWidgetDataUpdated()
        loadLayout()
    }

    private fun showToaster(message: String?) {
        view?.let {
            if (!message.isNullOrEmpty()) {
                Toaster.build(it, message, Toaster.LENGTH_LONG).show()
            }
        }
    }

    private fun chooseAddressWidgetRemoved() {

    }
}