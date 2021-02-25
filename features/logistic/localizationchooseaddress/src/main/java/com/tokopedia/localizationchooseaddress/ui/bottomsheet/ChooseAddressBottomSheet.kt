package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.localizationchooseaddress.di.ChooseAddressComponent
import com.tokopedia.localizationchooseaddress.di.DaggerChooseAddressComponent
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.SaveAddressDataModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.localizationchooseaddress.domain.model.DistrictRecommendationAddressModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class ChooseAddressBottomSheet : BottomSheetUnify(), HasComponent<ChooseAddressComponent>, AddressListItemAdapter.AddressListItemAdapterListener{

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ChooseAddressViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ChooseAddressViewModel::class.java]
    }

    private val adapter = AddressListItemAdapter(this)
    private var chooseAddressLayout: ConstraintLayout? = null
    private var noAddressLayout: ConstraintLayout? = null
    private var loginLayout: ConstraintLayout? = null
    private var buttonLogin: IconUnify? = null
    private var buttonAddAddress: IconUnify? = null
    private var buttonSnippetLocation: IconUnify? = null
    private var addressList: RecyclerView? = null
    private var listener: ChooseAddressBottomSheetListener? = null

    private var txtLocalization: Typography? = null
    private var contentLayout: FrameLayout? = null
    private var bottomLayout: ConstraintLayout? = null
    private var errorLayout: ConstraintLayout? = null
    private var progressBar: LoaderUnify? = null
    private var source: String = ""

    private var fm: FragmentManager? = null
    private var chooseAddressPref: ChooseAddressSharePref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setInitialViewState()
        if (userSession.isLoggedIn) {
            initObserver()
        } else setViewState(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_ADD_ADDRESS -> {
                val saveAddressDataModel = data?.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
                if (saveAddressDataModel != null) {
                    viewModel.setStateChosenAddress(
                            status = 2,
                            addressId = saveAddressDataModel.id.toString(),
                            receiverName = saveAddressDataModel.receiverName,
                            addressName = saveAddressDataModel.addressName,
                            latitude = saveAddressDataModel.latitude,
                            longitude = saveAddressDataModel.longitude,
                            districtId = saveAddressDataModel.districtId.toString(),
                            postalCode = saveAddressDataModel.postalCode
                    )
                }
            }
            REQUEST_CODE_GET_DISTRICT_RECOM -> {
                val discomModel = data?.getParcelableExtra<DistrictRecommendationAddressModel>("district_recommendation_address")
                val latitude = data?.getStringExtra("latitude")
                val longitude = data?.getStringExtra("longitude")
                if (discomModel != null) {
                    viewModel.setStateChosenAddress(
                            status = 4,
                            addressId = null,
                            addressName = "",
                            receiverName = "",
                            districtId = discomModel.districtId.toString(),
                            latitude = latitude,
                            longitude = longitude,
                            postalCode = ""

                    )
                }
            }
            REQUEST_CODE_ADDRESS_LIST -> {
                listener?.onAddressDataChanged()
                this.dismiss()
            }
            REQUEST_CODE_LOGIN_PAGE -> {
                viewModel.getDefaultChosenAddress("", source)
                setInitialViewState()
            }
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initLayout() {
        val view = View.inflate(context, R.layout.bottomsheet_choose_address, null)
        setupView(view)
        setChild(view)
        setCloseClickListener { this.dismiss() }
    }

    private fun setupView(child: View) {
        chooseAddressPref = ChooseAddressSharePref(context)
        chooseAddressLayout = child.findViewById(R.id.choose_address_layout)
        noAddressLayout = child.findViewById(R.id.no_address_layout)
        loginLayout = child.findViewById(R.id.login_layout)
        buttonLogin = child.findViewById(R.id.btn_chevron_login)
        buttonAddAddress = child.findViewById(R.id.btn_chevron_add)
        buttonSnippetLocation = child.findViewById(R.id.btn_chevron_snippet)
        addressList = child.findViewById(R.id.rv_address_card)

        txtLocalization = child.findViewById(R.id.txt_bottomsheet_localization)
        contentLayout = child.findViewById(R.id.frame_content_layout)
        bottomLayout = child.findViewById(R.id.bottom_layout)
        errorLayout = child.findViewById(R.id.error_state_layout)
        progressBar = child.findViewById(R.id.progress_bar)

        addressList?.adapter = adapter
        addressList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initData() {
        source = listener?.getLocalizingAddressHostSourceBottomSheet().toString()
        viewModel.getChosenAddressList(source)
    }

    private fun initObserver() {
        viewModel.chosenAddressList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    adapter.updateData(it.data)
                    setViewState(true)
                }

                is Fail -> {
                    setErrorViewState()
                }
            }
        })

        viewModel.setChosenAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    ChooseAddressTracking.onClickAvailableAddress(userSession.userId, IS_SUCCESS)
                    val data = it.data
                    val localData = ChooseAddressUtils.setLocalizingAddressData(
                            addressId = data.addressId.toString(),
                            cityId = data.cityId.toString(),
                            districtId = data.districtId.toString(),
                            lat = data.latitude,
                            long = data.longitude,
                            label = data.addressName,
                            postalCode = data.postalCode
                    )
                    chooseAddressPref?.setLocalCache(localData)
                    listener?.onAddressDataChanged()
                    this.dismiss()
                }

                is Fail -> {
                    ChooseAddressTracking.onClickAvailableAddress(userSession.userId, IS_NOT_SUCCESS)
                    listener?.onLocalizingAddressServerDown()
                    this.dismiss()
                }

            }
        })

        viewModel.getDefaultAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val data = it.data.addressData
                    val localData = ChooseAddressUtils.setLocalizingAddressData(
                            addressId = data.addressId.toString(),
                            cityId = data.cityId.toString(),
                            districtId = data.districtId.toString(),
                            lat = data.latitude,
                            long = data.longitude,
                            label = data.addressName,
                            postalCode = data.postalCode.toString()
                    )
                    chooseAddressPref?.setLocalCache(localData)
                    listener?.onLocalizingAddressLoginSuccessBottomSheet()
                    this.dismiss()
                }

                is Fail -> {
                    listener?.onLocalizingAddressServerDown()
                    this.dismiss()
                }
            }
        })
    }

    private fun setInitialViewState() {
        progressBar?.visible()
        txtLocalization?.gone()
        contentLayout?.gone()
        bottomLayout?.gone()
        errorLayout?.gone()
        setTitle("")
    }

    private fun setViewState(loginState: Boolean) {
        if (!loginState) {
            progressBar?.gone()
            txtLocalization?.visible()
            contentLayout?.visible()
            bottomLayout?.visible()
            errorLayout?.gone()
            chooseAddressLayout?.gone()
            noAddressLayout?.gone()
            loginLayout?.visible()
        } else {
            if (adapter.addressList.isEmpty()) {
                progressBar?.gone()
                txtLocalization?.visible()
                contentLayout?.visible()
                bottomLayout?.visible()
                errorLayout?.gone()
                chooseAddressLayout?.gone()
                noAddressLayout?.visible()
                loginLayout?.gone()
            } else {
                progressBar?.gone()
                txtLocalization?.visible()
                contentLayout?.visible()
                bottomLayout?.visible()
                errorLayout?.gone()
                chooseAddressLayout?.visible()
                noAddressLayout?.gone()
                loginLayout?.gone()
            }
        }
        setTitle("Mau kirim belanjaan ke mana")
        renderButton()
        setCloseClickListener {
            this.dismiss()
        }
    }

    private fun renderButton() {
        buttonLogin?.setOnClickListener {
            ChooseAddressTracking.onClickMasukBottomSheet(userSession.userId)
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN_PAGE)
        }

        buttonAddAddress?.setOnClickListener {
            ChooseAddressTracking.onClickButtonTambahAlamatBottomSheet(userSession.userId)
            startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2).apply {
                putExtra(EXTRA_IS_FULL_FLOW, true)
                putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
            }, REQUEST_CODE_ADD_ADDRESS)
        }

        buttonSnippetLocation?.setOnClickListener {
            ChooseAddressTracking.onClickPilihKotaKecamatan(userSession.userId)
            startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS).apply {
                putExtra(IS_LOCALIZATION, true)
            }, REQUEST_CODE_GET_DISTRICT_RECOM)
        }
    }

    private fun setErrorViewState() {
        progressBar?.gone()
        txtLocalization?.gone()
        contentLayout?.gone()
        bottomLayout?.gone()
        errorLayout?.visible()
        setTitle("")
        setCloseClickListener {
            ChooseAddressTracking.onClickCloseBottomSheet(userSession.userId)
            listener?.onLocalizingAddressServerDown()
            this.dismiss()
        }
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        fm?.let {
            show(it, "")
        }
    }

    fun setListener(listener: ChooseAddressBottomSheetListener) {
        this.listener = listener
    }

    private fun showError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun getComponent(): ChooseAddressComponent {
        return DaggerChooseAddressComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
        const val EXTRA_IS_LOGISTIC_LABEL = "EXTRA_IS_LOGISTIC_LABEL"
        const val EXTRA_IS_LOCALIZATION = "EXTRA_IS_LOCALIZATION"
        const val IS_LOCALIZATION = "is_localization"
        const val IS_SUCCESS = "success"
        const val IS_NOT_SUCCESS = "not success"
        const val REQUEST_CODE_ADD_ADDRESS = 199
        const val REQUEST_CODE_GET_DISTRICT_RECOM = 299
        const val REQUEST_CODE_ADDRESS_LIST = 399
        const val REQUEST_CODE_LOGIN_PAGE = 499
    }

    override fun onItemClicked(address: ChosenAddressList) {
        viewModel.setStateChosenAddress(
                status = address.status,
                addressId = address.addressId,
                receiverName =  address.receiverName,
                addressName = address.addressname,
                latitude = address.latitude,
                longitude = address.longitude,
                districtId = address.districtId,
                postalCode = address.postalCode
        )
    }

    override fun onOtherAddressClicked() {
        ChooseAddressTracking.onClickCekAlamatLainnya(userSession.userId)
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        intent.putExtra(EXTRA_IS_LOCALIZATION, true)
        startActivityForResult(intent, REQUEST_CODE_ADDRESS_LIST)
    }

    interface ChooseAddressBottomSheetListener {
        /**
         * this listen if we get server down on widget/bottomshet.
         * Host mandatory to GONE LocalizingAddressWidget
         */
        fun onLocalizingAddressServerDown();

        /**
         * Only use by bottomsheet, to notify every changes in address data
         */
        fun onAddressDataChanged()

        /**
         * String Source of Host Page
         */
        fun getLocalizingAddressHostSourceBottomSheet(): String

        /**
         * this listen is use to notify host/fragment if login is success from bottomshet
         */
        fun onLocalizingAddressLoginSuccessBottomSheet()
    }

}